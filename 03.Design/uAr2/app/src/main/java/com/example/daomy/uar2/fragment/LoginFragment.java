package com.example.daomy.uar2.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daomy.uar2.R;
import com.example.daomy.uar2.api.APIClient;
import com.example.daomy.uar2.api.APIService;
import com.example.daomy.uar2.model.MSG;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferencesToken;
    @BindView(R.id.txtEmailLogin)
    EditText txtEmailLogin;
    @BindView(R.id.txtPasswordLogin)
    EditText txtPasswordLogin;
    @BindView(R.id.imgLogin)
    ImageView imgLogin;
    @BindView(R.id.ConstraintLayout)
    ConstraintLayout constraintLayout;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.logging_in));
        pDialog.setCancelable(false);
        // Lấy token từ  Preferences
        sharedPreferencesToken=getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
    }

    private void addEvents() {
        imgLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
    /// Nếu validate trả giá trị true thì bắt làm lại .Validate là kiểm tra các thông số truyền vào email ,password
        if (!validate()) {
            onLoginFailed();
            return;
        }

        //imgLogin.setEnabled(false);

        loginByServer();
    }

    private void loginByServer() {

        pDialog.show();

        String email = txtEmailLogin.getText().toString();
        String password = txtPasswordLogin.getText().toString();


        APIService service = APIClient.getClient().create(APIService.class);
        // Gưi email ,pass đến server qua mẫu userCall của APIService
        Call<MSG> userCall = service.userLogIn(email,password);
// Nhận lại trả lời từ server bằng mẫu của MSG .
        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());

    // json success trả lại 1 nghĩa là thành công thực hiện lưu 1 PreferenToken để so sánh đăng nhập lầ sau
                if(response.body().getSuccess() == 1) {
                    SharedPreferences.Editor editor=sharedPreferencesToken.edit();
                    editor.putString("token",response.body().getToken());
                    editor.apply();
                    getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new MyPageFragment()).commit();
                }else {
//                    Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getMessage().equals("Wrong email")) {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, getResources().getString(R.string.wrong_email), Snackbar.LENGTH_LONG)
                                .setAction(getResources().getString(R.string.signup_for_wrong_email), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new RegisterFragment()).commit();
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                    else if (response.body().getMessage().equals("Wrong password")) {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, getResources().getString(R.string.wrong_password), Snackbar.LENGTH_LONG)
                                .setAction(getResources().getString(R.string.recovery_for_wrong_password), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new ReminderFragment()).commit();
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
<<<<<<< HEAD:03.Design/uAr2/app/src/main/java/com/example/daomy/uar2/fragment/LoginFragment.java
=======
                    else {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, response.body().getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("SIGNUP", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
>>>>>>> c3b4787... code màn hình login & register + update api + code web php login & register:03.Design/uAr2/app/src/main/java/com/example/daomy/uar2/activity/LoginActivity.java
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                pDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }


    public void onLoginSuccess() {
        imgLogin.setEnabled(true);
//        finish();
    }

    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        Snackbar snackbar = Snackbar
//                .make(constraintLayout, "Login failed", Snackbar.LENGTH_LONG);
//
//        // Changing action button text color
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.YELLOW);
//        snackbar.show();
//
//        imgLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = txtEmailLogin.getText().toString();
        String password = txtPasswordLogin.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
<<<<<<< HEAD:03.Design/uAr2/app/src/main/java/com/example/daomy/uar2/fragment/LoginFragment.java
            txtEmailLogin.setError(getResources().getString(R.string.txtEmailLogin_setError));
=======
            txtEmailLogin.setError("Enter a valid email address");
>>>>>>> c3b4787... code màn hình login & register + update api + code web php login & register:03.Design/uAr2/app/src/main/java/com/example/daomy/uar2/activity/LoginActivity.java
            requestFocus(txtEmailLogin);
            valid = false;
        } else {
            txtEmailLogin.setError(null);
        }

        if (password.isEmpty()) {
            txtPasswordLogin.setError(getResources().getString(R.string.txtPasswordLogin_setError));
            requestFocus(txtPasswordLogin);
            valid = false;
        } else {
            txtPasswordLogin.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
