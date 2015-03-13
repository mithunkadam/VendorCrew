package com.acc.vendorcrew.model;

/**
 * Created by Sagar on 3/10/2015.
 */
public class CategoriesModel {
    private String name;
    private String _id;
    private String imagePath;
    private int categoryId;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }

    public String getId(){
        return _id;
    }

    public void set_id(String _id){
        this._id = _id;
    }
}
