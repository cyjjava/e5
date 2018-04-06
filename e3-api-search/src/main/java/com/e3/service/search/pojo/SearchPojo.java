package com.e3.service.search.pojo;

import java.io.Serializable;

/**
 * Created by CYJ on 2018/3/15.
 */
public class SearchPojo implements Serializable{

    private String image;
    private String id;
    private long price;
    private String sell_point;
    private String title;
    private String catName;
    private String item_desc;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public  String[]  getImages(){
        String img[] =  this.getImage().split(",");
        return img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }
}
