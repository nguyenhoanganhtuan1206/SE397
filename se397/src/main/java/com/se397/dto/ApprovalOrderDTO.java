package com.se397.dto;

import java.time.LocalDate;

public class ApprovalOrderDTO {
    private String name;
    private LocalDate startDate;
    private double totalPrice;
    private String address;
    private String phoneNumber;
    private boolean status;
    private String province;

    public ApprovalOrderDTO() {}

    public ApprovalOrderDTO(String name, LocalDate startDate, double totalPrice, String address, String phoneNumber, boolean status, String province) {
        this.name = name;
        this.startDate = startDate;
        this.totalPrice = totalPrice;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
