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
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.model.GRNListModel;
import com.gayatry.model.PaymentReportModel;
import com.gayatry.model.SupplierListModel;
import com.gayatry.report.adapter.PaymentReportRecyclerAdapter;
import com.gayatry.report.adapter.StockReportRecyclerAdapter;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.AsyncTaskPostCommon;
import com.gayatry.utilz.floatingMenu.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 01-May-16.
 */
public class PaymentReportFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private PaymentReportRecyclerAdapter paymentReportRecyclerAdapter;
    private ProgressBar mPartyProgress, mProgress;
    private FloatingActionButton mGenerateReport;
    private RelativeLayout mRelParent;
    private List<HashMap<String, String>> mListParty;
    private List<PaymentReportModel> mListPayment;
    private String PartyId = "PartyId";
    private String PartyName = "PartyName";
    private final String ProjectId = "projectId";
    private Spinner mSpnrPartyName;
    private AsyncTaskCommon asyncTaskCommon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_report, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(AppUtils.isOnline(getContext())){
                if(mListParty == null)
                    getPartyList();
            }else{
                mPartyProgress.setVisibility(View.GONE);
                AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
            }
        }
    }

    private void getPartyList() {
        asyncTaskCommon  = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result)  {
                mPartyProgress.setVisibility(View.GONE);
                if(result.length() > 0){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        if (jsonArray.length() > 0) {
                            mListParty = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put(PartyId, ""+jsonArray.getJSONObject(i).getInt("PartyID"));
                                hashMap.put(PartyName, jsonArray.getJSONObject(i).getString("PartyName"));
                                mListParty.add(hashMap);
                            }
                            setSpinnerAdapter();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    mPartyProgress.setVisibility(View.GONE);
                    AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                }
            }
        });
        asyncTaskCommon.execute(getString(R.string.party_list_url)+"/0/0");
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

    private void setSpinnerAdapter() {
        if(mSpnrPartyName != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getPartyName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnrPartyName.setAdapter(adapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListParty != null && mListParty.size() > 0)
                    getPaymentReportList(getSelectedPartyId(mSpnrPartyName.getSelectedItemPosition()));
            }
        });
    }

    private void getPaymentReportList(String partyId) {
        mProgress.setVisibility(View.VISIBLE);
        AsyncTaskCommon asyncTaskCommon = new AsyncTaskCommon(getActivity(), new AsyncTaskCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result)  {
                mProgress.setVisibility(View.GONE);
                if(result.length() > 0){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        if (jsonArray.length() > 0) {
                            mListPayment = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PaymentReportModel paymentReportModel = new PaymentReportModel();
                                paymentReportModel.setCredit_Period(jsonArray.getJSONObject(i).getString("Credit_Period"));
                                paymentReportModel.setDue_Date(jsonArray.getJSONObject(i).getString("Due_Date"));
                                paymentReportModel.setGRN_Code(jsonArray.getJSONObject(i).getString("GRN_Code"));
                                paymentReportModel.setGRN_date(jsonArray.getJSONObject(i).getString("GRN_date"));
                                paymentReportModel.setOutStanding_Amt(jsonArray.getJSONObject(i).getString("OutStanding_Amt"));
                                paymentReportModel.setPartyName(jsonArray.getJSONObject(i).getString("PartyName"));
                                paymentReportModel.setParty_Inv_Date(jsonArray.getJSONObject(i).getString("Party_Inv_Date"));
                                paymentReportModel.setParty_Inv_No(jsonArray.getJSONObject(i).getString("Party_Inv_No"));
                                paymentReportModel.setPay_Total_Amt(jsonArray.getJSONObject(i).getString("Pay_Total_Amt"));
                                paymentReportModel.setRemain_Days(jsonArray.getJSONObject(i).getString("Remain_Days"));
                                paymentReportModel.setPayment_Status(jsonArray.getJSONObject(i).getString("Payment_Status"));
                                paymentReportModel.setTotalGRNValue(jsonArray.getJSONObject(i).getString("TotalGRNValue"));
                                mListPayment.add(paymentReportModel);
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
        asyncTaskCommon.execute(getString(R.string.payment_report_url)+"/"+partyId+"/"+getArguments().getString(ProjectId));
    }

    private String getSelectedPartyId(int selectedItemPosition) {
        return mListParty.get(selectedItemPosition).get(PartyId);
    }

    private void setRecyclerAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        paymentReportRecyclerAdapter = new PaymentReportRecyclerAdapter(mListPayment, PaymentReportFragment.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(paymentReportRecyclerAdapter);
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mGenerateReport = (FloatingActionButton) view.findViewById(R.id.btnGenerateReport);
        mRelParent = (RelativeLayout) view.findViewById(R.id.relParent);
        mSpnrPartyName = (Spinner) view.findViewById(R.id.spnrPartyName);
        mPartyProgress = (ProgressBar) view.findViewById(R.id.party_progress);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
    }

    public ArrayList<String> getPartyName() {
        ArrayList<String> partyName = new ArrayList<>();
        for (HashMap<String, String> model : mListParty){
            partyName.add(model.get(PartyName));
        }
        return partyName;
    }
}
