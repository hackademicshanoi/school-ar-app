package com.example.daomy.uar2.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.daomy.uar2.R;
import com.example.daomy.uar2.api.APIClient;
import com.example.daomy.uar2.api.APIService;
import com.example.daomy.uar2.model.MSG;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private static long SPLASH_MILLIS = 1000;
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferencesToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.activity_splash_screen, null, false);

        addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.checking_account));
        pDialog.setCancelable(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                sharedPreferencesToken=getSharedPreferences("token", Context.MODE_PRIVATE);
                String tokenSP=sharedPreferencesToken.getString("token","");
                if (!tokenSP.equals("")) {
                    checkTokenByServer();
                    finish();

                } else {
                    Intent intent = new Intent(SplashScreenActivity.this,
                            MainActivity.class);
                    intent.putExtra("success", 0);
                    startActivity(intent);
                    finish();
                }
                //overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

            }

        }, SPLASH_MILLIS);
    }

    private void checkTokenByServer() {

        //pDialog.show();

        String tokenSP=sharedPreferencesToken.getString("token","");


        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.checkToken(tokenSP);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                //pDialog.dismiss();
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());


                if(response.body().getSuccess() == 0) {
                    SharedPreferences.Editor editor=sharedPreferencesToken.edit();
                    editor.putString("token","");
                    editor.apply();
                }

                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                intent.putExtra("success", response.body().getSuccess());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                //pDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }
}
