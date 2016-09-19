package com.gayatry.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 10-Aug-16.
 */
public class EditGRNModel implements Parcelable {

    public EditGRNModel(){

    }

    public String Amount;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAssessableAmount() {
        return AssessableAmount;
    }

    public void setAssessableAmount(String assessableAmount) {
        AssessableAmount = assessableAmount;
    }

    public String getChallan_Date() {
        return Challan_Date;
    }

    public void setChallan_Date(String challan_Date) {
        Challan_Date = challan_Date;
    }

    public String getChallan_No() {
        return Challan_No;
    }

    public void setChallan_No(String challan_No) {
        Challan_No = challan_No;
    }

    public String getCredit_Period() {
        return Credit_Period;
    }

    public void setCredit_Period(String credit_Period) {
        Credit_Period = credit_Period;
    }

    public String getGRN_Code() {
        return GRN_Code;
    }

    public void setGRN_Code(String GRN_Code) {
        this.GRN_Code = GRN_Code;
    }

    public String getGRN_date() {
        return GRN_date;
    }

    public void setGRN_date(String GRN_date) {
        this.GRN_date = GRN_date;
    }

    public String getGRN_ID() {
        return GRN_ID;
    }

    public void setGRN_ID(String GRN_ID) {
        this.GRN_ID = GRN_ID;
    }

    public String getGRN_No() {
        return GRN_No;
    }

    public void setGRN_No(String GRN_No) {
        this.GRN_No = GRN_No;
    }

    public String getGRN_Type() {
        return GRN_Type;
    }

    public void setGRN_Type(String GRN_Type) {
        this.GRN_Type = GRN_Type;
    }

    public String getLR_Date() {
        return LR_Date;
    }

    public void setLR_Date(String LR_Date) {
        this.LR_Date = LR_Date;
    }

    public String getLR_No() {
        return LR_No;
    }

    public void setLR_No(String LR_No) {
        this.LR_No = LR_No;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getParty_ID() {
        return Party_ID;
    }

    public void setParty_ID(String party_ID) {
        Party_ID = party_ID;
    }

    public String getProduct_Category_Id() {
        return Product_Category_Id;
    }

    public void setProduct_Category_Id(String product_Category_Id) {
        Product_Category_Id = product_Category_Id;
    }

    public String getProduct_Category_Name() {
        return Product_Category_Name;
    }

    public void setProduct_Category_Name(String product_Category_Name) {
        Product_Category_Name = product_Category_Name;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSupp_Inv_Date() {
        return Supp_Inv_Date;
    }

    public void setSupp_Inv_Date(String supp_Inv_Date) {
        Supp_Inv_Date = supp_Inv_Date;
    }

    public String getSupp_Inv_No() {
        return Supp_Inv_No;
    }

    public void setSupp_Inv_No(String supp_Inv_No) {
        Supp_Inv_No = supp_Inv_No;
    }

    public String getTotalGRNValue() {
        return TotalGRNValue;
    }

    public void setTotalGRNValue(String totalGRNValue) {
        TotalGRNValue = totalGRNValue;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnit_Id() {
        return Unit_Id;
    }

    public void setUnit_Id(String unit_Id) {
        Unit_Id = unit_Id;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String AssessableAmount;
    public String Challan_Date;
    public String Challan_No;
    public String Credit_Period;
    public String GRN_Code;
    public String GRN_date;
    public String GRN_ID;
    public String GRN_No;
    public String GRN_Type;
    public String LR_Date;
    public String LR_No;
    public String PartyName;
    public String Party_ID;
    public String Product_Category_Id;
    public String Product_Category_Name;
    public String Product_ID;
    public String Product_Name;
    public String Qty;
    public String Rate;
    public String Remark;
    public String Supp_Inv_Date;
    public String Supp_Inv_No;
    public String TotalGRNValue;
    public String Unit;
    public String Unit_Id;
    public String VehicleNo;



    protected EditGRNModel(Parcel in) {
        Amount = in.readString();
        AssessableAmount = in.readString();
        Challan_Date = in.readString();
        Challan_No = in.readString();
        Credit_Period = in.readString();
        GRN_Code = in.readString();
        GRN_date = in.readString();
        GRN_ID = in.readString();
        GRN_No = in.readString();
        GRN_Type = in.readString();
        LR_Date = in.readString();
        LR_No = in.readString();
        PartyName = in.readString();
        Party_ID = in.readString();
        Product_Category_Id = in.readString();
        Product_Category_Name = in.readString();
        Product_ID = in.readString();
        Product_Name = in.readString();
        Qty = in.readString();
        Rate = in.readString();
        Remark = in.readString();
        Supp_Inv_Date = in.readString();
        Supp_Inv_No = in.readString();
        TotalGRNValue = in.readString();
        Unit = in.readString();
        Unit_Id = in.readString();
        VehicleNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Amount);
        dest.writeString(AssessableAmount);
        dest.writeString(Challan_Date);
        dest.writeString(Challan_No);
        dest.writeString(Credit_Period);
        dest.writeString(GRN_Code);
        dest.writeString(GRN_date);
        dest.writeString(GRN_ID);
        dest.writeString(GRN_No);
        dest.writeString(GRN_Type);
        dest.writeString(LR_Date);
        dest.writeString(LR_No);
        dest.writeString(PartyName);
        dest.writeString(Party_ID);
        dest.writeString(Product_Category_Id);
        dest.writeString(Product_Category_Name);
        dest.writeString(Product_ID);
        dest.writeString(Product_Name);
        dest.writeString(Qty);
        dest.writeString(Rate);
        dest.writeString(Remark);
        dest.writeString(Supp_Inv_Date);
        dest.writeString(Supp_Inv_No);
        dest.writeString(TotalGRNValue);
        dest.writeString(Unit);
        dest.writeString(Unit_Id);
        dest.writeString(VehicleNo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EditGRNModel> CREATOR = new Parcelable.Creator<EditGRNModel>() {
        @Override
        public EditGRNModel createFromParcel(Parcel in) {
            return new EditGRNModel(in);
        }

        @Override
        public EditGRNModel[] newArray(int size) {
            return new EditGRNModel[size];
        }
    };
}