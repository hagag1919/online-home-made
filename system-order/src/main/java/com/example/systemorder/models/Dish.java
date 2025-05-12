package com.example.systemorder.models;


public class Dish {
    private Long id;
    private String name;
    private Double price;
    private Integer amount;
    private String description;

    public Dish() {
    }
    
    public Dish(Long id,String name, Double price, Integer amount, String description) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}
