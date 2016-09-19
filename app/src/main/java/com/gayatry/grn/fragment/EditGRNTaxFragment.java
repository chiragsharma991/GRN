package com.gayatry.grn.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.gayatry.R;
import com.gayatry.model.TaxListModel;
import com.gayatry.rest.JsonParser;
import com.gayatry.utilz.AppUtils;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Admin on 06-Apr-16.
 */
public class EditGRNTaxFragment extends Fragment {

    private Spinner mSprTax;
    private RelativeLayout mRelParent;
    private ArrayList<TaxListModel> mTaxList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_tax, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mSprTax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(AppUtils.isOnline(getContext())){
                    //new GetProductDetailAsync().execute(getSelectedProductID(position));
                }else{
                    AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initView(View view) {
        mSprTax = (Spinner) view.findViewById(R.id.sprTax);
        mRelParent = (RelativeLayout) view.findViewById(R.id.relParent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(AppUtils.isOnline(getContext())){
                if(mTaxList == null)
                    new GetTaxAsync().execute();
            }else{
                AppUtils.showSnakbar(mRelParent, getString(R.string.error_internet));
            }
        }
    }

    class GetTaxAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.get_tax_list_url));
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
            if(response!="") {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    mTaxList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TaxListModel taxListModel = new TaxListModel();
                        taxListModel.setAmount(jsonArray.getJSONObject(i).getString("Amount"));
                        taxListModel.setPrintName(jsonArray.getJSONObject(i).getString("PrintName"));
                        taxListModel.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                        taxListModel.setTax_Detail_ID(jsonArray.getJSONObject(i).getString("Tax_Detail_ID"));
                        taxListModel.setTax_ID(jsonArray.getJSONObject(i).getString("Tax_ID"));
                        mTaxList.add(taxListModel);
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

    private void setSpinnerAdapter() {
        if(mSprTax!= null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, getPrintName());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSprTax.setAdapter(adapter);
        }
    }

    private ArrayList<String> getPrintName() {
        ArrayList<String> productName = new ArrayList<>();
        for (TaxListModel model : mTaxList){
            productName.add(model.getPrintName());
        }
        return productName;
    }

}
