package miniproject.codes.a2project.Common.LoginSignup;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.R;

public class ForgetPassword extends AppCompatActivity {

    CountryCodePicker forgetPasswordCountryCode;
    Button nextBtn;
    ProgressBar aProgressBar;
    TextInputLayout forgetPhoneNumber;

    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password);
        forgetPasswordCountryCode = findViewById(R.id.forget_country_code);
        nextBtn = findViewById(R.id.forget_next_btn);
        aProgressBar = findViewById(R.id.forget_progress_bar);
        forgetPhoneNumber = findViewById(R.id.forget_phone_no);
        aDialog = new Dialog(this);
        aProgressBar.setVisibility(View.GONE);
    }

    public void forgetCheckPhonePresent(View view) {
        aProgressBar.setVisibility(View.VISIBLE);
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getApplicationContext())) {
            aProgressBar.setVisibility(View.GONE);
            showCustomDialog();
            return;
        }
        if (!validateFields()) {
            aProgressBar.setVisibility(View.GONE);
            return;
        }

        String _phoneNo = forgetPhoneNumber.getEditText().getText().toString().trim();
        if (_phoneNo.charAt(0) == '0') {
            _phoneNo = _phoneNo.substring(1);
        }
        final String _completePhone = "+" + forgetPasswordCountryCode.getSelectedCountryCode() + _phoneNo;

        //check if user exist
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhone);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", _completePhone);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();
                    aProgressBar.setVisibility(View.GONE);
                } else {
                    aProgressBar.setVisibility(View.GONE);
                    forgetPhoneNumber.setError("No such user exists!");
                    forgetPhoneNumber.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validateFields() {
        String _phoneNo = forgetPhoneNumber.getEditText().getText().toString().trim();
        String checkSpace = "\\A\\w{1,20}\\z";
        if (_phoneNo.isEmpty()) {
            forgetPhoneNumber.setError("Phone number can not be empty!");
            forgetPhoneNumber.requestFocus();
            return false;
        } else if (!_phoneNo.matches(checkSpace)) {
            forgetPhoneNumber.setError("No white space allowed!");
            forgetPhoneNumber.requestFocus();
            return false;
        } else {
            forgetPhoneNumber.setError(null);
            forgetPhoneNumber.setErrorEnabled(false);
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

    private boolean isConnected(ForgetPassword forgetPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificonn != null && wificonn.isConnected()) || (mobileconn != null && mobileconn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }
}