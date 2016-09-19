package com.gayatry.model;

/**
 * Created by Admin on 30-Apr-16.
 */
public class ProductListModel {

    public String getAbbreviation() {
        return Abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        Abbreviation = abbreviation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIs_Active() {
        return Is_Active;
    }

    public void setIs_Active(String is_Active) {
        Is_Active = is_Active;
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

    public String Abbreviation;
    public String Description;
    public String Is_Active;
    public String Product_Category_Id;
    public String Product_Category_Name;

}
