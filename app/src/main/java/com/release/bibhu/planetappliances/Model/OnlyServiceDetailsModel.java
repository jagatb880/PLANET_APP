package com.release.bibhu.planetappliances.Model;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class OnlyServiceDetailsModel implements Serializable{

    String service_date = "";
    String service_no = "";
    String service_id = "";
    String user_id = "";
    String status = "";


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_date() {
        return service_date;
    }

    public void setService_date(String service_date) {
        this.service_date = service_date;
    }

    public String getService_no() {
        return service_no;
    }

    public void setService_no(String service_no) {
        this.service_no = service_no;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }


}
