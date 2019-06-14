package yucatancountryclub.com.ycc;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Zazu on 19/02/18.
 */

public class EndPoints {
    private static final String ROOT_URL = "https://ycc.punklabs.ninja/api/v1/";
    public static final String DIRECTORY_URL = ROOT_URL + "directory/";
    public static final String POST_IMG_URL = ROOT_URL + "reports/";
    public static final String UPLOAD_URL = ROOT_URL + "image-report/";
    public static final String REPORT_URL = ROOT_URL + "reports/";
    public static final String DOCUMENT_URL = ROOT_URL + "document_categories/";
    public static final String DEVICE_URL = ROOT_URL + "devices/";
    public static final String LOGIN_URL = ROOT_URL + "rest-auth/login/";
    public static String TOKEN = "";
    public static JSONArray directory = null;
    public static final String LOGOUT_URL = ROOT_URL + "rest-auth/logout/";
    public static final String USER_URL = ROOT_URL + "rest-auth/user/";
    public static final String QR_URL = ROOT_URL + "codes/";
}
