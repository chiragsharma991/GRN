package com.gayatry.report.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.model.AgingReportModel;
import com.gayatry.report.fragment.AgingReportFragment;

import java.util.List;

/**
 * Created by Admin on 01-May-16.
 */
public class AgingReportRecyclerAdapter extends RecyclerView.Adapter<AgingReportRecyclerAdapter.ViewHolder> {

    private List<AgingReportModel> mDataset;
    private AgingReportFragment agingReportFragment;

    public AgingReportRecyclerAdapter(List<AgingReportModel> arrayList, AgingReportFragment agingReportFragment) {
        mDataset = arrayList;
        this.agingReportFragment = agingReportFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtProductName, mTxtDay30, mTxtCategory, mTxtDay60, mTxtDay90, mTxtDay150, mTxtDay180, mTxtDayAbove180;
        public ViewHolder(View v) {
            super(v);
            mTxtProductName = (TextView) v.findViewById(R.id.txtProductName);
            mTxtDay30 = (TextView) v.findViewById(R.id.txtDay30);
            mTxtCategory = (TextView) v.findViewById(R.id.txtCategory);
            mTxtDay60 = (TextView) v.findViewById(R.id.txtDay60);
            mTxtDay90 = (TextView) v.findViewById(R.id.txtDay90);
            mTxtDay150 = (TextView) v.findViewById(R.id.txtDay150);
            mTxtDay180 = (TextView) v.findViewById(R.id.txtDay180);
            mTxtDayAbove180 = (TextView) v.findViewById(R.id.txtDayAbove180);
        }
    }

    @Override
    public AgingReportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_aging_report, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTxtProductName.setText(mDataset.get(position).getProduct_Name());
        holder.mTxtCategory.setText(mDataset.get(position).getProduct_Category_Name());
        holder.mTxtDay30.setText("<=30 : "+mDataset.get(position).getDays30_Qty());
        holder.mTxtDay60.setText(">30 & <=60 : "+mDataset.get(position).getDays3060_Qty());
        holder.mTxtDay90.setText(">60 & <=90 : "+mDataset.get(position).getDays6090_Qty());
        holder.mTxtDay150.setText(">90 & <=150 : "+mDataset.get(position).getDays90150_Qty());
        holder.mTxtDay180.setText(">150 & <=180 : "+mDataset.get(position).getDays150180_Qty());
        holder.mTxtDayAbove180.setText(">180 : "+mDataset.get(position).getDays180_Qty());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
