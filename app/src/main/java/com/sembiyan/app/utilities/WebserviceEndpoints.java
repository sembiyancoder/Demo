package com.sembiyan.app.utilities;

public class WebserviceEndpoints {
    public static String BASE_URL = "https://jallikattu.madhakottai.com/api/";
    public static String BASE_URL_V2 = "https://jallikattu.madhakottai.com/api/v2/";
    public static String LOGIN_AUTHENTICATION_URL = BASE_URL + "authentication/oauth2/token";
    public static String READ_RECORDS_I_SEARCH = BASE_URL_V2 + "isearch_read";
    public static String READ_PRODUCT_LIST = BASE_URL_V2 + "name_search";
    public static String POST_REGISTER_VIP = BASE_URL_V2 + "write";
}
