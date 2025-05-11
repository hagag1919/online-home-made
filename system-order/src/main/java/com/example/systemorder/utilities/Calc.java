package com.example.systemorder.utilities;

public class Calc {

    public Double calcTotalPrice(Double price, int quantity) {
        Double total = (price * quantity);
        return total;
    }

    public Boolean isAccepted(Double totalPrice, Double minCharge) {
        return totalPrice >= minCharge;
    }
}
