package com.gayatry.gtn.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.gtn.GTNListActivity;
import com.gayatry.model.GTNListModel;
import com.gayatry.utilz.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23-Apr-16.
 */
public class GTNRecyclerAdapter extends RecyclerView.Adapter<GTNRecyclerAdapter.ViewHolder> {

    private List<GTNListModel> mDataset;
    private GTNListActivity gtnListActivity;
    private int lastPosition = 0;

    public GTNRecyclerAdapter(ArrayList<GTNListModel> arrayList, GTNListActivity gtnListActivity) {
        mDataset=arrayList;
        this.gtnListActivity = gtnListActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtProjectName, mTxtChallanNo, mTxtDate, mTxtGtnCode, mTxtEdit, mTxtDelete;
        public CardView mCardView;
        public ViewHolder(View v) {
            super(v);
            mTxtProjectName = (TextView) v.findViewById(R.id.txtProjectName);
            mTxtChallanNo = (TextView) v.findViewById(R.id.txtChallanNo);
            mTxtDate = (TextView) v.findViewById(R.id.txtDate);
            mTxtGtnCode = (TextView) v.findViewById(R.id.txtGtnCode);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTxtEdit = (TextView) v.findViewById(R.id.txtEdit);
            mTxtDelete = (TextView) v.findViewById(R.id.txtDelete);
        }
    }

    @Override
    public GTNRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_gtn, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTxtProjectName.setText(mDataset.get(position).getProjectName());
        holder.mTxtDate.setText(AppUtils.formattedDate("dd/MM/yyyy", "dd MMM yyyy", mDataset.get(position).getGTN_date()));
        holder.mTxtGtnCode.setText(mDataset.get(position).getGTN_Code());
        holder.mTxtChallanNo.setText(mDataset.get(position).getChallan_No());
        holder.mTxtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gtnListActivity.openAlertDialog(mDataset.get(position), position);
            }
        });
        holder.mTxtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gtnListActivity.onEditClick(mDataset.get(position));
            }
        });
        animate(holder.mCardView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void animate(View view, final int pos) {
        if (pos > lastPosition) {
            view.animate().cancel();
            view.setTranslationY(100);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(100);
            lastPosition = pos;
        }
    }
}