package com.gayatry.gtn.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.grn.AddGRNActivity;
import com.gayatry.gtn.AddGTNActivity;
import com.gayatry.model.ProductDetailModel;
import com.gayatry.model.ProductListModel;
import com.gayatry.model.UnitModel;
import com.gayatry.rest.JsonParser;
import com.gayatry.storage.SharedPreferenceUtil;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.floatingMenu.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 27-Apr-16.
 */
public class GTNProductFragment extends Fragment {

    private LinearLayout mLinProductContainer;
    private RelativeLayout mRelParent;
    private Spinner mSpnrProduct;
    private ArrayList<ProductListModel> mProductList;
    private ArrayList<ProductDetailModel> mProductDetailList;
    private LayoutInflater layoutInflater;
    private ArrayList<UnitModel> mUnitList;
    private EditText mAssessmentValue;
    private ProgressBar mProductProgress;
    private FloatingActionButton mBtnSubmit;
    private GetUnitListAsync getUnitListAsync;
    private GetProductDetailAsync getProductDetailAsync;
    private GetProductAsync getProductAsync;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if(AppUtils.isOnline(getContext())){
            getUnitListAsync = new GetUnitListAsync();
            getUnitListAsync.execute();
        }else{
            AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
        }
        mSpnrProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(AppUtils.isOnline(getContext())){
                    getProductDetailAsync = new GetProductDetailAsync();
                    getProductDetailAsync.execute(getSelectedProductID(position));
                }else{
                    AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddGTNActivity)getActivity()).onSaveGtnClick();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getUnitListAsync != null && getUnitListAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getUnitListAsync.cancel(true);
        }
        if(getProductDetailAsync != null && getProductDetailAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getProductDetailAsync.cancel(true);
        }
        if(getProductAsync != null && getProductAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getProductAsync.cancel(true);
        }
    }

    private String getSelectedProductID(int position) {
        return mProductList.get(position).getProduct_Category_Id();
    }

    private String getSelectedUnitName(int position) {
        return mUnitList.get(position).getUnit();
    }

    private void initView(View view) {
        mLinProductContainer = (LinearLayout) view.findViewById(R.id.linProductContainer);
        mSpnrProduct = (Spinner) view.findViewById(R.id.spnrProduct);
        mRelParent = (RelativeLayout) view.findViewById(R.id.relParent);
        mAssessmentValue = (EditText) view.findViewById(R.id.edtAssessable);
        mProductProgress = (ProgressBar) view.findViewById(R.id.product_progress);
        mBtnSubmit = (FloatingActionButton) view.findViewById(R.id.btnSubmit);
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


    private void setProductDetailList() {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLinProductContainer.removeAllViews();
        for (int i = 0; i < mProductDetailList.size(); i++) {
            final View view = layoutInflater.inflate(R.layout.list_item_add_grn_product, null);
            final EditText edtQuantity = (EditText) view.findViewById(R.id.edtQuantity);
            final EditText edtRate = (EditText) view.findViewById(R.id.edtRate);
            final TextView txtProduct = (TextView) view.findViewById(R.id.txtProduct);
            final Spinner sprUnit = (Spinner) view.findViewById(R.id.sprUnit);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getUnitName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sprUnit.setAdapter(adapter);
            txtProduct.setText(mProductDetailList.get(i).getProduct_Name());
            final int finalI1 = i;
            edtQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(mLinProductContainer.getChildAt(finalI1) instanceof  LinearLayout){
                        LinearLayout linearLayout = ((LinearLayout) mLinProductContainer.getChildAt(finalI1));
                        EditText edtQuantity = (EditText) linearLayout.findViewById(R.id.edtQuantity);
                        EditText edtRate = (EditText) linearLayout.findViewById(R.id.edtRate);
                        EditText edtAmount = (EditText) linearLayout.findViewById(R.id.edtAmount);
                        if(!edtRate.getText().toString().trim().equals("") && !edtQuantity.getText().toString().trim().equals(""))
                            edtAmount.setText(""+Integer.parseInt(edtQuantity.getText().toString().trim())*
                                    Integer.parseInt(edtRate.getText().toString().trim()));

                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((AddGTNActivity)getActivity()).mAssesableAmount = getAllSelectProductDetails().split("Total: ")[1];
                            mAssessmentValue.setText(""+getAllSelectProductDetails());
                        }
                    }, 500);

                }
            });
            edtRate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(mLinProductContainer.getChildAt(finalI1) instanceof  LinearLayout){
                        LinearLayout linearLayout = ((LinearLayout) mLinProductContainer.getChildAt(finalI1));
                        EditText edtQuantity = (EditText) linearLayout.findViewById(R.id.edtQuantity);
                        EditText edtRate = (EditText) linearLayout.findViewById(R.id.edtRate);
                        EditText edtAmount = (EditText) linearLayout.findViewById(R.id.edtAmount);
                        if(!edtQuantity.getText().toString().trim().equals("") && !edtRate.getText().toString().trim().equals("")) {
                            edtAmount.setText(""+Integer.parseInt(edtQuantity.getText().toString().trim()) *
                                    Integer.parseInt(edtRate.getText().toString().trim()));
                        }
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((AddGTNActivity)getActivity()).mAssesableAmount = getAllSelectProductDetails().split("Total: ")[1];
                            mAssessmentValue.setText(""+getAllSelectProductDetails());
                        }
                    }, 500);
                }
            });
            mLinProductContainer.addView(view);
        }
    }

    private String getAllSelectProductDetails(){
        int total=0;
        for (int i = 0; i < mLinProductContainer.getChildCount(); i++) {
            LinearLayout linearLayout = ((LinearLayout) mLinProductContainer.getChildAt(i));
            EditText edtQuantity = (EditText) linearLayout.findViewById(R.id.edtQuantity);
            EditText edtRate = (EditText) linearLayout.findViewById(R.id.edtRate);
            EditText edtAmount = (EditText) linearLayout.findViewById(R.id.edtAmount);
            Spinner sprUnit = (Spinner) linearLayout.findViewById(R.id.sprUnit);
            if(!edtAmount.getText().toString().trim().equals(""))
                total = total+Integer.parseInt(edtAmount.getText().toString());
            Log.e("check", ""+edtQuantity.getText().toString()+", "+edtRate.getText().toString()+", "+edtAmount.getText().toString()+", "+sprUnit.getSelectedItemPosition());
        }
        return "Total: "+total;
    }

    private ArrayList<String> getUnitName() {
        ArrayList<String> unitList = new ArrayList<>();
        for (UnitModel unitModel : mUnitList) {
            unitList.add(unitModel.getUnit());
        }
        return unitList;
    }

    public String getAllValues(String grnId)  {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < mLinProductContainer.getChildCount(); i++) {
            JSONObject jsonObject = new JSONObject();
            LinearLayout linearLayout = ((LinearLayout) mLinProductContainer.getChildAt(i));
            EditText edtQuantity = (EditText) linearLayout.findViewById(R.id.edtQuantity);
            EditText edtRate = (EditText) linearLayout.findViewById(R.id.edtRate);
            EditText edtAmount = (EditText) linearLayout.findViewById(R.id.edtAmount);
            Spinner sprUnit = (Spinner) linearLayout.findViewById(R.id.sprUnit);
            try {
                jsonObject.put("Amount", "" + edtAmount.getText().toString().trim());
                jsonObject.put("GTN_ID", ""+grnId.replaceAll("\"", ""));
                jsonObject.put("Product_Category_ID", ""+getSelectedProductID(mSpnrProduct.getSelectedItemPosition()));
                jsonObject.put("Product_ID", ""+getSelectedProductID(mSpnrProduct.getSelectedItemPosition()));
                jsonObject.put("Qty", "" + edtQuantity.getText().toString().trim());
                jsonObject.put("Rate", "" + edtRate.getText().toString().trim());
                jsonObject.put("Sr_No", ""+(i+1));
                jsonObject.put("Unit", ""+getSelectedUnitName(sprUnit.getSelectedItemPosition()));
                jsonObject.put("WC_Id", ""+((AddGTNActivity)getActivity()).mProjectId);
                jsonObject.put("YearCode", "" + SharedPreferenceUtil.getString(AppUtils.YEAR_CODE, ""));
            }catch (Exception e){
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            Log.e("check", ""+edtQuantity.getText().toString()+", "+edtRate.getText().toString()+", "+edtAmount.getText().toString()+", "+sprUnit.getSelectedItemPosition());
            Log.e("json", ""+jsonArray.toString());
        }
        return jsonArray.toString();
    }

    public boolean isValidate() {
        for (int i = 0; i < mLinProductContainer.getChildCount(); i++) {
            LinearLayout linearLayout = ((LinearLayout) mLinProductContainer.getChildAt(i));
            EditText edtQuantity = (EditText) linearLayout.findViewById(R.id.edtQuantity);
            EditText edtRate = (EditText) linearLayout.findViewById(R.id.edtRate);
            EditText edtAmount = (EditText) linearLayout.findViewById(R.id.edtAmount);
            if(edtQuantity.getText().toString().trim().equals("")){
                return false;
            } else if(edtRate.getText().toString().trim().equals("")){
                return false;
            } else if(edtAmount.getText().toString().trim().equals("")){
                return false;
            }
        }
        return true;
    }

    class GetProductAsync extends AsyncTask<String, Void, String>{
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
            mProductProgress.setVisibility(View.GONE);
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

    class GetProductDetailAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_product_category_detail_url)+"/"+params[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try{
                if(response!="") {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        mProductDetailList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProductDetailModel productDetailModel = new ProductDetailModel();
                            productDetailModel.setAbbreviation(jsonArray.getJSONObject(i).getString("Abbreviation"));
                            productDetailModel.setProduct_Id(jsonArray.getJSONObject(i).getString("Product_Id"));
                            productDetailModel.setProduct_Name(jsonArray.getJSONObject(i).getString("Product_Name"));
                            mProductDetailList.add(productDetailModel);
                        }
                        setProductDetailList();
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
    }


    class GetUnitListAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_unit_list_url));
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try{
                if(response!="") {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        mUnitList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            UnitModel unitModel = new UnitModel();
                            unitModel.setUnit(jsonArray.getJSONObject(i).getString("Unit"));
                            unitModel.setUnit_id(jsonArray.getJSONObject(i).getString("Unit_id"));
                            mUnitList.add(unitModel);
                        }
                    } else {
                        AppUtils.showSnakbar(mRelParent, getString(R.string.no_data_available));
                    }
                }else {
                    AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
                }
            }catch (Exception e){
                e.printStackTrace();
                //AppUtils.showSnakbar(mRelParent, getString(R.string.error_server));
            }
        }
    }

    private void setSpinnerAdapter(){
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

}


