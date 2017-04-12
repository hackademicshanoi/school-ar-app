package com.example.daomy.uar2.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

    public RegisterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.registration, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Creating Account...");
        pDialog.setCancelable(false);

        arrSchool = new ArrayList<>();
        adapterChool = new SpinnerSchoolAdapter(getActivity(),R.layout.spinner_item,arrSchool);
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

        final String firstNameRegis = txtFirstNameRegis.getText().toString();
        final String lastNameRegis = txtLastNameRegis.getText().toString();
        final String emailRegis = txtEmailRegis.getText().toString();
        final String passwordRegis = txtPasswordRegis.getText().toString();
        String dateOfBirth = spYearRegis.getSelectedItem().toString()+"-"
                +spMonthRegis.getSelectedItem().toString()+"-"
                +spDayRegis.getSelectedItem().toString();
        int idSchool = arrSchool.get(spSchoolRegis.getSelectedItemPosition()).getId();
        final String schoolName = arrSchool.get(spSchoolRegis.getSelectedItemPosition()).getSchool_name();


        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.userSignUp(emailRegis,passwordRegis,firstNameRegis,lastNameRegis,dateOfBirth,idSchool,null,null);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                pDialog.dismiss();
                //onSignupSuccess();


                if(response.body().getSuccess() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fullName",firstNameRegis+" "+lastNameRegis);
                    bundle.putString("emailRegis",emailRegis);
                    bundle.putString("dateOfBirth",spYearRegis.getSelectedItem().toString()+" "+getResources().getString(R.string.year)
                    +" "+spMonthRegis.getSelectedItem().toString()+" "+getResources().getString(R.string.month)+" "+spDayRegis.getSelectedItem().toString()+" "
                    +getResources().getString(R.string.day));
                    bundle.putString("schoolName",schoolName);
                    bundle.putString("passwordRegis",passwordRegis);

                    RegisterInfoFragment registerInfoFragment = new RegisterInfoFragment();
                    registerInfoFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, registerInfoFragment).commit();
                }else {
//
                    Snackbar snackbar = Snackbar
                            .make(regislayout, getResources().getString(R.string.email_exist), Snackbar.LENGTH_LONG)
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
                getActivity(),
                android.R.layout.simple_spinner_item,
                arrYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYearRegis.setAdapter(adapterYear);

        spYearRegis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(spYearRegis.getSelectedItem().toString());
                int month = Integer.parseInt(spMonthRegis.getSelectedItem().toString());
                int spDayRegisPos = spDayRegis.getSelectedItemPosition();
                List<String> arrDay = Arrays.asList(getResources().getStringArray(R.array.item_day));
                //Set sự kiện thay đổi khi chọn vào năm nhuận, nếu tháng đang chọn là tháng 2
                if(((year%4==0)&&(year%100!=0)&&(month==2))||((year%400==0)&&(month==2))) {
                    arrDay = arrDay.subList(0, 29);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                    //Giữ vị trí ngày được chọn ở spDayRegis khi thay đổi giá trị năm
                    spDayRegis.setSelection(Integer.parseInt(arrDay.get(spDayRegisPos)) - 1);

                    //Nếu không phải là năm nhuận mà đang chọn tháng 2
                } else if (((year%4!=0)&&(month==2))||((year%100==0)&&(year%400!=0)&&(month==2))){
                    arrDay = arrDay.subList(0, 28);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                    //Nếu chuyển từ năm nhuận sang năm khác thì sẽ bị tràn mảng, kiểm tra xem có tràn hay không
                    //Nếu tràn thì set vị trí ngày mặc định về 0, không thì lưu cái cũ
                    if (spDayRegisPos==arrDay.size()){
                        spDayRegis.setSelection(0);
                    } else {
                        spDayRegis.setSelection(Integer.parseInt(arrDay.get(spDayRegisPos)) - 1);
                    }
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
                getActivity(),
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
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arrDay);
                    adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDayRegis.setAdapter(adapterDay);
                } else if (position == 1) {
                    //Kiểm tra năm nhuận
                    if(((year%4==0)&&(year%100!=0))||(year%400==0)) {
                        arrDay = arrDay.subList(0, 29);
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                arrDay);
                        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDayRegis.setAdapter(adapterDay);
                    }
                    else {
                        arrDay = arrDay.subList(0, 28);
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                arrDay);
                        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDayRegis.setAdapter(adapterDay);
                    }
                } else {
                    arrDay = arrDay.subList(0,30);
                    ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                            getActivity(),
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

        if (spSchoolRegis.getSelectedItemPosition()==0){
            View view = spSchoolRegis.getSelectedView();
            adapterChool.setError(view, getActivity().getString(R.string.error_spinner_school));
            valid=false;
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }







    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
