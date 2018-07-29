package com.release.bibhu.planetappliances.Model;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class Complain implements Serializable{
    String issue = "";
    String customer_name = "";
    String mobile = "";
    String address = "";
    String payment = "";
    String service_type = "";
    String product_name = "";
    String id = "";
    String complainId = "";
    int complainType = 0;
    String positive = "";
    String feedBack = "";
    String enquiryId = "";
    String email = "";
    String status = "";

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    String make = "";
    String model = "";

    String emp_name ="";
    String emp_id="";
    String emp_phone="";

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_phone() {
        return emp_phone;
    }

    public void setEmp_phone(String emp_phone) {
        this.emp_phone = emp_phone;
    }

    public String getEmp_desig() {
        return emp_desig;
    }

    public void setEmp_desig(String emp_desig) {
        this.emp_desig = emp_desig;
    }

    String emp_desig="";


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }



    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }



    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }



    public int getComplainType() {
        return complainType;
    }

    public void setComplainType(int complainType) {
        this.complainType = complainType;
    }



    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }





    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    String priority = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
