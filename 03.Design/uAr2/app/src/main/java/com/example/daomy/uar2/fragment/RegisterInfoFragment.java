package com.example.daomy.uar2.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RegisterInfoFragment extends Fragment {

    private ProgressDialog pDialog;
    SharedPreferences sharedPreferencesToken;

    @BindView(R.id.txtFullNameRegisInfo)
    TextView txtFullNameRegisInfo;
    @BindView(R.id.txtDateOfBirthRegisInfo)
    TextView txtDateOfBirthRegisInfo;
    @BindView(R.id.txtSchoolNameRegisInfo)
    TextView txtSchoolNameRegisInfo;
    @BindView(R.id.txtEmailRegisInfo)
    TextView txtEmailRegisInfo;
    @BindView(R.id.txtPasswordRegisInfo)
    TextView txtPasswordRegisInfo;
    @BindView(R.id.imgRegisInfo)
    ImageView imgRegisInfo;

    public RegisterInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.registration_info, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.logging_in));
        pDialog.setCancelable(false);
        sharedPreferencesToken=getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
    }

    private void addEvents() {

        Bundle bundle = getArguments();
        String fullName = bundle.getString("fullName");
        final String emailRegis = bundle.getString("emailRegis");
        String dateOfBirth = bundle.getString("dateOfBirth");
        String schoolName = bundle.getString("schoolName");
        final String passwordRegis = bundle.getString("passwordRegis");

        String passwordRegis1 = "";
        for (int i = 0;i<passwordRegis.length();i++){
            passwordRegis1 = passwordRegis1+"*";
        }


        txtFullNameRegisInfo.setText(fullName);
        txtEmailRegisInfo.setText(emailRegis);
        txtDateOfBirthRegisInfo.setText(dateOfBirth);
        txtSchoolNameRegisInfo.setText(schoolName);
        txtPasswordRegisInfo.setText(passwordRegis1);

        imgRegisInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.show();

                APIService service = APIClient.getClient().create(APIService.class);

                Call<MSG> userCall = service.userLogIn(emailRegis,passwordRegis);

                userCall.enqueue(new Callback<MSG>() {
                    @Override
                    public void onResponse(Call<MSG> call, Response<MSG> response) {
                        pDialog.dismiss();
                        //onSignupSuccess();
                        Log.d("onResponse", "" + response.body().getMessage());


                        if(response.body().getSuccess() == 1) {
                            SharedPreferences.Editor editor=sharedPreferencesToken.edit();
                            editor.putString("token",response.body().getToken());
                            editor.apply();
                            getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new MyPageFragment()).commit();
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Call<MSG> call, Throwable t) {
                        pDialog.dismiss();
                        Log.d("onFailure", t.toString());
                    }
                });
            }
        });
    }
}
