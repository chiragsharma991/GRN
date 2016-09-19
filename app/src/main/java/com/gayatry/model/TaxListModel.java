package com.gayatry.model;

/**
 * Created by Admin on 04-May-16.
 */
public class TaxListModel {

    public String Amount;
    public String PrintName;

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPrintName() {
        return PrintName;
    }

    public void setPrintName(String printName) {
        PrintName = printName;
    }

    public String getTax_ID() {
        return Tax_ID;
    }

    public void setTax_ID(String tax_ID) {
        Tax_ID = tax_ID;
    }

    public String getTax_Detail_ID() {
        return Tax_Detail_ID;
    }

    public void setTax_Detail_ID(String tax_Detail_ID) {
        Tax_Detail_ID = tax_Detail_ID;
    }

    public String Rate;
    public String Tax_Detail_ID;
    public String Tax_ID;

}
