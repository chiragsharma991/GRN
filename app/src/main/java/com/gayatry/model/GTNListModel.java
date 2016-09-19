package com.gayatry.model;

import java.io.Serializable;

/**
 * Created by Admin on 27-Apr-16.
 */
public class GTNListModel implements Serializable{

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

    public String getTotalGTNValue() {
        return TotalGTNValue;
    }

    public void setTotalGTNValue(String totalGTNValue) {
        TotalGTNValue = totalGTNValue;
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

    public String Challan_Date;
    public String Challan_No;
    public String GTN_Code;
    public String GTN_ID;
    public String GTN_No;
    public String GTN_date;
    public String IsCancelled;
    public String ProjectName;
    public String Project_ID;
    public String TotalGTNValue;
    public String WC_Id;
    public String WC_Name;
    public String YearCode;
}
