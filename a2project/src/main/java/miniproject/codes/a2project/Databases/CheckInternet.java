package miniproject.codes.a2project.Databases;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {
    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}
