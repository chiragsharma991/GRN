package com.gayatry.grn.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.grn.GRNListActivity;
import com.gayatry.model.GRNListModel;

import java.util.ArrayList;
import java.util.List;

public class GRNRecyclerAdapter extends RecyclerView.Adapter<GRNRecyclerAdapter.ViewHolder> {

    private List<GRNListModel> mDataset;
    private GRNListActivity grnListActivity;
    private int lastPosition = 0;

    public GRNRecyclerAdapter(ArrayList<GRNListModel> arrayList, GRNListActivity grnListActivity) {
        mDataset=arrayList;
        this.grnListActivity = grnListActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtPartyName, mTxtGrnType, mTxtDate, mTxtGrnCode, mTxtEdit, mTxtDelete;
        public CardView mCardView;
        public ViewHolder(View v) {
            super(v);
            mTxtPartyName = (TextView) v.findViewById(R.id.txtPartyName);
            mTxtGrnType = (TextView) v.findViewById(R.id.txtGrnType);
            mTxtDate = (TextView) v.findViewById(R.id.txtDate);
            mTxtGrnCode = (TextView) v.findViewById(R.id.txtGrnCode);
            mTxtEdit = (TextView) v.findViewById(R.id.txtEdit);
            mTxtDelete = (TextView) v.findViewById(R.id.txtDelete);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    @Override
    public GRNRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.list_item_grn, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTxtPartyName.setText(mDataset.get(position).getPartyName());
        holder.mTxtDate.setText(mDataset.get(position).getGRN_date());
        holder.mTxtGrnCode.setText(mDataset.get(position).getGRN_Code());
        holder.mTxtGrnType.setText(mDataset.get(position).getGRN_Type());
        holder.mTxtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grnListActivity.openAlertDialog(mDataset.get(position), position);
            }
        });
        holder.mTxtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grnListActivity.onEditClick(mDataset.get(position));
            }
        });
        animate(holder.mCardView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

   /* @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((ViewHolder)holder).mCardView.clearAnimation();
    }*/

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(grnListActivity, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
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