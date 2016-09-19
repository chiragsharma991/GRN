package com.gayatry.gtn.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.gtn.AddGTNActivity;
import com.gayatry.gtn.EditGTNActivity;
import com.gayatry.model.EditGTNModel;
import com.gayatry.model.GTNListModel;
import com.gayatry.model.ProjectListModel;
import com.gayatry.storage.SharedPreferenceUtil;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Admin on 27-Apr-16.
 */
public class EditGTNGeneralFragment extends Fragment implements View.OnClickListener{

    private EditText edtGtnCode, edtChallanNo, edtRemark;
    private TextView txtGtnDate, txtChallanDate;
    private Spinner mSpnrProject;
    private AsyncTaskCommon asyncTaskCommon;
    private ProgressBar mProductProgressBar;
    private LinearLayout mLinParent;
    private String strGtnCode, strChallanNo,strRemark,
            strGtnDate, strChallanDate;
    private String GTN_MODEL="gtn_model";
    private GTNListModel gtnListModel;
    private ArrayList<ProjectListModel> mProjectList;
    private RadioGroup mRadioGrpGtnType;
    private ArrayList<EditGTNModel> mEditGTNModelList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_gtn_general, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gtnListModel = (GTNListModel) getArguments().getSerializable(GTN_MODEL);
        mEditGTNModelList = ((EditGTNActivity)getActivity()).mEditGTNModelList;
        initView(view);
        setValues();
        mRadioGrpGtnType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioGrnType = (RadioButton) getView().findViewById(checkedId);
                getGeneratedGtnCode(radioGrnType.getText().toString());
            }
        });
    }

    void getGeneratedGtnCode(String grnType){
        if(AppUtils.isOnline(getActivity())) {
            asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if (result.length() > 0) {
                        try {
                            if (result != "") {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    edtGtnCode.setText(jsonArray.getJSONObject(0).getString("Tcode"));
                                } else {
                                    AppUtils.showSnakbar(mLinParent, getString(R.string.no_data_available));
                                }
                            } else {
                                AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
                }
            });
            asyncTaskCommon.execute(getString(R.string.get_grn_code_url) + "/" + ((EditGTNActivity) getActivity()).mProjectId + "/gtn/" +
                    SharedPreferenceUtil.getString(AppUtils.YEAR_CODE, "") + "/" + grnType);
        }
    }

    private void setValues() {
        edtGtnCode.setText(mEditGTNModelList.get(0).getGTN_Code());
        edtChallanNo.setText(mEditGTNModelList.get(0).getChallan_No());
        edtRemark.setText(mEditGTNModelList.get(0).getRemark());
        txtGtnDate.setText(AppUtils.formattedDate("MM/dd/yyyy", "dd MMMM yyyy", mEditGTNModelList.get(0).getGTN_date().split(" ")[0]));
        txtChallanDate.setText(AppUtils.formattedDate("MM/dd/yyyy", "dd MMMM yyyy", mEditGTNModelList.get(0).getChallan_Date().split(" ")[0]));
        mRadioGrpGtnType.check(mEditGTNModelList.get(0).getGTN_Type().equalsIgnoreCase("GTN") ? R.id.radioPO : R.id.radioDirect);
    }

    private void initView(View view) {
        edtGtnCode = (EditText) view.findViewById(R.id.edtGtnCode);
        edtChallanNo = (EditText) view.findViewById(R.id.edtChallanNo);
        edtRemark = (EditText) view.findViewById(R.id.edtRemark);;
        txtGtnDate = (TextView) view.findViewById(R.id.txtGtnDate);
        txtChallanDate = (TextView) view.findViewById(R.id.txtChallanDate);;
        mSpnrProject = (Spinner) view.findViewById(R.id.spnrProject);
        mLinParent = (LinearLayout) view.findViewById(R.id.linParent);
        mProductProgressBar = (ProgressBar) view.findViewById(R.id.project_progress);
        mRadioGrpGtnType = (RadioGroup) view.findViewById(R.id.radioGroupGtnType);

        txtGtnDate.setOnClickListener(this);
        txtChallanDate.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(asyncTaskCommon != null && asyncTaskCommon.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTaskCommon.cancel(true);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(AppUtils.isOnline(getContext())){
                getProjectList();
            }else{
                AppUtils.showSnakbar(mLinParent, getString(R.string.error_internet));
            }
        }
    }

    void getProjectList(){
        asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result)  {
                if(result.length() > 0){
                    handleProjects(result);
                }else {
                    AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
                    mProductProgressBar.setVisibility(View.GONE);
                }
            }
        });
        asyncTaskCommon.execute(getString(R.string.get_project_list_gtn_url));
    }

    private void handleProjects(String result) {
        mProductProgressBar.setVisibility(View.GONE);
        try{
            if(result!="") {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    mProjectList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectListModel projectListModel = new ProjectListModel();
                        projectListModel.setName(jsonArray.getJSONObject(i).getString("Name"));
                        projectListModel.setExcise_Opening(jsonArray.getJSONObject(i).getString("Excise_Opening"));
                        projectListModel.setNonExcise_Opening(jsonArray.getJSONObject(i).getString("NonExcise_Opening"));
                        projectListModel.setWC_ID(jsonArray.getJSONObject(i).getString("WC_ID"));
                        mProjectList.add(projectListModel);
                    }
                    setProductSpinnerAdapter();
                } else {
                    AppUtils.showSnakbar(mLinParent, getString(R.string.no_data_available));
                }
            }else {
                AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setProductSpinnerAdapter() {
        if(mSpnrProject!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getProjectName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrProject.setAdapter(adapter);
            mSpnrProject.setSelection(getProjectSeletedId());
        }
    }

    private int getProjectSeletedId() {
        for (int i = 0; i < mProjectList.size(); i++) {
            if(mEditGTNModelList.get(0).getProduct_ID().equals(mProjectList.get(i).getWC_ID()))
                return i;
        }
        return 0;
    }

    public ArrayList<String> getProjectName() {
        ArrayList<String> product = new ArrayList<>();
        for (ProjectListModel model : mProjectList){
            product.add(model.getName());
        }
        return product;
    }

    public boolean isValidate() {
        strGtnCode = edtGtnCode.getText().toString().trim();
        strChallanNo = edtChallanNo.getText().toString().trim();
        strRemark = edtRemark.getText().toString().trim();
        strGtnDate = txtGtnDate.getText().toString();
        strChallanDate = txtChallanDate.getText().toString();

        if (TextUtils.isEmpty(strGtnCode)) {
            edtGtnCode.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strChallanNo)) {
            edtChallanNo.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strRemark)) {
            edtRemark.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strGtnDate)) {
            txtGtnDate.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strChallanDate)) {
            txtChallanDate.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtGtnDate:
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("dateFrom", 1);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.txtChallanDate:
                datePickerDialogFragment = new DatePickerDialogFragment();
                bundle = new Bundle();
                bundle.putInt("dateFrom", 2);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
        }
    }

    public String getAllValues() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GTN_ID", ""+((EditGTNActivity)getActivity()).mEditGTNModelList.get(0).getGTN_ID());
            //jsonObject.put("WC_Id", ""+((EditGTNActivity)getActivity()).mProjectId);
            //jsonObject.put("YearCode", ""+ SharedPreferenceUtil.getString(AppUtils.YEAR_CODE, ""));
            //jsonObject.put("GTN_Code", ""+strGtnCode);
            jsonObject.put("GTN_date", ""+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", strGtnDate));
            //jsonObject.put("GTN_Type", ""+getSelectedGtnType());
            jsonObject.put("Challan_No", ""+strChallanNo);
            jsonObject.put("Challan_Date", ""+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", strChallanDate));
            //jsonObject.put("Project_ID", ""+getProjectId(mSpnrProject.getSelectedItemPosition()));
            jsonObject.put("TotalGTNValue", ""+((EditGTNActivity)getActivity()).mAssesableAmount);
            jsonObject.put("Modify_By", ""+SharedPreferenceUtil.getString(AppUtils.USER_ID, ""));
            jsonObject.put("Remark", ""+strRemark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String getProjectId(int selectedItemPosition) {
        return mProjectList.get(selectedItemPosition).getWC_ID();
    }

    private String getSelectedGtnType() {
        int selectedId = mRadioGrpGtnType.getCheckedRadioButtonId();
        RadioButton radioGrnType = (RadioButton) getView().findViewById(selectedId);
        if(radioGrnType.getText().toString().equals("GTN")){
            return "0";
        }else {
            return "1";
        }
    }

    @SuppressLint("ValidFragment")
    public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private boolean cancelDialog = false;
        private int year;
        private int month;
        private int day;
        private int dateFrom;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month =  c.get(Calendar.MONTH);
            int day =  c.get(Calendar.DAY_OF_MONTH);
            dateFrom = getArguments().getInt("dateFrom");
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void setDatePickerDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            cancelDialog = true;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (!cancelDialog) {
                String mDatePick = AppUtils.formattedDate("yyyy MM dd", "dd MMMM yyyy", ""+year+" "+(month+1)+" "+day);
                switch (dateFrom){
                    case 1:
                        txtGtnDate.setText(mDatePick);
                        break;
                    case 2:
                        txtChallanDate.setText(mDatePick);
                        break;
                }
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDatePickerDate(year, monthOfYear, dayOfMonth);
        }
    }
}
