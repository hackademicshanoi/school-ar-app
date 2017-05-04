package com.example.daomy.uar2.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.daomy.uar2.R;
import com.example.daomy.uar2.activity.MainActivity;
import com.example.daomy.uar2.adapter.SpinnerSchoolAdapter;
import com.example.daomy.uar2.api.APIClient;
import com.example.daomy.uar2.api.APIService;
import com.example.daomy.uar2.model.MSG;
import com.example.daomy.uar2.model.School;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private static final String TAG = "EditFragment";
    private static String URL_SCHOOL = APIClient.BASE_URL + "login/views/school.php";
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferencesToken;
    ArrayList<Integer> arrYear, arrMonth;
    List<String> arrDay;

    ArrayList<School> arrSchool;
    SpinnerSchoolAdapter adapterChool;

    @BindView(R.id.txtFirstNameEdit)
    EditText txtFirstNameEdit;
    @BindView(R.id.txtLastNameEdit)
    EditText txtLastNameEdit;
    @BindView(R.id.txtPasswordNowEdit)
    EditText txtPasswordNowEdit;
    @BindView(R.id.txtPasswordNewEdit)
    EditText txtPasswordNewEdit;
    @BindView(R.id.txtPasswordNew2Edit)
    EditText txtPasswordNew2Edit;
    @BindView(R.id.spYearEdit)
    Spinner spYearEdit;
    @BindView(R.id.spMonthEdit)
    Spinner spMonthEdit;
    @BindView(R.id.spDayEdit)
    Spinner spDayEdit;
    @BindView(R.id.spSchoolEdit)
    Spinner spSchoolEdit;
    @BindView(R.id.spSchool2Edit)
    Spinner spSchool2Edit;
    @BindView(R.id.imgEditInfo)
    ImageView imgEditInfo;
    @BindView(R.id.editLayout)
    FrameLayout editLayout;


    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.edit, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {
        sharedPreferencesToken = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cheking Account...");
        pDialog.setCancelable(false);

        arrSchool = new ArrayList<>();
        adapterChool = new SpinnerSchoolAdapter(getActivity(), R.layout.spinner_item, arrSchool);
        adapterChool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSchoolEdit.setAdapter(adapterChool);
        spSchool2Edit.setAdapter(adapterChool);

    }

    private void addEvents() {
        xuLyThongTinSpinnerYearMonthDay();

        new DocThongTinSchool().execute(URL_SCHOOL);

        checkTokenAndReturnDataByServer();

        imgEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo();
            }
        });
    }

    private void editInfo() {
        Log.d(TAG, "Edit");

        if (validate() == false) {
            return;
        }

        editByServer();
    }

    private void editByServer() {

        pDialog.show();

        final String firstNameEdit = txtFirstNameEdit.getText().toString();
        final String lastNameEdit = txtLastNameEdit.getText().toString();
        final String passwordNow = txtPasswordNowEdit.getText().toString();
        final String passwordNew = txtPasswordNewEdit.getText().toString();
        String dateOfBirth = spYearEdit.getSelectedItem().toString()+"-"
                +spMonthEdit.getSelectedItem().toString()+"-"
                +spDayEdit.getSelectedItem().toString();
        int idSchool = arrSchool.get(spSchoolEdit.getSelectedItemPosition()).getId();
        int idSchool2 = arrSchool.get(spSchool2Edit.getSelectedItemPosition()).getId();
        String tokenSP = sharedPreferencesToken.getString("token", "");

        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.editInfo(firstNameEdit,lastNameEdit,dateOfBirth,idSchool,idSchool2,passwordNow,passwordNew,tokenSP);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();
                //onSignupSuccess();


                if(response.body().getSuccess() == 1) {

                    String tokenSP = sharedPreferencesToken.getString("token", "");
                    if ((response.body().getToken()!=null)&&(!tokenSP.equals(response.body().getToken())))
                    {
                        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
                        editor.putString("token", response.body().getToken());
                        editor.apply();
                    }

                    getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new MyPageFragment()).commit();
                }else {
//
                    txtPasswordNowEdit.setError(getResources().getString(R.string.wrong_password));
                    requestFocus(txtPasswordNowEdit);
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                pDialog.dismiss();
                Log.d("onFailure", t.toString());
            }
        });


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
                    txtFirstNameEdit.setText(response.body().getFirstName());
                    txtLastNameEdit.setText(response.body().getLastName());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateInString = response.body().getDateOfBirth();
                    try {

                        Date date = simpleDateFormat.parse(dateInString);
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        spYearEdit.setSelection(arrYear.indexOf(calendar.get(Calendar.YEAR)));
                        spMonthEdit.setSelection(arrMonth.indexOf(calendar.get(Calendar.MONTH) + 1));

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spDayEdit.setSelection(arrDay.indexOf(calendar.get(Calendar.DATE) + ""));
                            }
                        }, 50);
                        for (School school : arrSchool) {
                            if (school.getId() == response.body().getIdSchool()) {
                                spSchoolEdit.setSelection(arrSchool.indexOf(school));
                            }
                        }
//                        for (int i = 0; i < arrSchool.size(); i++) {
//                            School school = arrSchool.get(i);
//                            if (school.getId()==(response.body().getIdSchool())){
//                                spSchoolEdit.setSelection(arrSchool.indexOf(school));
//                            }
//                        }
                        for (School school : arrSchool) {
                            if (response.body().getIdSchool2() == 0)
                            {
                                spSchool2Edit.setSelection(0);
                            }
                            else if (school.getId() == response.body().getIdSchool2()) {
                                spSchool2Edit.setSelection(arrSchool.indexOf(school));
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    response.body().getIdSchool();
                    response.body().getIdSchool2();
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
                School schoolSelect = new School();
                schoolSelect.setId(0);
                schoolSelect.setSchool_name(getResources().getString(R.string.select_school_name_spinner));
                arrSchool.add(schoolSelect);
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
        arrYear = new ArrayList<>();
        int currentYear = new GregorianCalendar().get(Calendar.YEAR);
        for (int i = (currentYear - 100); i < currentYear; i++) {
            arrYear.add(i + 1);
        }
        Collections.sort(arrYear, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                arrYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYearEdit.setAdapter(adapterYear);

        arrMonth = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            arrMonth.add(i + 1);
        }
        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<Integer>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                arrMonth);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonthEdit.setAdapter(adapterMonth);

        arrDay = Arrays.asList(getResources().getStringArray(R.array.item_day));
        arrDay = arrDay.subList(0, 31);
        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                arrDay);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayEdit.setAdapter(adapterDay);

        spYearEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(spYearEdit.getSelectedItem().toString());
                int month = Integer.parseInt(spMonthEdit.getSelectedItem().toString());
                int spDayEditPos = spDayEdit.getSelectedItemPosition();
                arrDay = Arrays.asList(getResources().getStringArray(R.array.item_day));
                //Set sự kiện thay đổi khi chọn vào năm nhuận, nếu tháng đang chọn là tháng 2
                if (((year % 4 == 0) && (year % 100 != 0) && (month == 2)) || ((year % 400 == 0) && (month == 2))) {
                    arrDay = arrDay.subList(0, 29);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayEdit.setAdapter(adapterDay);
                    //Giữ vị trí ngày được chọn ở spDayEdit khi thay đổi giá trị năm
                    spDayEdit.setSelection(Integer.parseInt(arrDay.get(spDayEditPos)) - 1);

                    //Nếu không phải là năm nhuận mà đang chọn tháng 2
                } else if (((year % 4 != 0) && (month == 2)) || ((year % 100 == 0) && (year % 400 != 0) && (month == 2))) {
                    arrDay = arrDay.subList(0, 28);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayEdit.setAdapter(adapterDay);
                    //Nếu chuyển từ năm nhuận sang năm khác thì sẽ bị tràn mảng, kiểm tra xem có tràn hay không
                    //Nếu tràn thì set vị trí ngày mặc định về 0, không thì lưu cái cũ
                }
                if (spDayEditPos >= arrDay.size()) {
                    spDayEdit.setSelection(0);
                } else {
                    spDayEdit.setSelection(Integer.parseInt(arrDay.get(spDayEditPos)) - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spMonthEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(spYearEdit.getSelectedItem().toString());
                int spDayEditPos = spDayEdit.getSelectedItemPosition();
                arrDay = Arrays.asList(getResources().getStringArray(R.array.item_day));
                if (position == 0 || position == 2 || position == 4 || position == 6 || position == 7 || position == 9
                        || position == 11) {
                    arrDay = arrDay.subList(0, 31);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayEdit.setAdapter(adapterDay);
                } else if (position == 1) {
                    //Kiểm tra năm nhuận
                    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                        arrDay = arrDay.subList(0, 29);
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                arrDay);
                        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDayEdit.setAdapter(adapterDay);
                    } else {
                        arrDay = arrDay.subList(0, 28);
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                arrDay);
                        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDayEdit.setAdapter(adapterDay);
                    }
                } else {
                    arrDay = arrDay.subList(0, 30);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayEdit.setAdapter(adapterDay);
                }
                if (spDayEditPos >= arrDay.size()) {
                    spDayEdit.setSelection(0);
                } else {
                    spDayEdit.setSelection(Integer.parseInt(arrDay.get(spDayEditPos)) - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String firstNameEdit = txtFirstNameEdit.getText().toString();
        String lastNameEdit = txtLastNameEdit.getText().toString();
        String passwordNowEdit = txtPasswordNowEdit.getText().toString();
        String passwordNewEdit = txtPasswordNewEdit.getText().toString();
        String passwordNew2Edit = txtPasswordNew2Edit.getText().toString();

        if (firstNameEdit.isEmpty()) {
            txtFirstNameEdit.setError(getResources().getString(R.string.first_name_empty));
            requestFocus(txtFirstNameEdit);
            valid = false;
        } else {
            txtFirstNameEdit.setError(null);
        }

        if (lastNameEdit.isEmpty()) {
            txtLastNameEdit.setError(getResources().getString(R.string.last_name_empty));
            requestFocus(txtLastNameEdit);
            valid = false;
        } else {
            txtLastNameEdit.setError(null);
        }

        if (passwordNowEdit.isEmpty()) {
            txtPasswordNowEdit.setError(getResources().getString(R.string.password_is_empty));
            requestFocus(txtPasswordNowEdit);
            valid = false;
        } else {
            txtPasswordNowEdit.setError(null);
        }

        if (!passwordNew2Edit.equals(passwordNewEdit)) {
            txtPasswordNew2Edit.setError(getResources().getString(R.string.password_regis_error));
            requestFocus(txtPasswordNew2Edit);
            valid = false;
        } else {
            txtPasswordNew2Edit.setError(null);
        }

        return valid;


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
