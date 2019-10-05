package com.example.staffcafeposapp.Model;

import java.util.ArrayList;

public class Order {
    private int order_id, table_no;
    private double order_total;
    private ArrayList<MenuItem> orderItemArrayList;
    private String order_status;

    public Order() {
    }

    public Order(int order_id, int table_no, double order_total, ArrayList<MenuItem> menuItems) {
        this.order_id = order_id;
        this.table_no = table_no;
        this.order_total = order_total;
        this.orderItemArrayList = menuItems;
        this.order_status = "Not Paid";
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getTable_no() {
        return table_no;
    }

    public void setTable_no(int table_no) {
        this.table_no = table_no;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public ArrayList<MenuItem> getOrderItemArrayList() {
        return orderItemArrayList;
    }

    public void setOrderItemArrayList(ArrayList<MenuItem> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
    }
}
