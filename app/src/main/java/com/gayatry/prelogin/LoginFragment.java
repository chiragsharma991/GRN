package com.gayatry.prelogin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.base.MainActivity;
import com.gayatry.model.YearModel;
import com.gayatry.rest.JsonParser;
import com.gayatry.storage.SharedPreferenceUtil;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Admin on 04-Apr-16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "LoginFragment";
    private Button mBtnLogin;
    private EditText edtPass, edtEmail;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout mLinParent;
    private Spinner mSpnrYearCode;
    private ProgressBar mYearCodeProgress;
    private ArrayList<YearModel> mYearModelList;
    private AsyncTaskCommon asyncTaskCommon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getYearCode();
        mBtnLogin.setOnClickListener(this);
        mSpnrYearCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getYearCode() {
        if(AppUtils.isOnline(getActivity())){
            asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    mYearCodeProgress.setVisibility(View.GONE);
                    handleYearCodeResponse(result);
                }
            });
            asyncTaskCommon.execute(getString(R.string.get_year_list_url));
        }else{
            mYearCodeProgress.setVisibility(View.GONE);
            AppUtils.showSnakbar(mLinParent, ""+getString(R.string.error_internet));
        }
    }

    private void handleYearCodeResponse(String result) {
        try{
            if(result!="") {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    mYearModelList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        YearModel yearModel = new YearModel();
                        yearModel.setYearCode(jsonArray.getJSONObject(i).getString("Year_Code"));
                        yearModel.setYear(jsonArray.getJSONObject(i).getString("Year"));
                        mYearModelList.add(yearModel);
                    }
                    setSpinnerAdapter();
                } else {
                    AppUtils.showSnakbar(mLinParent, getString(R.string.no_data_available));
                }
            }else {
                AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
            }
        }catch (Exception e){
            e.printStackTrace();
            AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
        }
    }

    private void setSpinnerAdapter(){
        SpinnerAdapter adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getYear());
        mSpnrYearCode.setAdapter(adap);
    }

    private void initView(View view) {
        edtEmail = (EditText) view.findViewById(R.id.edtUsername);
        edtPass = (EditText) view.findViewById(R.id.edtPass);
        mBtnLogin = (Button) view.findViewById(R.id.btnLogin);
        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
        mLinParent = (LinearLayout) view.findViewById(R.id.linParent);
        mSpnrYearCode = (Spinner) view.findViewById(R.id.spnrYearCode);
        mYearCodeProgress = (ProgressBar) view.findViewById(R.id.year_code_progress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                if(validate()){
                    if(AppUtils.isOnline(getContext())){
                        if(mYearModelList != null && mYearModelList.size() > 0)
                            new LoginAttemptAsync().execute(edtEmail.getText().toString().trim(), edtPass.getText().toString(), getYearCode(mSpnrYearCode.getSelectedItemPosition()));
                    }else {
                        AppUtils.showSnakbar(mLinParent, ""+getString(R.string.error_internet));
                    }
                }
                break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(asyncTaskCommon != null && asyncTaskCommon.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTaskCommon.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(asyncTaskCommon != null && asyncTaskCommon.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTaskCommon.cancel(true);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(edtPass.getText().toString())) {
            edtPass.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    public ArrayList<String> getYear() {
        ArrayList<String> yearList = new ArrayList<>();
        for (YearModel model : mYearModelList){
            yearList.add(model.getYear());
        }
        return yearList;
    }

    public String getYearCode(int pos) {
        return mYearModelList.get(pos).getYearCode();
    }

    private class LoginAttemptAsync extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.login_url)+"/"+params[0]+"/"+params[1]+"/"+params[2]);
                //return jsonParser.getResponse(getString(R.string.login_url)+"/admin/123/"+params[2]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            handleResponse(response);
        }
    }

    private void handleResponse(String response) {
        try{
            /*SharedPreferenceUtil.putValue(AppUtils.IS_LOGIN, true);
            SharedPreferenceUtil.putValue(AppUtils.USER_FULL_NAME, "Testing");
            SharedPreferenceUtil.putValue(AppUtils.USER_ID, "1");
            SharedPreferenceUtil.save();
            ((MainActivity) getActivity()).moveProjectListFragment();*/
            if(response!="") {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    SharedPreferenceUtil.putValue(AppUtils.IS_LOGIN, true);
                    SharedPreferenceUtil.putValue(AppUtils.USER_FULL_NAME, jsonArray.getJSONObject(0).getString("UName"));
                    SharedPreferenceUtil.putValue(AppUtils.USER_ID, jsonArray.getJSONObject(0).getString("User_Id"));
                    SharedPreferenceUtil.putValue(AppUtils.PROFILE_PIC, jsonArray.getJSONObject(0).getString("imgurl"));
                    SharedPreferenceUtil.putValue(AppUtils.USER_NAME, jsonArray.getJSONObject(0).getString("UserLoginName"));
                    SharedPreferenceUtil.putValue(AppUtils.ROLE_USER, jsonArray.getJSONObject(0).getString("Role_name"));
                    SharedPreferenceUtil.putValue(AppUtils.YEAR_CODE, ""+mYearModelList.get(mSpnrYearCode.getSelectedItemPosition()).getYearCode());
                    SharedPreferenceUtil.save();
                    ((MainActivity) getActivity()).moveProjectListFragment();
                } else {
                    showProgress(false);
                    AppUtils.showSnakbar(mLinParent, "" + getString(R.string.error_server_login));
                }
            }else {
                showProgress(false);
                AppUtils.showSnakbar(mLinParent, "" + getString(R.string.error_server));
            }
        }catch (Exception e){
            e.printStackTrace();
            showProgress(false);
            AppUtils.showSnakbar(mLinParent, "" + getString(R.string.error_server));
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
