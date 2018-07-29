package com.release.bibhu.planetappliances.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class OnlyServiceModel implements Serializable{
    String name = "";
    String phone = "";
    String amc_number = "";
    String user_id = "";
    String id = "";
    String amc_type = "";
    String service_list_details = "";
    ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModels = new ArrayList<>();


    public ArrayList<OnlyServiceDetailsModel> getOnlyServiceDetailsModels() {
        return onlyServiceDetailsModels;
    }

    public void setOnlyServiceDetailsModels(ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModels) {
        this.onlyServiceDetailsModels = onlyServiceDetailsModels;
    }


    public String getService_list_details() {
        return service_list_details;
    }

    public void setService_list_details(String service_list_details) {
        this.service_list_details = service_list_details;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmc_number() {
        return amc_number;
    }

    public void setAmc_number(String amc_number) {
        this.amc_number = amc_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmc_type() {
        return amc_type;
    }

    public void setAmc_type(String amc_type) {
        this.amc_type = amc_type;
    }







}
