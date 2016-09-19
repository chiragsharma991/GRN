package com.gayatry.report.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.gayatry.R;
import com.gayatry.model.AgingReportModel;
import com.gayatry.model.PaymentReportModel;
import com.gayatry.model.ProductListModel;
import com.gayatry.report.adapter.AgingReportRecyclerAdapter;
import com.gayatry.report.adapter.PaymentReportRecyclerAdapter;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.floatingMenu.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 01-May-16.
 */
public class AgingReportFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private AgingReportRecyclerAdapter agingReportRecyclerAdapter;
    private FloatingActionButton mGenerateReport;
    private Spinner mSpnrProduct;
    private ProgressBar mProgress, mProductProgress;
    private String PartyId = "PartyId";
    private String PartyName = "PartyName";
    private final String ProjectId = "projectId";
    private ArrayList<ProductListModel> mProductList;
    private RelativeLayout mRelParent;
    private ArrayList<AgingReportModel> mListAging;
    private AsyncTaskCommon asyncTaskCommon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aging_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProductList != null && mProductList.size() > 0){
                    getAgingReportList(getSelectedPartyId(mSpnrProduct.getSelectedItemPosition()));
                }
            }
        });
    }

    private String getSelectedPartyId(int selectedItemPosition) {
        return mProductList.get(selectedItemPosition).getProduct_Category_Id();
    }

    private void setRecyclerAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        agingReportRecyclerAdapter = new AgingReportRecyclerAdapter(mListAging, AgingReportFragment.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(agingReportRecyclerAdapter);
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mGenerateReport = (FloatingActionButton) view.findViewById(R.id.btnGenerateReport);
        mSpnrProduct = (Spinner) view.findViewById(R.id.spnrProductName);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mProductProgress = (ProgressBar) view.findViewById(R.id.product_progress);
        mRelParent = (RelativeLayout) view.findViewById(R.id.relParent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(AppUtils.isOnline(getContext())){
                if(mProductList == null)
                    getProductList();
            }else{
                AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(asyncTaskCommon != null && asyncTaskCommon.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTaskCommon.cancel(true);
        }
    }

    private void getProductList() {
         asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result)  {
                mProductProgress.setVisibility(View.GONE);
                if (result.length() > 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
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
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                    }
                }else {
                    mProductProgress.setVisibility(View.GONE);
                    AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                }
            }
        });
        asyncTaskCommon.execute(getString(R.string.get_product_category_url)+"/0");

    }

    private void setSpinnerAdapter() {
        if(mSpnrProduct!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getProductName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrProduct.setAdapter(adapter);
        }
    }

    private ArrayList<String> getProductName() {
        ArrayList<String> productName = new ArrayList<>();
        for (ProductListModel model : mProductList){
            productName.add(model.getProduct_Category_Name());
        }
        return productName;
    }

    private void getAgingReportList(String partyId) {
        mProgress.setVisibility(View.VISIBLE);
        AsyncTaskCommon asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result)  {
                mProgress.setVisibility(View.GONE);
                if(result.length() > 0){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        if (jsonArray.length() > 0) {
                            mListAging = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                AgingReportModel agingReportModel = new AgingReportModel();
                                agingReportModel.setDays180_Qty(jsonArray.getJSONObject(i).getString("Days180_Qty"));
                                agingReportModel.setDays3060_Qty(jsonArray.getJSONObject(i).getString("Days3060_Qty"));
                                agingReportModel.setDays6090_Qty(jsonArray.getJSONObject(i).getString("Days6090_Qty"));
                                agingReportModel.setDays90150_Qty(jsonArray.getJSONObject(i).getString("Days90150_Qty"));
                                agingReportModel.setProduct_Category_Name(jsonArray.getJSONObject(i).getString("Product_Category_Name"));
                                agingReportModel.setProduct_Id(jsonArray.getJSONObject(i).getString("Product_Id"));
                                agingReportModel.setProduct_Name(jsonArray.getJSONObject(i).getString("Product_Name"));
                                agingReportModel.setDays150180_Qty(jsonArray.getJSONObject(i).getString("Days150180_Qty"));
                                agingReportModel.setDays30_Qty(jsonArray.getJSONObject(i).getString("days30_Qty"));
                                mListAging.add(agingReportModel);
                            }
                            setRecyclerAdapter();
                        }else {
                            mProgress.setVisibility(View.GONE);
                            AppUtils.showSnakbar(mRelParent, getString(R.string.error_data_unavailable));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    mProgress.setVisibility(View.GONE);
                    AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                }
            }
        });
        asyncTaskCommon.execute(getString(R.string.aging_report_url)+"/"+partyId+"/"+getArguments().getString(ProjectId));
    }
}
