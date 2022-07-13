package miniproject.codes.a2project.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

//used by whole app to find out login or not
public class SessionManager {
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_CLGROLLNO = "clgRollNo";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DATE = "date";
    public static final String KEY_GENDER = "gender";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = _context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String fullName, String userName, String clgRollNo, String email, String phoneNo, String password, String date, String gender) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_CLGROLLNO, clgRollNo);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phoneNo);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_GENDER, gender);
        editor.commit();
    }

    public HashMap<String, String> getUsersDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_CLGROLLNO, usersSession.getString(KEY_CLGROLLNO, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONENUMBER, usersSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_DATE, usersSession.getString(KEY_DATE, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else return false;

    }

    public void logoutFromSession() {
        editor.clear();
        editor.commit();
    }

}
