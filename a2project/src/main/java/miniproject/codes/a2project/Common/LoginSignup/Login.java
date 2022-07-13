package miniproject.codes.a2project.Common.LoginSignup;

import androidx.annotation.NonNull;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.Databases.SessionManager;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class Login extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber, passWord;
    ProgressBar progressbar;
    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_country_code);
        phoneNumber = findViewById(R.id.login_phone_no);
        passWord = findViewById(R.id.login_password);
        progressbar = findViewById(R.id.login_progress_bar);
        aDialog = new Dialog(this);
        progressbar.setVisibility(View.INVISIBLE);

    }

    public void letUserLoggIn(View view) {

        //checks internet
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getApplicationContext())) {
            showCustomDialog();
            return;
        }

        if (!validateFields()) {
            return;
        }
        //get data
        progressbar.setVisibility(View.VISIBLE);
        String _phoneNo = phoneNumber.getEditText().getText().toString().trim();
        String _passWord = passWord.getEditText().getText().toString().trim();

        if (_phoneNo.charAt(0) == '0') {
            _phoneNo = _phoneNo.substring(1);
        }
        String _completePhoneNo = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNo;

        //Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_completePhoneNo).child("password").getValue(String.class);
                    if (systemPassword.equals(_passWord)) {
                        passWord.setError(null);
                        passWord.setErrorEnabled(false);
                        progressbar.setVisibility(View.GONE);

                        //to get some extra info
                        String fullName = snapshot.child(_completePhoneNo).child("fullName").getValue(String.class);
                        String userName = snapshot.child(_completePhoneNo).child("userName").getValue(String.class);
                        String gender = snapshot.child(_completePhoneNo).child("gender").getValue(String.class);
                        String email = snapshot.child(_completePhoneNo).child("email").getValue(String.class);
                        String clgRollNo = snapshot.child(_completePhoneNo).child("clgRollNo").getValue(String.class);
                        String password = snapshot.child(_completePhoneNo).child("password").getValue(String.class);
                        String date = snapshot.child(_completePhoneNo).child("date").getValue(String.class);
                        String phoneNo = snapshot.child(_completePhoneNo).child("phoneNo").getValue(String.class);


                        //Create a session
                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(fullName, userName, clgRollNo, email, phoneNo, password, date, gender);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                        //Toast.makeText(getApplicationContext(), fullName + "\n" + userName + "\n" + gender + "\n" + email + "\n" + clgRollNo, Toast.LENGTH_LONG).show();
                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No such user exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showCustomDialog() {

        aDialog.setContentView(R.layout.popup);
        aDialog.setCanceledOnTouchOutside(false);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close_dialog = aDialog.findViewById(R.id.close_cross);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), mainLoginActivity.class));
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

    //checks internet
    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificonn != null && wificonn.isConnected()) || (mobileconn != null && mobileconn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private boolean validateFields() {
        String _phoneNo = phoneNumber.getEditText().getText().toString().trim();
        String _passWord = passWord.getEditText().getText().toString().trim();
        String checkSpace = "\\A\\w{1,20}\\z";
        if (_phoneNo.isEmpty()) {
            phoneNumber.setError("Phone number can not be empty!");
            phoneNumber.requestFocus();
            return false;
        } else if (!_phoneNo.matches(checkSpace)) {
            phoneNumber.setError("No white space allowed!");
            phoneNumber.requestFocus();
            return false;
        } else if (_passWord.isEmpty()) {
            phoneNumber.setError("Password can not be empty!");
            phoneNumber.requestFocus();
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            passWord.setError(null);
            passWord.setErrorEnabled(false);
            return true;
        }
    }

    public void callCreateAccountActivity(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp1.class));
        finish();
    }

    public void CallForgetPasswordScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
        intent.putExtra("whatToDo", "updateData");
        startActivity(intent);
    }
}