package com.sembiyan.app.model;

import java.util.UUID;

public class LineModel {
    private String uniqueID = UUID.randomUUID().toString();
    private String item;
    private String description;
    private Double quantity;
    private Double rate;


    public LineModel(String item, String description, Double quantity, Double rate) {
        this.item = item;
        this.description = description;
        this.quantity = quantity;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "LineModel{" +
                "uniqueID='" + uniqueID + '\'' +
                ", item='" + item + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                '}';
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getUniqueID() {
        return uniqueID;
    }
}
