package yucatancountryclub.com.ycc;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Lincoln on 05/05/16.
 */
/**
 * Created by sebastianmendezgiron on 25/01/18.
 */

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome-to-ycc";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String TOKEN = "";
    private static final Boolean IS_LOGGED_IN = false;
    private static final String REGISTRATION_ID = "";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void setLogged(Boolean logged) {
        editor.putBoolean(String.valueOf(IS_LOGGED_IN), logged);
        editor.commit();
    }

    public boolean getLogged() {
        return pref.getBoolean(String.valueOf(IS_LOGGED_IN), false);
    }

    public String getRegistrationId() {
        return pref.getString(REGISTRATION_ID, "");
    }
    public void setRegistrationId(String reg){
        editor.putString(REGISTRATION_ID, reg);
        editor.commit();
    }
}
