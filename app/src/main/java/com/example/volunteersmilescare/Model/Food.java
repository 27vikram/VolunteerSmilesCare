package com.example.volunteersmilescare.Model;

public class Food {
    private String Name, Image, Price, Discount, MenuID;

    public Food() {
    }

    public Food(String name, String image, String price, String discount, String menuID) {
        Name = name;
        Price = price;
        Discount = discount;
        MenuID = menuID;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }
}
