package com.example.daomy.uar2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daomy.uar2.MainActivity;
import com.example.daomy.uar2.R;
import com.example.daomy.uar2.api.APIClient;
import com.example.daomy.uar2.api.APIService;
import com.example.daomy.uar2.model.MSG;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog pDialog;
    @BindView(R.id.txtEmailLogin)
    EditText txtEmailLogin;
    @BindView(R.id.txtPasswordLogin)
    EditText txtPasswordLogin;
    @BindView(R.id.imgLogin)
    ImageView imgLogin;
    @BindView(R.id.ConstraintLayout)
    ConstraintLayout constraintLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        addControls();
        addEvents();
    }

    private void addControls() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging in...");
        pDialog.setCancelable(false);
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

        if (validate() == false) {
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

        Call<MSG> userCall = service.userLogIn(email,password);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());


                if(response.body().getSuccess() == 1) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else {
//                    Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getMessage().equals("Wrong email")) {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, response.body().getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("REGISTER", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                    else if (response.body().getMessage().equals("Wrong password")) {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, response.body().getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("RECOVERY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                pDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }



    public void onLoginSuccess() {
        imgLogin.setEnabled(true);
        finish();
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
            txtEmailLogin.setError("Enter a valid email address");
            requestFocus(txtEmailLogin);
            valid = false;
        } else {
            txtEmailLogin.setError(null);
        }

        if (password.isEmpty()) {
            txtPasswordLogin.setError("Password is empty");
            requestFocus(txtPasswordLogin);
            valid = false;
        } else {
            txtPasswordLogin.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
