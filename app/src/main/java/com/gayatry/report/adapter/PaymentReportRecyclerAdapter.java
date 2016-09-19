package com.gayatry.report.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.model.PaymentReportModel;
import com.gayatry.report.fragment.PaymentReportFragment;

import java.util.List;

/**
 * Created by Admin on 01-May-16.
 */
public class PaymentReportRecyclerAdapter extends RecyclerView.Adapter<PaymentReportRecyclerAdapter.ViewHolder> {

    private List<PaymentReportModel> mDataset;
    private PaymentReportFragment paymentReportFragment;

    public PaymentReportRecyclerAdapter(List<PaymentReportModel> arrayList, PaymentReportFragment paymentReportFragment) {
        mDataset = arrayList;
        this.paymentReportFragment = paymentReportFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtProductName, mTxtGRNCode, mTxtGRNAmount, mTxtAmount, mTxtPaymentDetail, mTxtPartyInvoice;
        public ViewHolder(View v) {
            super(v);
            mTxtProductName = (TextView) v.findViewById(R.id.txtProductName);
            mTxtGRNCode = (TextView) v.findViewById(R.id.txtGrnCode);
            mTxtGRNAmount = (TextView) v.findViewById(R.id.txtGrnAmount);
            mTxtAmount = (TextView) v.findViewById(R.id.txtAmount);
            mTxtPaymentDetail = (TextView) v.findViewById(R.id.txtPaymentDetail);
            mTxtPartyInvoice = (TextView) v.findViewById(R.id.txtPartyInvoice);
        }
    }

    @Override
    public PaymentReportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_payment_report, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTxtProductName.setText(mDataset.get(position).getPartyName());
        holder.mTxtGRNCode.setText(mDataset.get(position).getGRN_Code());
        holder.mTxtGRNAmount.setText(mDataset.get(position).getTotalGRNValue());
        holder.mTxtAmount.setText(mDataset.get(position).getOutStanding_Amt());
        holder.mTxtPaymentDetail.setText(mDataset.get(position).getPayment_Status());
        holder.mTxtPartyInvoice.setText(mDataset.get(position).getParty_Inv_No());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
