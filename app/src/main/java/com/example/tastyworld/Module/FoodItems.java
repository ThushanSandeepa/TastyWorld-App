package com.example.tastyworld.Module;

public class FoodItems {

    private String productName,description,price,date,image,pid,time;

    public FoodItems() {
    }

    public FoodItems(String productName, String description, String price, String date, String image, String pid, String time) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.date = date;
        this.image = image;
        this.pid = pid;
        this.time = time;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
