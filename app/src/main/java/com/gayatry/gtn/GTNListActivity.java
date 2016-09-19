package com.gayatry.gtn;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.base.BaseActivity;
import com.gayatry.grn.EditGRNActivity;
import com.gayatry.gtn.adapter.GTNRecyclerAdapter;
import com.gayatry.model.EditGRNModel;
import com.gayatry.model.EditGTNModel;
import com.gayatry.model.GRNListModel;
import com.gayatry.model.GTNListModel;
import com.gayatry.rest.JsonParser;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.Callbaks;
import com.gayatry.utilz.floatingMenu.AddFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 23-Apr-16.
 */
public class GTNListActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddFloatingActionButton mAddBtn;
    private ProgressBar mProgressBar;
    private LinearLayout mLinInternet;
    private RelativeLayout mRelParent;
    private final String ProjectId = "projectId";
    private TextView mTxtErrorMsg;
    private ImageView mImgRetry;
    private GetGTNListAsync getGTNListAsync;
    private String GTN_MODEL="gtn_model";
    private ArrayList<GTNListModel> mGtnList;
    private AsyncTaskCommon asyncTaskCommon;
    private String GTN_LIST_MODEL = "gtn_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gtn_list);
        initView();
        getGTNList();
    }

    private void getGTNList() {
        if(isOnline(this)){
            getGTNListAsync = new GetGTNListAsync();
            getGTNListAsync.execute();
        }
        else {
            visibleErrorMsg(getString(R.string.error_internet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgRetry:
                hideErrorMsg();
                getGTNList();
                break;
            case R.id.btnAdd:
                startActivityForResult(new Intent(GTNListActivity.this, AddGTNActivity.class).putExtra(ProjectId, getIntent().getExtras().getString(ProjectId)), AppUtils.REQUEST_CODE_ADD_GRN);
                //startActivity(new Intent(GTNListActivity.this, AddGTNActivity.class));
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getGTNListAsync != null && getGTNListAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getGTNListAsync.cancel(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == AppUtils.REQUEST_CODE_ADD_GRN) {
                getGTNList();
            }
        }
    }

    public void openAlertDialog(final GTNListModel grnListModel, final int position) {
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

    void deleteGrnList(GTNListModel grnListModel, final int position){
        if(AppUtils.isOnline(this)) {
            AppUtils.showProgressDialog(GTNListActivity.this);
            asyncTaskCommon = new AsyncTaskCommon(this, new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    AppUtils.stopProgressDialog();
                    mGtnList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    if(mGtnList.size() == 0){
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
            asyncTaskCommon.execute(getString(R.string.delete_gtn_master)+"/"+grnListModel.getGTN_ID());
        }else{
            AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
        }
    }

    public void onEditClick(GTNListModel grnListModel) {
        getAllEditGtnData(grnListModel);
    }

    void getAllEditGtnData(final GTNListModel gtnListModel){
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
                                JSONArray jsonArray = jsonObject.getJSONArray("lst_GTN_Report1_Class");
                                ArrayList<EditGTNModel> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    EditGTNModel editGTNModel = new EditGTNModel();
                                    editGTNModel.setAmount(object.getString("Amount"));
                                    editGTNModel.setProduct_Name(object.getString("Product_Name"));
                                    editGTNModel.setProduct_Category_Name(object.getString("Product_Category_Name"));
                                    editGTNModel.setProduct_Category_ID(object.getString("Product_Category_ID"));
                                    editGTNModel.setChallan_No(object.getString("Challan_No"));
                                    editGTNModel.setProduct_ID(object.getString("Product_ID"));
                                    editGTNModel.setChallan_Date(object.getString("Challan_Date"));
                                    editGTNModel.setRate(object.getString("Rate"));
                                    editGTNModel.setUnit_Id(object.getString("Unit_Id"));
                                    editGTNModel.setUnit(object.getString("Unit"));
                                    editGTNModel.setGTN_Code(object.getString("GTN_Code"));
                                    editGTNModel.setGTN_date(object.getString("GTN_date"));
                                    editGTNModel.setGTN_ID(object.getString("GTN_ID"));
                                    editGTNModel.setGTN_Type(object.getString("GTN_Type"));
                                    editGTNModel.setIsCancelled(object.getString("IsCancelled"));
                                    editGTNModel.setNet_Qty(object.getString("Net_Qty"));
                                    editGTNModel.setQty(object.getString("Qty"));
                                    editGTNModel.setRemark(object.getString("Remark"));
                                    editGTNModel.setTotalGTNValue(object.getString("TotalGTNValue"));
                                    editGTNModel.setWC_Id(object.getString("WC_Id"));
                                    editGTNModel.setWC_Name(object.getString("WC_Name"));
                                    editGTNModel.setYearCode(object.getString("YearCode"));
                                    list.add(editGTNModel);
                                }
                                if(list.size() > 0){
                                    Intent intent = new Intent(GTNListActivity.this, EditGTNActivity.class);
                                    intent.putExtra(GTN_MODEL, gtnListModel);
                                    intent.putExtra(ProjectId, getIntent().getExtras().getString(ProjectId));
                                    intent.putParcelableArrayListExtra(GTN_LIST_MODEL, list);
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
            asyncTaskCommon.execute(getString(R.string.edit_gtn_url)+"/"+gtnListModel.getGTN_ID());
        }
    }


    private class GetGTNListAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_gtn_list_url)+"/0/"+ getIntent().getExtras().getString(ProjectId));
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
                    mGtnList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GTNListModel gtnListModel = new GTNListModel();
                        gtnListModel.setChallan_Date(jsonArray.getJSONObject(i).getString("Challan_Date"));
                        gtnListModel.setChallan_No(jsonArray.getJSONObject(i).getString("Challan_No"));
                        gtnListModel.setGTN_Code(jsonArray.getJSONObject(i).getString("GTN_Code"));
                        gtnListModel.setGTN_date(jsonArray.getJSONObject(i).getString("GTN_date"));
                        gtnListModel.setGTN_ID(jsonArray.getJSONObject(i).getString("GTN_ID"));
                        gtnListModel.setGTN_No(jsonArray.getJSONObject(i).getString("GTN_No"));
                        gtnListModel.setIsCancelled(jsonArray.getJSONObject(i).getString("IsCancelled"));
                        gtnListModel.setProject_ID(jsonArray.getJSONObject(i).getString("Project_ID"));
                        gtnListModel.setProjectName(jsonArray.getJSONObject(i).getString("ProjectName"));
                        gtnListModel.setYearCode(jsonArray.getJSONObject(i).getString("YearCode"));
                        gtnListModel.setWC_Id(jsonArray.getJSONObject(i).getString("WC_Id"));
                        gtnListModel.setWC_Name(jsonArray.getJSONObject(i).getString("WC_Name"));
                        gtnListModel.setTotalGTNValue(jsonArray.getJSONObject(i).getString("TotalGTNValue"));
                        mGtnList.add(gtnListModel);
                    }
                    setAdapter(mGtnList);
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

    private void setAdapter(ArrayList<GTNListModel> arrayList) {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GTNRecyclerAdapter(arrayList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        initToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAddBtn = (AddFloatingActionButton) findViewById(R.id.btnAdd);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mLinInternet = (LinearLayout) findViewById(R.id.linInternet);
        mRelParent = (RelativeLayout) findViewById(R.id.relParent);
        mTxtErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);
        mImgRetry = (ImageView) findViewById(R.id.imgRetry);
        mImgRetry.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
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
