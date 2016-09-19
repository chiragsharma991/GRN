package com.gayatry.grn.fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.gayatry.grn.AddGRNActivity;
import com.gayatry.model.PoNoModel;
import com.gayatry.model.SupplierListModel;
import com.gayatry.rest.JsonParser;
import com.gayatry.storage.SharedPreferenceUtil;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.AsyncTaskPostCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 06-Apr-16.
 */
public class GRNGeneralFragment extends Fragment implements View.OnClickListener{

    private TextView txtGrnDate, txtChallanDate, txtInvoiceDate, txtLRDate, txtPoDate;
    private EditText edtGrnCode, edtChallanNo, edtInvoiceNo, edtCrPeriod, edtLRNo, edtVehicleNo, edtRemark;
    private List<SupplierListModel> mSupplierList;
    private List<PoNoModel> mPoNoList;
    private LinearLayout mLinParent;
    private Spinner mSpnrSupplier, mSpnrPoNo;
    private ProgressBar mProgressSupplier, mProgressPoNo;
    private String strGrnCode, strChallanNo, strInvoiceNo, strCrPeriod, strLRNo, strVehicleNo, strRemark,
            strGrnDate, strChallanDate, strInvoiceDate, strLRDate, strPoDate;
    private RadioGroup mRadioGrpGrnType;
    private GetPoNoAsync getPoNoAsync;
    private GetSupplierAsync getSupplierAsync;
    private AsyncTaskCommon asyncTaskCommon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_general, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getGeneratedGrnCode("PO");
        mRadioGrpGrnType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioGrnType = (RadioButton) getView().findViewById(checkedId);
                getGeneratedGrnCode(radioGrnType.getText().toString());
            }
        });
        mSpnrSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(AppUtils.isOnline(getContext())){
                    getPoNoAsync = new GetPoNoAsync();
                    getPoNoAsync.execute(getSelectedSupplierID(position));
                }else{
                    AppUtils.showSnakbar(mLinParent, getString(R.string.error_internet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void getGeneratedGrnCode(String grnType){
        if(AppUtils.isOnline(getActivity())) {
            asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if (result.length() > 0) {
                        try {
                            if (result != "") {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    edtGrnCode.setText(jsonArray.getJSONObject(0).getString("Tcode"));
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
            asyncTaskCommon.execute(getString(R.string.get_grn_code_url) + "/" + ((AddGRNActivity) getActivity()).mProjectId + "/grn/" +
                    SharedPreferenceUtil.getString(AppUtils.YEAR_CODE, "") + "/" + grnType);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getSupplierAsync != null && getSupplierAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getSupplierAsync.cancel(true);
        }
        if(getPoNoAsync != null && getPoNoAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getPoNoAsync.cancel(true);
        }
        if(asyncTaskCommon != null && asyncTaskCommon.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTaskCommon.cancel(true);
        }
    }

    private String getSelectedSupplierID(int position) {
        return mSupplierList.get(position).getPartyID();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(AppUtils.isOnline(getContext())){
                if(mSupplierList == null) {
                    getSupplierAsync = new GetSupplierAsync();
                    getSupplierAsync.execute();
                }
            }else{
                AppUtils.showSnakbar(mLinParent, getString(R.string.error_internet));
            }
        }
    }

    private void initView(View view) {
        txtGrnDate = (TextView) view.findViewById(R.id.txtGrnDate);
        txtChallanDate = (TextView) view.findViewById(R.id.txtChallanDate);
        txtInvoiceDate = (TextView) view.findViewById(R.id.txtInvoiceDate);
        txtLRDate = (TextView) view.findViewById(R.id.txtLRDate);
        txtPoDate = (TextView) view.findViewById(R.id.txtPoDate);
        mLinParent = (LinearLayout) view.findViewById(R.id.linParent);
        mSpnrSupplier = (Spinner) view.findViewById(R.id.spnrSupplier);
        mSpnrPoNo = (Spinner) view.findViewById(R.id.spnrPoNo);
        mProgressPoNo = (ProgressBar) view.findViewById(R.id.pono_progress);
        mProgressSupplier = (ProgressBar) view.findViewById(R.id.supplier_progress);
        edtGrnCode = (EditText) view.findViewById(R.id.edtGrnCode);
        edtChallanNo = (EditText) view.findViewById(R.id.edtChallanNo);
        edtInvoiceNo = (EditText) view.findViewById(R.id.edtInvoiceNo);
        edtCrPeriod = (EditText) view.findViewById(R.id.edtCrPeriod);
        edtLRNo = (EditText) view.findViewById(R.id.edtLRNo);
        edtVehicleNo = (EditText) view.findViewById(R.id.edtVehicleNo);
        edtRemark = (EditText) view.findViewById(R.id.edtRemark);
        mRadioGrpGrnType = (RadioGroup) view.findViewById(R.id.radioGroupGrnType);

        txtGrnDate.setOnClickListener(this);
        txtChallanDate.setOnClickListener(this);
        txtInvoiceDate.setOnClickListener(this);
        txtLRDate.setOnClickListener(this);
        txtPoDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtGrnDate:
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("dateFrom",1);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.txtChallanDate:
                datePickerDialogFragment = new DatePickerDialogFragment();
                bundle = new Bundle();
                bundle.putInt("dateFrom",2);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.txtInvoiceDate:
                datePickerDialogFragment = new DatePickerDialogFragment();
                bundle = new Bundle();
                bundle.putInt("dateFrom",3);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.txtLRDate:
                datePickerDialogFragment = new DatePickerDialogFragment();
                bundle = new Bundle();
                bundle.putInt("dateFrom",4);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.txtPoDate:
                datePickerDialogFragment = new DatePickerDialogFragment();
                bundle = new Bundle();
                bundle.putInt("dateFrom",5);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
        }
    }

    public String getAllValues() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GRN_ID", "0");
            jsonObject.put("WC_Id", ""+((AddGRNActivity)getActivity()).mProjectId);
            jsonObject.put("YearCode", ""+ SharedPreferenceUtil.getString(AppUtils.YEAR_CODE, ""));
            jsonObject.put("GRN_Code", ""+strGrnCode);
            jsonObject.put("GRN_date", ""+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", strGrnDate));
            jsonObject.put("GRN_Type", ""+ getSelectedGrnType());//radio btn
            jsonObject.put("Order_ID", "0");
            jsonObject.put("Party_ID", ""+getSelectedSupplierID(mSpnrSupplier.getSelectedItemPosition()));
            jsonObject.put("Challan_No", ""+strChallanNo);
            jsonObject.put("Challan_Date", ""+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", strChallanDate));
            jsonObject.put("Supp_Inv_No", ""+strInvoiceNo);
            jsonObject.put("Supp_Inv_Date", ""+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", strInvoiceDate));
            jsonObject.put("Credit_Period", ""+strCrPeriod);
            jsonObject.put("LR_No", ""+strLRNo);
            jsonObject.put("LR_Date", ""+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", strLRDate));
            jsonObject.put("Transporter", "0");
            jsonObject.put("VehicleNo", ""+strVehicleNo);
            jsonObject.put("RegisterTypeID", "0");
            jsonObject.put("AssessableAmount", ""+((AddGRNActivity)getActivity()).mAssessableAmount);
            jsonObject.put("TotalTaxValue", "0");
            jsonObject.put("TotalGRNValue", "0");
            jsonObject.put("Inspected_By", "1");
            jsonObject.put("IsAccepted", "1");
            jsonObject.put("Created_By", ""+SharedPreferenceUtil.getString(AppUtils.USER_ID, ""));
            jsonObject.put("Create_date", ""+AppUtils.getCurruntDate());
            jsonObject.put("Modify_By", "0");
            jsonObject.put("Modify_date", "1990-01-01");
            jsonObject.put("IsCancelled", "0");
            jsonObject.put("Remark", ""+strRemark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String getSelectedGrnType() {
        int selectedId = mRadioGrpGrnType.getCheckedRadioButtonId();
        RadioButton radioGrnType = (RadioButton) getView().findViewById(selectedId);
        return radioGrnType.getText().toString();
       /* if(radioGrnType.getText().toString().equals("PO")){
            return "0";
        }else {
            return "1";
        }*/
    }


    public boolean isValidate() {
        strGrnCode = edtGrnCode.getText().toString().trim();
        strChallanNo = edtChallanNo.getText().toString().trim();
        strInvoiceNo = edtInvoiceNo.getText().toString().trim();
        strCrPeriod = edtCrPeriod.getText().toString().trim();
        strLRNo = edtLRNo.getText().toString().trim();
        strVehicleNo = edtVehicleNo.getText().toString().trim();
        strRemark = edtRemark.getText().toString().trim();
        strGrnDate = txtGrnDate.getText().toString();
        strChallanDate = txtChallanDate.getText().toString();
        strInvoiceDate = txtInvoiceDate.getText().toString();
        strLRDate = txtLRDate.getText().toString();
        strPoDate = txtPoDate.getText().toString();

        if (TextUtils.isEmpty(strGrnCode)) {
            edtGrnCode.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strChallanNo)) {
            edtChallanNo.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strInvoiceNo)) {
            edtInvoiceNo.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strCrPeriod)) {
            edtCrPeriod.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strLRNo)) {
            edtLRNo.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strVehicleNo)) {
            edtVehicleNo.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strRemark)) {
            edtRemark.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strGrnDate)) {
            txtGrnDate.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strChallanDate)) {
            txtChallanDate.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strInvoiceDate)) {
            txtInvoiceDate.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strLRDate)) {
            txtLRDate.setError(getString(R.string.error_field_required));
            return false;
        }else if (TextUtils.isEmpty(strPoDate)) {
            txtPoDate.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
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
                        txtGrnDate.setText(mDatePick);
                        break;
                    case 2:
                        txtChallanDate.setText(mDatePick);
                        break;
                    case 3:
                        txtInvoiceDate.setText(mDatePick);
                        break;
                    case 4:
                        txtLRDate.setText(mDatePick);
                        break;
                    case 5:
                        txtPoDate.setText(mDatePick);
                        break;
                }
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDatePickerDate(year, monthOfYear, dayOfMonth);
        }
    }

    class GetSupplierAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_supplier_list_url)+"/1");
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            mProgressSupplier.setVisibility(View.GONE);
            handleSupplierResponse(response);
        }
    }

    private void handleSupplierResponse(String response) {
        try{
            if(response!="") {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    mSupplierList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SupplierListModel supplierListModel = new SupplierListModel();
                        supplierListModel.setParty_Type(jsonArray.getJSONObject(i).getString("Party_Type"));
                        supplierListModel.setPartyID(jsonArray.getJSONObject(i).getString("PartyID"));
                        supplierListModel.setPartyType_Id(jsonArray.getJSONObject(i).getString("PartyType_Id"));
                        supplierListModel.setPartyName(jsonArray.getJSONObject(i).getString("PartyName"));
                        mSupplierList.add(supplierListModel);
                    }
                    setSupplierSpinnerAdapter();
                } else {
                    AppUtils.showSnakbar(mLinParent, getString(R.string.no_data_available));
                }
            }else {
                AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
            }
        }catch (Exception e){
            e.printStackTrace();
           /* if(mLinParent != null)
                AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));*/
        }
    }

    private void setSupplierSpinnerAdapter() {
        if(mSpnrSupplier!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getSupplierName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrSupplier.setAdapter(adapter);
        }
    }

    public ArrayList<String> getSupplierName() {
        ArrayList<String> supplierName = new ArrayList<>();
        for (SupplierListModel model : mSupplierList){
            supplierName.add(model.getPartyName());
        }
        return supplierName;
    }


    class GetPoNoAsync extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressPoNo.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_PO_list_url)+"/"+params[0]+"/"+((AddGRNActivity)getActivity()).mProjectId+"/P/A");
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            mProgressPoNo.setVisibility(View.GONE);
            try{
                if(response!="") {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        mPoNoList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PoNoModel poNoModel = new PoNoModel();
                            poNoModel.setPCode(jsonArray.getJSONObject(i).getString("PCode"));
                            poNoModel.setPID(jsonArray.getJSONObject(i).getString("PID"));
                            mPoNoList.add(poNoModel);
                        }
                        setPoNoSpinnerAdapter();
                    } else {
                        if(mPoNoList != null) {
                            mPoNoList.clear();
                            setPoNoSpinnerAdapter();
                        }
                        AppUtils.showSnakbar(mLinParent, getString(R.string.no_data_available));
                    }
                }else {
                    AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));
                }
            }catch (Exception e){
                e.printStackTrace();
                /*if(mLinParent != null)
                    AppUtils.showSnakbar(mLinParent, getString(R.string.error_server));*/
            }
        }
    }

    private void setPoNoSpinnerAdapter() {
        if(mSpnrPoNo!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getPoCode());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrPoNo.setAdapter(adapter);
        }
    }

    public ArrayList<String> getPoCode() {
        ArrayList<String> poCode = new ArrayList<>();
        for (PoNoModel model : mPoNoList){
            poCode.add(model.getPCode());
        }
        return poCode;
    }


}
