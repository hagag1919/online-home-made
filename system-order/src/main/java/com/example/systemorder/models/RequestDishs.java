package com.example.systemorder.models;

public class RequestDishs {
    private long dishID;
    private int quantity;
    public RequestDishs(long dishID, int quantity) {}
    public  RequestDishs() {}
    public long getDishID() {
        return dishID;
    }
    public void setDishID(long dishID) {
        this.dishID = dishID;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
