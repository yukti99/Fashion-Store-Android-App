package com.example.fashionista.Model;

public class Cart {
    private String pid,pname,price,discount,quantity;
    public Cart(){

    }
    public Cart(String pid, String pname, String price, String discount, String quantity) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }
    public String getPid() {
        return pid;
    }
    public String getPname() {
        return pname;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
