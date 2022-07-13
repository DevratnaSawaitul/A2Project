package miniproject.codes.a2project.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.R;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout newPassword, reNewPassword;
    ProgressBar update_progress_bar;
    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_new_password);
        newPassword = findViewById(R.id.new_password);
        reNewPassword = findViewById(R.id.reNew_password);
        update_progress_bar = findViewById(R.id.update_progress_bar);
        update_progress_bar.setVisibility(View.GONE);
    }

    public void setNewPasswordBtn(View view) {
        update_progress_bar.setVisibility(View.VISIBLE);


        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getApplicationContext())) {
            update_progress_bar.setVisibility(View.GONE);
            showCustomDialog();
            return;
        }

        if (!validatePassword() | !validateRePassword()) {
            update_progress_bar.setVisibility(View.GONE);
            return;
        }

        String _newPassword = newPassword.getEditText().getText().toString().trim();
        String _phoneNo = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNo).child("password").setValue(_newPassword);
        Toast.makeText(getApplicationContext(), "Updated SuccessFully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class));
        finish();
    }

    private boolean validateRePassword() {
        String val = newPassword.getEditText().getText().toString().trim();
        String checkPassword = "^" +
//                "(?=.*[0-9])"+    //at least 1 digit
//                "(?=.*[a-z])"+    //at least 1 lower case letter
//                "(?=.*[A-Z])"+    //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +  //any letter
//                "(?=.*[@#$%^&+=])" +//at least one  special character
                "(?=\\S+$)" +       //no white space
                ".{4,}" +           //at least 4 character
                "$";
        if (val.isEmpty()) {
            newPassword.setError("Field can no be empty");
            newPassword.requestFocus();
            return false;
        } else if (!val.matches(checkPassword)) {
            newPassword.setError("Password should contain 4 characters! ");
            newPassword.requestFocus();
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = reNewPassword.getEditText().getText().toString().trim();
        String checkPassword = "^" +
//                "(?=.*[0-9])"+    //at least 1 digit
//                "(?=.*[a-z])"+    //at least 1 lower case letter
//                "(?=.*[A-Z])"+    //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +  //any letter
//                "(?=.*[@#$%^&+=])" +//at least one  special character
                "(?=\\S+$)" +       //no white space
                ".{4,}" +           //at least 4 character
                "$";
        if (val.isEmpty()) {
            reNewPassword.setError("Field can no be empty");
            reNewPassword.requestFocus();
            return false;
        } else if (!val.matches(checkPassword)) {
            reNewPassword.setError("Password should contain 4 characters! ");
            reNewPassword.requestFocus();
            return false;
        } else if (!val.equals(newPassword.getEditText().getText().toString().trim())) {
            reNewPassword.setError("rePassword Doesn't match with password");
            reNewPassword.requestFocus();
            return false;
        } else {
            reNewPassword.setError(null);
            reNewPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void showCustomDialog() {

        aDialog.setContentView(R.layout.popup);
        aDialog.setCanceledOnTouchOutside(false);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close_dialog = aDialog.findViewById(R.id.close_cross);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        connect_Internet = aDialog.findViewById(R.id.connect_internet);
        connect_Internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                aDialog.dismiss();
            }
        });
        aDialog.show();
    }

    private boolean isConnected(SetNewPassword setNewPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) setNewPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificonn != null && wificonn.isConnected()) || (mobileconn != null && mobileconn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}