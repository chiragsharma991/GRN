package com.gayatry.grn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.grn.adapter.GRNRecyclerAdapter;
import com.gayatry.base.BaseActivity;
import com.gayatry.model.EditGRNModel;
import com.gayatry.model.GRNListModel;
import com.gayatry.model.StockReportModel;
import com.gayatry.rest.JsonParser;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.floatingMenu.AddFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GRNListActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddFloatingActionButton mAddBtn;
    private ProgressBar mProgressBar;
    private LinearLayout mLinInternet;
    private RelativeLayout mRelParent;
    private TextView mTxtErrorMsg;
    private final String ProjectId = "projectId";
    private ImageView mImgRetry;
    private final String GRN_MODEL = "grn_model";
    private final String GRN_LIST_MODEL = "grn_list_model";
    private GetGRNListAsync getGRNListAsync;
    private ArrayList<GRNListModel> mGrnList;
    private AsyncTaskCommon asyncTaskCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn_list);
        initView();
        getGRNList();
    }

    private void getGRNList() {
        if(isOnline(this)){
            getGRNListAsync = new GetGRNListAsync();
            getGRNListAsync.execute();
        }
        else {
            visibleErrorMsg(getString(R.string.error_internet));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getGRNListAsync != null && getGRNListAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getGRNListAsync.cancel(true);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgRetry:
                hideErrorMsg();
                getGRNList();
                break;
            case R.id.btnAdd:
                startActivityForResult(new Intent(GRNListActivity.this, AddGRNActivity.class).putExtra(ProjectId, getIntent().getExtras().getString(ProjectId)), AppUtils.REQUEST_CODE_ADD_GRN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == AppUtils.REQUEST_CODE_ADD_GRN) {
                getGRNList();
            }
        }
    }

    public void openAlertDialog(final GRNListModel grnListModel, final int position) {
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure want to Delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               deleteGrnList(grnListModel, position);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    void deleteGrnList(GRNListModel grnListModel, final int position){
        if(AppUtils.isOnline(this)) {
            AppUtils.showProgressDialog(GRNListActivity.this);
            asyncTaskCommon = new AsyncTaskCommon(this, new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    AppUtils.stopProgressDialog();
                    mGrnList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    if(mGrnList.size() == 0){
                        mLinInternet.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mTxtErrorMsg.setText(getString(R.string.no_data_available));
                    }
                   /* if (result.length() > 0) {
                        try {
                            if (result != "") {

                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {

                                }
                            } else {
                                AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));*/
                }
            });
            asyncTaskCommon.execute(getString(R.string.delete_grn_master)+"/"+grnListModel.getGRN_ID());
        }else{
            AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
        }
    }

    public void onEditClick(GRNListModel grnListModel) {
        getAllEditGrnData(grnListModel);
    }

    void getAllEditGrnData(final GRNListModel grnListModel){
        if(AppUtils.isOnline(this)) {
            AppUtils.showProgressDialog(this);
            asyncTaskCommon = new AsyncTaskCommon(this, new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    AppUtils.stopProgressDialog();
                    if (result.length() > 0) {
                        try {
                            if (result != "") {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("lst_GRN_Report1_Class");
                                ArrayList<EditGRNModel> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    EditGRNModel editGRNModel = new EditGRNModel();
                                    editGRNModel.setAmount(object.getString("Amount"));
                                    editGRNModel.setProduct_Name(object.getString("Product_Name"));
                                    editGRNModel.setProduct_Category_Name(object.getString("Product_Category_Name"));
                                    editGRNModel.setCredit_Period(object.getString("Credit_Period"));
                                    editGRNModel.setAssessableAmount(object.getString("AssessableAmount"));
                                    editGRNModel.setChallan_Date(object.getString("Challan_Date"));
                                    editGRNModel.setChallan_No(object.getString("Challan_No"));
                                    editGRNModel.setGRN_Code(object.getString("GRN_Code"));
                                    editGRNModel.setGRN_date(object.getString("GRN_date"));
                                    editGRNModel.setGRN_ID(object.getString("GRN_ID"));
                                    editGRNModel.setGRN_No(object.getString("GRN_No"));
                                    editGRNModel.setGRN_Type(object.getString("GRN_Type"));
                                    editGRNModel.setLR_Date(object.getString("LR_Date"));
                                    editGRNModel.setLR_No(object.getString("LR_No"));
                                    editGRNModel.setParty_ID(object.getString("Party_ID"));
                                    editGRNModel.setPartyName(object.getString("PartyName"));
                                    editGRNModel.setProduct_ID(object.getString("Product_ID"));
                                    editGRNModel.setQty(object.getString("Qty"));
                                    editGRNModel.setRate(object.getString("Rate"));
                                    editGRNModel.setRemark(object.getString("Remark"));
                                    editGRNModel.setVehicleNo(object.getString("VehicleNo"));
                                    editGRNModel.setTotalGRNValue(object.getString("TotalGRNValue"));
                                    editGRNModel.setProduct_Category_Id(object.getString("Product_Category_Id"));
                                    editGRNModel.setSupp_Inv_No(object.getString("Supp_Inv_No"));
                                    editGRNModel.setUnit(object.getString("Unit"));
                                    editGRNModel.setUnit_Id(object.getString("Unit_Id"));
                                    editGRNModel.setSupp_Inv_Date(object.getString("Supp_Inv_Date"));
                                    list.add(editGRNModel);
                                }
                                if(list.size() > 0){
                                    Intent intent = new Intent(GRNListActivity.this, EditGRNActivity.class);
                                    intent.putExtra(ProjectId, getIntent().getExtras().getString(ProjectId));
                                    intent.putExtra(GRN_MODEL, grnListModel);
                                    intent.putParcelableArrayListExtra(GRN_LIST_MODEL, list);
                                    startActivity(intent);
                                }else{
                                    AppUtils.showSnakbar(mRelParent, getString(R.string.no_data_available));
                                }
                            } else {
                                AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                }
            });
            asyncTaskCommon.execute(getString(R.string.edit_grn_url)+"/"+grnListModel.getGRN_ID());
        }
    }

    private class GetGRNListAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_grn_list_url)+"/0/"+getIntent().getExtras().getString(ProjectId));
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            showProgress(false);
            handleResponse(response);
        }
    }

    private void handleResponse(String response) {
        try{
            if(response!="") {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    hideErrorMsg();
                    mGrnList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GRNListModel grnListModel = new GRNListModel();
                        grnListModel.setAssessableAmount(jsonArray.getJSONObject(i).getString("AssessableAmount"));
                        grnListModel.setChallan_Date(jsonArray.getJSONObject(i).getString("Challan_Date"));
                        grnListModel.setChallan_No(jsonArray.getJSONObject(i).getString("Challan_No"));
                        grnListModel.setGRN_Code(jsonArray.getJSONObject(i).getString("GRN_Code"));
                        grnListModel.setGRN_date(convertTimeStampToDate(jsonArray.getJSONObject(i).getString("GRN_date")));
                        grnListModel.setGRN_ID(jsonArray.getJSONObject(i).getString("GRN_ID"));
                        grnListModel.setGRN_No(jsonArray.getJSONObject(i).getString("GRN_No"));
                        grnListModel.setGRN_Type(jsonArray.getJSONObject(i).getString("GRN_Type"));
                        grnListModel.setIsCancelled(jsonArray.getJSONObject(i).getString("IsCancelled"));
                        grnListModel.setLR_Date(jsonArray.getJSONObject(i).getString("LR_Date"));
                        grnListModel.setLR_No(jsonArray.getJSONObject(i).getString("LR_No"));
                        grnListModel.setOrder_Code(jsonArray.getJSONObject(i).getString("Order_Code"));
                        grnListModel.setOrder_ID(jsonArray.getJSONObject(i).getString("Order_ID"));
                        grnListModel.setParty_ID(jsonArray.getJSONObject(i).getString("Party_ID"));
                        grnListModel.setPartyName(jsonArray.getJSONObject(i).getString("PartyName"));
                        mGrnList.add(grnListModel);
                    }
                    setAdapter(mGrnList);
                } else {
                    visibleErrorMsg(getString(R.string.no_data_available));
                }
            }else {
                visibleErrorMsg(getString(R.string.error_server));
            }
        }catch (Exception e){
            e.printStackTrace();
            visibleErrorMsg(getString(R.string.error_server));
        }
    }

    private String convertTimeStampToDate(String time){
        try {
            String timestamp = time.split("\\(")[1].split("-")[0];
            Date createdOn = new Date(Long.parseLong(timestamp));
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            return sdf.format(createdOn);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private void hideErrorMsg() {
        mLinInternet.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void visibleErrorMsg(String msg) {
        mLinInternet.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mTxtErrorMsg.setText(msg);
        AppUtils.showSnakbar(mRelParent, "" + msg);
    }

    private void setAdapter(ArrayList<GRNListModel> arrayList) {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GRNRecyclerAdapter(arrayList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        initToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAddBtn = (AddFloatingActionButton) findViewById(R.id.btnAdd);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mLinInternet = (LinearLayout) findViewById(R.id.linInternet);
        mRelParent = (RelativeLayout) findViewById(R.id.relParent);
        mImgRetry = (ImageView) findViewById(R.id.imgRetry);
        mTxtErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);
        mImgRetry.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRecyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
