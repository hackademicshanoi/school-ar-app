package com.example.daomy.uar2.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
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
import com.example.daomy.uar2.other.RoundedTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MyPageFragment extends Fragment {
    Bitmap bitmap;
    String image_name,encoded_string;
    String tokenSP;

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
    @BindView(R.id.imgProfileMyPage)
    ImageView imgProfileMyPage;

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

        imgProfileMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 10);

            }
        });

        checkTokenAndReturnDataByServer();
    }

    private void checkTokenAndReturnDataByServer() {
        pDialog.show();
        tokenSP = sharedPreferencesToken.getString("token", "");

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
                    Picasso.with(getContext()).load(response.body().getProfilePicture())
                            .transform(new RoundedTransformation(500, 4))
                            .fit()
                            .centerCrop()
                            .into(imgProfileMyPage);
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

                    txtFirstSchoolMyPage.setText(response.body().getIdSchool()+"");
                    txtSecondSchoolMyPage.setText(response.body().getIdSchool2()+"");

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

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 ) {
            try {
                image_name = tokenSP+".png";

                if (data == null){
                    return ;
                }
                else {
                    Uri imageUri = data.getData();
                    InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(is);
                    new Encode_image().execute();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmap.recycle();
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }

    private void makeRequest() {
        APIService service = APIClient.getClient().create(APIService.class);
        Call<MSG> userCall = service.setImage(tokenSP,encoded_string,image_name);
        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                if(response.body().getSuccess() == 1) {
                    APIService service = APIClient.getClient().create(APIService.class);
                    Call<MSG> userCall = service.returnData(tokenSP);
                    userCall.enqueue(new Callback<MSG>() {
                        @Override
                        public void onResponse(Call<MSG> call, final Response<MSG> response) {

                                    Picasso.with(getContext())
                                            .load(response.body().getProfilePicture())
                                            .transform(new RoundedTransformation(500, 4))
                                            .fit()
                                            .centerCrop()
                                            .skipMemoryCache()
                                            .into(imgProfileMyPage);

                        }

                        @Override
                        public void onFailure(Call<MSG> call, Throwable t) {
                            pDialog.dismiss();
                        }
                    });

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

}
