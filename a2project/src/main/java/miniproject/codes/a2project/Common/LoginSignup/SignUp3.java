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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.R;

public class SignUp3 extends AppCompatActivity {

    ImageView backBtn;
    Button login;
    TextView titleText;
    boolean found = false;
    TextInputLayout phoneNo;
    CountryCodePicker aCountryCodePicker;

    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3);
        backBtn = findViewById(R.id.signup_back_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);


        aDialog = new Dialog(this);

        phoneNo = findViewById(R.id.signup_phone_no);
        aCountryCodePicker = (CountryCodePicker) findViewById(R.id.signup_country_code);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void callOTPSignupScreen(View view) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getApplicationContext())) {
            showCustomDialog();
            return;
        }

        if (!validatePhone()) {
            return;
        }

        String getUserEnteredPhoneNumber = phoneNo.getEditText().getText().toString().trim();
        String _phoneNo = "+" + aCountryCodePicker.getSelectedCountryCode() + getUserEnteredPhoneNumber;

//        if (checkIfAlreadyPresent(_phoneNo)) {
//            phoneNo.setError("Phone Number Already in Use Try another Phone Number");
//            return;
//        } else {
//            phoneNo.setError(null);
//        }

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
        intent.putExtra("clgRollNo", getIntent().getStringExtra("clgRollNo"));
        intent.putExtra("email", getIntent().getStringExtra("email"));
        intent.putExtra("password", getIntent().getStringExtra("password"));
        intent.putExtra("gender", getIntent().getStringExtra("gender"));
        intent.putExtra("date", getIntent().getStringExtra("date"));
        intent.putExtra("phoneNo", _phoneNo);

//        add transition
//        Pair[] pairs = new Pair[4];
//        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
//        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
//        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
//        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3.this, pairs);
//            startActivity(intent, options.toBundle());
//        } else {
        startActivity(intent);
//        }
    }

    private boolean checkIfAlreadyPresent(String __phoneNo) {
        found = false;

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.getKey().toString().equals(__phoneNo)) {
                        Toast.makeText(SignUp3.this, "Phone Number already in DataBase", Toast.LENGTH_SHORT).show();
                        found = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp3.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (found) {
            return true;
        }
        return false;

    }

    private boolean userAlreadyExists(String phoneNo) {
        Query checkUserExists = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(phoneNo);
        final boolean[] Present = new boolean[1];
        checkUserExists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Present[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Pls try again later", Toast.LENGTH_SHORT).show();
            }
        });
        return Present[0];
    }

    private boolean validatePhone() {
        String val = phoneNo.getEditText().getText().toString().trim();
        String checkSpace = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNo.setError("Enter Valid phone number");
            return false;
        } else if (!val.matches(checkSpace)) {
            phoneNo.setError("No white space allowed!");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}