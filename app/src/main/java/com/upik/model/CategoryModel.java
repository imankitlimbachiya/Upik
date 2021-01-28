package com.upik.model;

/**
 * Created by Admin on 5/25/2019.
 */

public class CategoryModel {
    String CategoryID, CategoryName, array_job_list;

    public String getArray_job_list() {
        return array_job_list;
    }

    public void setArray_job_list(String array_job_list) {
        this.array_job_list = array_job_list;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
