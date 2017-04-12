package com.example.daomy.uar2.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    @BindView(R.id.txtEmailReminder)
    EditText txtEmailReminder;
    @BindView(R.id.imgSendReminder)
    ImageView imgSendReminder;
    @BindView(R.id.imgBackReminder)
    ImageView imgBackReminder;
    @BindView(R.id.layoutReminder)
    FrameLayout layoutReminder;

    private ProgressDialog pDialog;
    private static final String TAG = "ReminderFragment";


    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.reminder, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Checking email...");
        pDialog.setCancelable(false);
    }

    private void addEvents() {
        imgSendReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendingEmail();
            }
        });

        imgBackReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


    }



    private void sendingEmail() {
        Log.d(TAG, "Reminder");

        if (validate() == false) {
            onSendingFailed();
            return;
        }

        //imgLogin.setEnabled(false);

        sendingByServer();
    }

    private void onSendingFailed() {

    }

    private boolean validate() {
        boolean valid = true;

        String email = txtEmailReminder.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmailReminder.setError("Enter a valid email address");
            requestFocus(txtEmailReminder);
            valid = false;
        } else {
            txtEmailReminder.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void sendingByServer() {
        pDialog.show();

        String email = txtEmailReminder.getText().toString();

        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.reminder(email);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());


                if(response.body().getSuccess() == 1) {
                    getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new LoginFragment()).commit();
                }else {
//
                        Snackbar snackbar = Snackbar
                                .make(layoutReminder, getResources().getString(R.string.wrong_email), Snackbar.LENGTH_LONG)
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
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                pDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }

}
