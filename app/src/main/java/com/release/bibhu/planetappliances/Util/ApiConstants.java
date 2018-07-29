package com.release.bibhu.planetappliances.Util;

/**
 * Created by MUVI on 10-Feb-18.
 */

public class ApiConstants {

    //  public static final String BASE_URL = "http://adinathtech.in/";
//  public static final String BASE_URL = "http://stage.alysiantech.com/agent_app/";
    public static final String BASE_URL = "http://stage.alysiantech.com/API/";
//    public static final String BASE_URL = "http://www.planetappliances.com/API/";

    public static final String LOGIN = "login";
    public static String LOGIN_BASE_URL = BASE_URL + "users/";

    public static final String LOGOUT = "logout";
    public static final String LOGOUT_BASE_URL = BASE_URL + "src/public/";

    public static final String ENQUERY_DASHBOARD = "enquiryDashboard";
    public static final String ENQUERY_DASHBOARD_BASE_URL = BASE_URL + "users/";

    public static final String ASSIGNED_COMPLAIN = "listAssigned";
    public static final String ASSIGNED_COMPLAIN_BASE_URL = BASE_URL + "complain/";

    public static final String ASSIGNED_COMPLAIN_WITHOUT_BILLNO = "listAssigned";
    public static final String ASSIGNED_COMPLAIN_WITHOUT_BILLNO_BASE_URL = BASE_URL + "complain/";

    public static final String ADMIN_ASSIGNED_COMPLAIN = "listComplain";
    public static final String ADMIN_ASSIGNED_COMPLAIN_BASE_URL = BASE_URL + "complain/";

    public static final String ADMIN_ASSIGNED_COMPLAIN_WITHOUT_BILLNO = "listComplain";
    public static final String ADMIN_ASSIGNED_COMPLAIN_WITHOUT_BILLNO_BASE_URL = BASE_URL + "complain/";

    public static final String UPDATE_ASSIGNED_COMPLAIN = "save";
    public static final String UPDATE_ASSIGNED_COMPLAIN_BASE_URL = BASE_URL + "complain/";

    public static final String ASSIGN_COMPLAIN_OLD = "assignComplain";
    public static final String ASSIGN_COMPLAIN_OLD_BASE_URL = BASE_URL + "complain/";

    public static final String UPDATE_ASSIGNED_COMPLAIN_OLD = "save";
    public static final String UPDATE_ASSIGNED_COMPLAIN_OLD_BASE_URL = BASE_URL + "complain/";


    public static final String FORGOT_PASSWORD = "";
    public static final String FORGOT_PASSWORD_BASE_URL = "";

    public static final String ADD_CUSTOMER = "save";
    public static final String ADD_CUSTOMER_BASE_URL = BASE_URL + "customer/";

    public static final String PRODUCT_LIST = "listProduct";
    public static final String PRODUCT_LIST_BASE_URL = BASE_URL + "product/";

    public static final String MODEL_LIST = "";
    public static final String MODEL_LIST_BASE_URL = BASE_URL + "/";

    public static final String CHANGE_PASSWORD = "changePassword";
    public static final String CHANGE_PASSWORD_BASE_URL = BASE_URL + "users/";

    public static final String OTP_VALIDATE = "checkOtp";
    public static final String OTP_VALIDATE_BASE_URL = BASE_URL + "users/";


    public static final String CREATE_OTP = "createOtp";
    public static final String CREATE_OTP_BASE_URL = BASE_URL + "users/";

    public static final String CUSTOMER_ENQUERY = "save";
    public static final String CUSTOMER_ENQUERY_BASE_URL = BASE_URL + "enquiry/";

    public static final String PRODUCT_USER_DETAILS_FOR_COMPLAIN = "searchCustomer";
    public static final String PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL = BASE_URL + "customer/";



    public static final String ENQUERY_LIST = "listEnquiry";
    public static final String ENQUERY_LIST_BASE_URL = BASE_URL + "enquiry/";

    public static final String COMPLIAN_COUNT = "complainDashboard";
    public static final String COMPLIAN_COUNT_BASE_URL = BASE_URL + "users/";

    public static final String AMC_LIST = "listAmcType";
    public static final String AMC_LIST_BASE_URL = BASE_URL + "manage/";

    public static final String REGISTER_AMC_COMPLAIN = "saveAmc";
    public static final String REGISTER_AMC_COMPLAIN_BASE_URL = BASE_URL + "manage/";

    public static final String NEW_CUSTOMER_COMPLAIN = "customerComplinsave";
    public static final String NEW_CUSTOMER_COMPLAIN_BASE_URL = BASE_URL + "complain/";

    public static final String REGISTER_COMPLAIN = "customerComplinsave";
    public static final String REGISTER_COMPLAIN_BASE_URL = BASE_URL + "complain/";


    public static final String AGENT_LIST = "listAgent";
    public static final String AGENT_LIST_BASE_URL = BASE_URL + "agents/";

    public static final String ADMIN_DASHBOARD = "dashboardData";
    public static final String ADMIN_DASHBOARD_BASE_URL = BASE_URL + "users/";

    public static final String ONLY_SERVICING = "listAmcOnly";
    public static final String ONLY_SERVICING_BASE_URL = BASE_URL + "manage/";

    public static final String MOTOR_SERVICING = "listAmcMotor";
    public static final String MOTOR_SERVICING_BASE_URL = BASE_URL + "manage/";

    public static final String AGENT_ONLY_SERVICING = "listAmcOnlyAgent";
    public static final String AGENT_ONLY_SERVICING_BASE_URL = BASE_URL + "manage/";





    public static final String ASSIGN_ONLY_AMC_SERVICING = "assignAmcOnly";
    public static final String ASSIGN_ONLY_AMC_SERVICING_BASE_URL = BASE_URL + "manage/";

    public static final String RESOLVE_MOTOR_AMC = "saveAmc";
    public static final String RESOLVE_MOTOR_AMC_BASE_URL = BASE_URL + "manage/";

    public static final String RESOLVE_ONLY_AMC = "assignAmcOnly";
    public static final String RESOLVE_ONLY_AMC_BASE_URL = BASE_URL + "manage/";

}
