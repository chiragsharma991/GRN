package com.gayatry.model;

/**
 * Created by Admin on 29-Jul-16.
 */
public class ProjectListModel {

    public String Excise_Opening;
    public String Name;
    public String NonExcise_Opening;

    public String getWC_ID() {
        return WC_ID;
    }

    public void setWC_ID(String WC_ID) {
        this.WC_ID = WC_ID;
    }

    public String getExcise_Opening() {
        return Excise_Opening;
    }

    public void setExcise_Opening(String excise_Opening) {
        Excise_Opening = excise_Opening;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNonExcise_Opening() {
        return NonExcise_Opening;
    }

    public void setNonExcise_Opening(String nonExcise_Opening) {
        NonExcise_Opening = nonExcise_Opening;
    }

    public String WC_ID;
}
