package com.gayatry.report.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.model.ProductListModel;
import com.gayatry.model.ProductNameModel;
import com.gayatry.model.StockReportModel;
import com.gayatry.report.adapter.StockReportRecyclerAdapter;
import com.gayatry.rest.JsonParser;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.floatingMenu.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 01-May-16.
 */
public class StockReportFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private StockReportRecyclerAdapter stockReportRecyclerAdapter;
    private FloatingActionButton mGenerateReport;
    private TextView mTxtProjectName, mTxtFromDate, mTxtToDate;
    private final String ProjectId = "projectId";
    private final String ProjectName = "projectName";
    private Spinner mSpnrProductCat, mSpnrProductName;
    private ArrayList<ProductListModel> mProductList;
    private ArrayList<ProductNameModel> mProductNameList;
    private RelativeLayout mRelParent;
    private ProgressBar mProgressProductCat, mProgressProductName, mReport_progress;
    private ArrayList<StockReportModel> mStockReportList;
    private GetProductAsync getProductAsync;
    private AsyncTaskCommon asyncTaskCommon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mTxtProjectName.setText("Project (Site) : "+getArguments().getString(ProjectName));
        mSpnrProductCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getProductNameWs(getSelectedProductCatName(mSpnrProductCat.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String getSelectedProductCatName(int selectedItemPosition) {
        return mProductList.get(selectedItemPosition).getProduct_Category_Name();
    }

    private String getSelectedProductCatId(int selectedItemPosition) {
        return mProductList.get(selectedItemPosition).getProduct_Category_Id();
    }

    private String getSelectedProductId(int selectedItemPosition) {
        return mProductNameList.get(selectedItemPosition).getProduct_Id();
    }

    void getProductNameWs(String name){
        if(AppUtils.isOnline(getActivity())) {
            mProgressProductName.setVisibility(View.VISIBLE);
            asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if (result.length() > 0) {
                        try {
                            if (result != "") {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    mProductNameList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProductNameModel productListModel = new ProductNameModel();
                                        productListModel.setProduct_Id(jsonArray.getJSONObject(i).getString("Product_Id"));
                                        productListModel.setProduct_Name(jsonArray.getJSONObject(i).getString("Product_Name"));
                                        mProductNameList.add(productListModel);
                                    }
                                    setSpinnerProductNameAdapter();
                                }
                            } else {
                                mProgressProductName.setVisibility(View.GONE);
                                AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                        mProgressProductName.setVisibility(View.GONE);
                    }
                }
            });
            asyncTaskCommon.execute(getString(R.string.get_product_master_report_url)+"/0/0/"+name);
        }else{
            mProgressProductName.setVisibility(View.GONE);
        }
    }

    private void setSpinnerProductNameAdapter(){
        mProgressProductName.setVisibility(View.GONE);
        if(mSpnrProductName!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getProductName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrProductName.setAdapter(adapter);
        }
    }

    private ArrayList<String> getProductName() {
        ArrayList<String> productName = new ArrayList<>();
        for (ProductNameModel model : mProductNameList){
            productName.add(model.getProduct_Name());
        }
        return productName;
    }


    void getStockReport(){
        if(AppUtils.isOnline(getActivity())) {
            mReport_progress.setVisibility(View.VISIBLE);
            asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if (result.length() > 0) {
                        try {
                            if (result != "") {
                                mReport_progress.setVisibility(View.GONE);
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    mStockReportList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        StockReportModel stockReportModel = new StockReportModel();
                                        stockReportModel.setProduct_Id(jsonArray.getJSONObject(i).getString("Product_Id"));
                                        stockReportModel.setProduct_Name(jsonArray.getJSONObject(i).getString("Product_Name"));
                                        stockReportModel.setCl_Qty(jsonArray.getJSONObject(i).getString("Cl_Qty"));
                                        stockReportModel.setConsp_Qty(jsonArray.getJSONObject(i).getString("Consp_Qty"));
                                        stockReportModel.setOP_Qty(jsonArray.getJSONObject(i).getString("OP_Qty"));
                                        stockReportModel.setProd_Qty(jsonArray.getJSONObject(i).getString("Prod_Qty"));
                                        stockReportModel.setProduct_Type(jsonArray.getJSONObject(i).getString("Product_Type"));
                                        stockReportModel.setPur_Qty(jsonArray.getJSONObject(i).getString("Pur_Qty"));
                                        mStockReportList.add(stockReportModel);
                                    }
                                    setRecyclerAdapter();
                                }
                            } else {
                                mReport_progress.setVisibility(View.GONE);
                                AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mReport_progress.setVisibility(View.GONE);
                        }
                    } else {
                        mReport_progress.setVisibility(View.GONE);
                        AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                    }
                }
            });
            asyncTaskCommon.execute(getString(R.string.get_stock_report_url)+"/"+getArguments().getString(ProjectId)+"/"+
                    AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", mTxtFromDate.getText().toString())+"/"+AppUtils.formattedDate("dd MMMM yyyy", "yyyy-MM-dd", mTxtToDate.getText().toString())+"/"+
                    getSelectedProductCatId(mSpnrProductCat.getSelectedItemPosition())+"/"+ getSelectedProductId(mSpnrProductName.getSelectedItemPosition()));  //project id/from/to/product/product type
        }
    }


    private void setRecyclerAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        stockReportRecyclerAdapter = new StockReportRecyclerAdapter(mStockReportList, StockReportFragment.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(stockReportRecyclerAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(AppUtils.isOnline(getContext())){
                if(mProductList == null){
                    getProductAsync = new GetProductAsync();
                    getProductAsync.execute();
                }
            }else{
                AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getProductAsync != null && getProductAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getProductAsync.cancel(true);
        }
        if(asyncTaskCommon != null && asyncTaskCommon.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTaskCommon.cancel(true);
        }
    }



    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mGenerateReport = (FloatingActionButton) view.findViewById(R.id.btnGenerateReport);
        mTxtProjectName = (TextView) view.findViewById(R.id.txtProjectName);
        mTxtFromDate = (TextView) view.findViewById(R.id.txtFromDate);
        mTxtToDate = (TextView) view.findViewById(R.id.txtToDate);
        mSpnrProductCat = (Spinner) view.findViewById(R.id.spnrProductCat);
        mSpnrProductName = (Spinner) view.findViewById(R.id.spnrProductName);
        mRelParent = (RelativeLayout) view.findViewById(R.id.relParent);
        mProgressProductCat = (ProgressBar) view.findViewById(R.id.product_cat_progress);
        mProgressProductName = (ProgressBar) view.findViewById(R.id.product_name_progress);
        mReport_progress = (ProgressBar) view.findViewById(R.id.report_progress);

        mGenerateReport.setOnClickListener(this);
        mTxtFromDate.setOnClickListener(this);
        mTxtToDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGenerateReport:
                if(isValidate()){
                   getStockReport();
                }
                break;
            case R.id.txtFromDate:
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("dateFrom",1);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.txtToDate:
                datePickerDialogFragment = new DatePickerDialogFragment();
                bundle = new Bundle();
                bundle.putInt("dateFrom",2);
                datePickerDialogFragment.setArguments(bundle);
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
                break;
        }
    }

    private boolean isValidate() {
        if (!AppUtils.isOnline(getActivity())) {
            AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
            return false;
        }else if (TextUtils.isEmpty(mTxtFromDate.getText().toString())) {
            AppUtils.showSnakbar(mRelParent, "Please select From date");
            return false;
        }else if (TextUtils.isEmpty(mTxtToDate.getText().toString())) {
            AppUtils.showSnakbar(mRelParent, "Please select To date");
            return false;
        }else if (mProductNameList == null && mProductNameList.size() <= 0 && mProductList.size() <= 0 && mProductList == null) {
            AppUtils.showSnakbar(mRelParent, "Please select product");
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
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            dateFrom = getArguments().getInt("dateFrom");
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
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
                String mDatePick = AppUtils.formattedDate("yyyy MM dd", "dd MMMM yyyy", "" + year + " " + (month + 1) + " " + day);
                switch (dateFrom) {
                    case 1:
                        mTxtFromDate.setText(mDatePick);
                        break;
                    case 2:
                        mTxtToDate.setText(mDatePick);
                        break;
                }
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDatePickerDate(year, monthOfYear, dayOfMonth);
        }
    }

    class GetProductAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_product_category_url)+"/0");
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            mProgressProductCat.setVisibility(View.GONE);
            handleResponse(response);
        }
    }

    private void handleResponse(String response) {
        try{
            if(response!="") {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    mProductList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProductListModel productListModel = new ProductListModel();
                        productListModel.setAbbreviation(jsonArray.getJSONObject(i).getString("Abbreviation"));
                        productListModel.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                        productListModel.setIs_Active(jsonArray.getJSONObject(i).getString("Is_Active"));
                        productListModel.setProduct_Category_Id(jsonArray.getJSONObject(i).getString("Product_Category_Id"));
                        productListModel.setProduct_Category_Name(jsonArray.getJSONObject(i).getString("Product_Category_Name"));
                        mProductList.add(productListModel);
                    }
                    setSpinnerAdapter();
                } else {
                    AppUtils.showSnakbar(mRelParent, getString(R.string.no_data_available));
                }
            }else {
                AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
            }
        }catch (Exception e){
            e.printStackTrace();
            AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
        }
    }

    private void setSpinnerAdapter(){
        if(mSpnrProductCat!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getProductNameCat());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrProductCat.setAdapter(adapter);
        }
    }

    private ArrayList<String> getProductNameCat() {
        ArrayList<String> productName = new ArrayList<>();
        for (ProductListModel model : mProductList){
            productName.add(model.getProduct_Category_Name());
        }
        return productName;
    }
}
