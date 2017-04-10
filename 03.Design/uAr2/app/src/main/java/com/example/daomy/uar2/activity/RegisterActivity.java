package com.example.daomy.uar2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daomy.uar2.MainActivity;
import com.example.daomy.uar2.R;
import com.example.daomy.uar2.adapter.SpinnerSchoolAdapter;
import com.example.daomy.uar2.api.APIClient;
import com.example.daomy.uar2.api.APIService;
import com.example.daomy.uar2.model.MSG;
import com.example.daomy.uar2.model.School;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static String URL_SCHOOL = APIClient.BASE_URL+"login/views/school.php";
    private ProgressDialog pDialog;
    @BindView(R.id.txtFirstNameRegis)
    EditText txtFirstNameRegis;
    @BindView(R.id.txtLastNameRegis)
    EditText txtLastNameRegis;
    @BindView(R.id.txtEmailRegis)
    EditText txtEmailRegis;
    @BindView(R.id.txtPasswordRegis)
    EditText txtPasswordRegis;
    @BindView(R.id.spYearRegis)
    Spinner spYearRegis;
    @BindView(R.id.spMonthRegis)
    Spinner spMonthRegis;
    @BindView(R.id.spDayRegis)
    Spinner spDayRegis;
    @BindView(R.id.spSchoolRegis)
    Spinner spSchoolRegis;
    @BindView(R.id.chkAgreeRegis)
    CheckBox chkAgreeRegis;
    @BindView(R.id.imgRegis)
    ImageView imgRegis;
    @BindView(R.id.regislayout)
    FrameLayout regislayout;

    ArrayList<School> arrSchool;
    SpinnerSchoolAdapter adapterChool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        ButterKnife.bind(this);
        addControls();
        addEvents();
    }

    private void addControls() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Creating Account...");
        pDialog.setCancelable(false);

        arrSchool = new ArrayList<>();
        adapterChool = new SpinnerSchoolAdapter(RegisterActivity.this,R.layout.spinner_item,arrSchool);
        adapterChool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSchoolRegis.setAdapter(adapterChool);
    }

    private void addEvents() {
        xuLyThongTinSpinnerYearMonthDay();

        new DocThongTinSchool().execute(URL_SCHOOL);

        imgRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });






    }

    private void register() {
        Log.d(TAG, "Register");

        if (validate() == false) {
            return;
        }

        regisByServer();
    }

    private void regisByServer() {

        pDialog.show();

        String firstNameRegis = txtFirstNameRegis.getText().toString();
        String lastNameRegis = txtLastNameRegis.getText().toString();
        String emailRegis = txtEmailRegis.getText().toString();
        String passwordRegis = txtPasswordRegis.getText().toString();
        String dateOfBirth = spYearRegis.getSelectedItem().toString()+"-"
                +spMonthRegis.getSelectedItem().toString()+"-"
                +spDayRegis.getSelectedItem().toString();
        int idSchool = arrSchool.get(spSchoolRegis.getSelectedItemPosition()).getId();


        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.userSignUp(emailRegis,passwordRegis,firstNameRegis,lastNameRegis,dateOfBirth,idSchool,null,null);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();
                //onSignupSuccess();


                if(response.body().getSuccess() == 1) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else {
//
                        Snackbar snackbar = Snackbar
                                .make(regislayout, response.body().getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("RECOVERY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                pDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });


    }

    class DocThongTinSchool extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    School school = new School();
                    String key = iter.next();
                    school.setId(Integer.parseInt(key));
                    String schoolName = jsonObject.getString(key);
                    school.setSchool_name(schoolName);
                    arrSchool.add(school);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapterChool.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder reqBuilder = new Request.Builder();
                reqBuilder.url(params[0]);
                Request request = reqBuilder.build();
                okhttp3.Response response = okHttpClient.newCall(request).execute();
                String kqTraVe = response.body().string();
                return kqTraVe;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void xuLyThongTinSpinnerYearMonthDay() {
        ArrayList<Integer> arrYear = new ArrayList<>();
        int currentYear = new GregorianCalendar().get(Calendar.YEAR);
        for (int i= (currentYear-100);i<currentYear;i++){
            arrYear.add(i+1);
        }
        Collections.sort(arrYear, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(
                RegisterActivity.this,
                android.R.layout.simple_spinner_item,
                arrYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYearRegis.setAdapter(adapterYear);

        spYearRegis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(spYearRegis.getSelectedItem().toString());
                int month = Integer.parseInt(spMonthRegis.getSelectedItem().toString());
                List<String> arrDay = Arrays.asList(getResources().getStringArray(R.array.item_day));
                //Set sự kiện thay đổi khi chọn vào năm nhuận, nếu tháng đang chọn là tháng 2
                if(((year%4==0)&&(year%100!=0)&&(month==2))||((year%400==0)&&(month==2))) {
                    arrDay = arrDay.subList(0, 29);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            RegisterActivity.this,
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                    //Nếu không phải là năm nhuận mà đang chọn tháng 2
                } else if (((year%4!=0)&&(month==2))||((year%100==0)&&(year%400!=0)&&(month==2))){
                    arrDay = arrDay.subList(0, 28);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            RegisterActivity.this,
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayList<Integer> arrMonth = new ArrayList<>();
        for (int i= 0;i<12;i++){
            arrMonth.add(i+1);
        }
        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<Integer>(
                RegisterActivity.this,
                android.R.layout.simple_spinner_item,
                arrMonth);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonthRegis.setAdapter(adapterMonth);

        spMonthRegis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(spYearRegis.getSelectedItem().toString());
                List<String> arrDay = Arrays.asList(getResources().getStringArray(R.array.item_day));
                if (position == 0 || position == 2 || position == 4 || position == 6 || position == 7 || position == 9
                        || position == 11) {
                    arrDay = arrDay.subList(0,31);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            RegisterActivity.this,
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                } else if (position == 1) {
                    //Kiểm tra năm nhuận
                    if(((year%4==0)&&(year%100!=0))||(year%400==0)) {
                        arrDay = arrDay.subList(0, 29);
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                                RegisterActivity.this,
                                android.R.layout.simple_spinner_item,
                                arrDay);
                        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDayRegis.setAdapter(adapterDay);
                    }
                    else {
                        arrDay = arrDay.subList(0, 28);
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                                RegisterActivity.this,
                                android.R.layout.simple_spinner_item,
                                arrDay);
                        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDayRegis.setAdapter(adapterDay);
                    }
                } else {
                    arrDay = arrDay.subList(0,30);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            RegisterActivity.this,
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String firstNameRegis = txtFirstNameRegis.getText().toString();
        String lastNameRegis = txtLastNameRegis.getText().toString();
        String emailRegis = txtEmailRegis.getText().toString();
        String passwordRegis = txtPasswordRegis.getText().toString();

        if (firstNameRegis.isEmpty()) {
            txtFirstNameRegis.setError("First name is empty");
            requestFocus(txtFirstNameRegis);
            valid = false;
        } else {
            txtFirstNameRegis.setError(null);
        }

        if (lastNameRegis.isEmpty()) {
            txtLastNameRegis.setError("Last name is empty");
            requestFocus(txtLastNameRegis);
            valid = false;
        } else {
            txtLastNameRegis.setError(null);
        }

        if (emailRegis.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailRegis).matches()) {
            txtEmailRegis.setError("Enter a valid email address");
            requestFocus(txtEmailRegis);
            valid = false;
        } else {
            txtEmailRegis.setError(null);
        }

        if (passwordRegis.isEmpty()) {
            txtPasswordRegis.setError("Password is empty");
            requestFocus(txtPasswordRegis);
            valid = false;
        } else {
            txtPasswordRegis.setError(null);
        }

        if (!chkAgreeRegis.isChecked()) {
            chkAgreeRegis.setError("You must agree with Our terms");
            valid = false;
        } else {
            chkAgreeRegis.setError(null);
        }


        return valid;


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}
