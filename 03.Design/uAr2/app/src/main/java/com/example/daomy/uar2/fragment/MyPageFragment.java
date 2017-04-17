package com.example.daomy.uar2.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.daomy.uar2.activity.MainActivity;
import com.example.daomy.uar2.api.APIClient;
import com.example.daomy.uar2.api.APIService;
import com.example.daomy.uar2.model.MSG;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment {

    private ProgressDialog pDialog;
    SharedPreferences sharedPreferencesToken;

    @BindView(R.id.txtFullNameMyPage)
    TextView txtFullNameMyPage;
    @BindView(R.id.txtDateOfBirthMyPage)
    TextView txtDateOfBirthMyPage;
    @BindView(R.id.txtFirstSchoolMyPage)
    TextView txtFirstSchoolMyPage;
    @BindView(R.id.txtSecondSchoolMyPage)
    TextView txtSecondSchoolMyPage;
    @BindView(R.id.imgCameraMyPage)
    ImageView imgCameraMyPage;
    @BindView(R.id.imgEditMyPage)
    ImageView imgEditMyPage;

    public MyPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mypage, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {
        sharedPreferencesToken = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.checking_account));
        pDialog.setCancelable(false);
    }

    private void addEvents() {
        imgEditMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditFragment editFragment = new EditFragment();
                getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, editFragment).commit();
            }
        });

        checkTokenAndReturnDataByServer();
    }

    private void checkTokenAndReturnDataByServer() {
        pDialog.show();

        String tokenSP = sharedPreferencesToken.getString("token", "");
        APIService service = APIClient.getClient().create(APIService.class);
        Call<MSG> userCall = service.returnData(tokenSP);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();

                if (response.body().getSuccess() == 1) {
                    txtFullNameMyPage.setText(response.body().getFirstName()+" "+response.body().getLastName());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateInString = response.body().getDateOfBirth();
                    try {

                        Date date = simpleDateFormat.parse(dateInString);
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        txtDateOfBirthMyPage.setText(calendar.get(Calendar.YEAR)+" "+getResources().getString(R.string.year)
                        +" "+(calendar.get(Calendar.MONTH)+1)+" "+getResources().getString(R.string.month)
                        +" "+calendar.get(Calendar.DATE)+getResources().getString(R.string.day)+getResources().getString(R.string.born));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    txtFirstSchoolMyPage.setText(response.body().getSchoolName());
                    txtSecondSchoolMyPage.setText(response.body().getSchoolName2());
                } else {
                    SharedPreferences.Editor editor = sharedPreferencesToken.edit();
                    editor.putString("token", "");
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("success", 0);
                    startActivity(intent);
                    getActivity().finish();
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
