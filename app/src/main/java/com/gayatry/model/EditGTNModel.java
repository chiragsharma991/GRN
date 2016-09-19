package com.gayatry.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 11-Aug-16.
 */
public class EditGTNModel implements Parcelable {

    public EditGTNModel(){}

    public String Amount;
    public String Challan_Date;
    public String Challan_No;
    public String GTN_Code;
    public String GTN_ID;
    public String GTN_No;
    public String GTN_Type;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
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

    public String getGTN_Code() {
        return GTN_Code;
    }

    public void setGTN_Code(String GTN_Code) {
        this.GTN_Code = GTN_Code;
    }

    public String getGTN_ID() {
        return GTN_ID;
    }

    public void setGTN_ID(String GTN_ID) {
        this.GTN_ID = GTN_ID;
    }

    public String getGTN_No() {
        return GTN_No;
    }

    public void setGTN_No(String GTN_No) {
        this.GTN_No = GTN_No;
    }

    public String getGTN_Type() {
        return GTN_Type;
    }

    public void setGTN_Type(String GTN_Type) {
        this.GTN_Type = GTN_Type;
    }

    public String getGTN_date() {
        return GTN_date;
    }

    public void setGTN_date(String GTN_date) {
        this.GTN_date = GTN_date;
    }

    public String getIsCancelled() {
        return IsCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        IsCancelled = isCancelled;
    }

    public String getNet_Qty() {
        return Net_Qty;
    }

    public void setNet_Qty(String net_Qty) {
        Net_Qty = net_Qty;
    }

    public String getProduct_Category_ID() {
        return Product_Category_ID;
    }

    public void setProduct_Category_ID(String product_Category_ID) {
        Product_Category_ID = product_Category_ID;
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

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProject_ID() {
        return Project_ID;
    }

    public void setProject_ID(String project_ID) {
        Project_ID = project_ID;
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

    public String getTotalGTNValue() {
        return TotalGTNValue;
    }

    public void setTotalGTNValue(String totalGTNValue) {
        TotalGTNValue = totalGTNValue;
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

    public String getWC_Id() {
        return WC_Id;
    }

    public void setWC_Id(String WC_Id) {
        this.WC_Id = WC_Id;
    }

    public String getWC_Name() {
        return WC_Name;
    }

    public void setWC_Name(String WC_Name) {
        this.WC_Name = WC_Name;
    }

    public String getYearCode() {
        return YearCode;
    }

    public void setYearCode(String yearCode) {
        YearCode = yearCode;
    }

    public String GTN_date;
    public String IsCancelled;
    public String Net_Qty;
    public String Product_Category_ID;
    public String Product_Category_Name;
    public String Product_ID;
    public String Product_Name;
    public String ProjectName;
    public String Project_ID;
    public String Qty;
    public String Rate;
    public String Remark;
    public String TotalGTNValue;
    public String Unit;
    public String Unit_Id;
    public String WC_Id;
    public String WC_Name;
    public String YearCode;

    protected EditGTNModel(Parcel in) {
        Amount = in.readString();
        Challan_Date = in.readString();
        Challan_No = in.readString();
        GTN_Code = in.readString();
        GTN_ID = in.readString();
        GTN_No = in.readString();
        GTN_Type = in.readString();
        GTN_date = in.readString();
        IsCancelled = in.readString();
        Net_Qty = in.readString();
        Product_Category_ID = in.readString();
        Product_Category_Name = in.readString();
        Product_ID = in.readString();
        Product_Name = in.readString();
        ProjectName = in.readString();
        Project_ID = in.readString();
        Qty = in.readString();
        Rate = in.readString();
        Remark = in.readString();
        TotalGTNValue = in.readString();
        Unit = in.readString();
        Unit_Id = in.readString();
        WC_Id = in.readString();
        WC_Name = in.readString();
        YearCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Amount);
        dest.writeString(Challan_Date);
        dest.writeString(Challan_No);
        dest.writeString(GTN_Code);
        dest.writeString(GTN_ID);
        dest.writeString(GTN_No);
        dest.writeString(GTN_Type);
        dest.writeString(GTN_date);
        dest.writeString(IsCancelled);
        dest.writeString(Net_Qty);
        dest.writeString(Product_Category_ID);
        dest.writeString(Product_Category_Name);
        dest.writeString(Product_ID);
        dest.writeString(Product_Name);
        dest.writeString(ProjectName);
        dest.writeString(Project_ID);
        dest.writeString(Qty);
        dest.writeString(Rate);
        dest.writeString(Remark);
        dest.writeString(TotalGTNValue);
        dest.writeString(Unit);
        dest.writeString(Unit_Id);
        dest.writeString(WC_Id);
        dest.writeString(WC_Name);
        dest.writeString(YearCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EditGTNModel> CREATOR = new Parcelable.Creator<EditGTNModel>() {
        @Override
        public EditGTNModel createFromParcel(Parcel in) {
            return new EditGTNModel(in);
        }

        @Override
        public EditGTNModel[] newArray(int size) {
            return new EditGTNModel[size];
        }
    };
}