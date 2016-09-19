package com.gayatry.report.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.grn.GRNListActivity;
import com.gayatry.model.GRNListModel;
import com.gayatry.model.StockReportModel;
import com.gayatry.report.fragment.StockReportFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 01-May-16.
 */
public class StockReportRecyclerAdapter extends RecyclerView.Adapter<StockReportRecyclerAdapter.ViewHolder> {

    private List<StockReportModel> mDataset;
    private StockReportFragment stockReportFragment;

    public StockReportRecyclerAdapter(List<StockReportModel> arrayList, StockReportFragment stockReportFragment) {
        mDataset = arrayList;
        this.stockReportFragment = stockReportFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtProductName, mTxtConsumption, mTxtCategory, mTxtOpening, mTxtClosing, mTxtPurchase;
        public ViewHolder(View v) {
            super(v);
            mTxtProductName = (TextView) v.findViewById(R.id.txtProductName);
            mTxtConsumption = (TextView) v.findViewById(R.id.txtConsumption);
            mTxtCategory = (TextView) v.findViewById(R.id.txtCategory);
            mTxtPurchase = (TextView) v.findViewById(R.id.txtPurchase);
            mTxtOpening = (TextView) v.findViewById(R.id.txtOpening);
            mTxtClosing = (TextView) v.findViewById(R.id.txtClosing);
        }
    }

    @Override
    public StockReportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stock_report, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTxtProductName.setText(mDataset.get(position).getProduct_Name());
        holder.mTxtCategory.setText(mDataset.get(position).getProduct_Type());
        holder.mTxtConsumption.setText(mDataset.get(position).getConsp_Qty());
        holder.mTxtOpening.setText(mDataset.get(position).getOP_Qty());
        holder.mTxtClosing.setText(mDataset.get(position).getCl_Qty());
        holder.mTxtPurchase.setText(mDataset.get(position).getPur_Qty());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
