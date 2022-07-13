package miniproject.codes.a2project.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class SignUp1 extends AppCompatActivity {

    //variables
    ImageView backBtn;
    Button next, login;
    TextView titleText;
    TextInputLayout fullname, clgRollno, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);

        fullname = findViewById(R.id.signup_fullname);
        email = findViewById(R.id.signup_email);
        clgRollno = findViewById(R.id.signup_rollno);
        password = findViewById(R.id.signup_password);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void callNextSignupScreen(View view) {
        if (!validateFullName() | !validateRoll_no() | !validateEmail() | !validatePassword()) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), SignUp2.class);
        intent.putExtra("fullName", fullname.getEditText().getText().toString().trim());
        intent.putExtra("clgRollNo", clgRollno.getEditText().getText().toString().trim());
        intent.putExtra("email", email.getEditText().getText().toString().trim());
        intent.putExtra("password", password.getEditText().getText().toString().trim());

        //add transition
//        Pair[] pairs = new Pair[4];
//        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
//        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
//        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
//        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp1.this, pairs);
//            startActivity(intent, options.toBundle());
//        } else {
        startActivity(intent);
//        }
    }

    private boolean validateFullName() {
        String val = fullname.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullname.setError("Field can not be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

//    private boolean validateUsername() {
//        String val = username.getEditText().getText().toString().trim();
//        String checkSpace = "\\A\\w{1,20}\\z";//check a to z if any space between 20 char
//        if (val.isEmpty()) {
//            username.setError("Field can not be empty");
//            return false;
//        } else if (val.length() > 20) {
//            username.setError("Username is too large limit 20");
//            return false;
//        } else if (val.length() < 8) {
//            username.setError("Username is too short at least 8 character");
//            return false;
//        } else if (!val.matches(checkSpace)) {
//            username.setError("No white space are allowed");
//            return false;
//        } else {
//            username.setError(null);
//            username.setErrorEnabled(false);
//            return true;
//        }
//    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";//check a to z if any space between, @ in email char
        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRoll_no() {
        String val = clgRollno.getEditText().getText().toString().trim();
        String checkRoll_no = "[A-Z]{2,4}[0-9]{5}$";
        if (val.isEmpty()) {
            clgRollno.setError("Field can not be empty");
            return false;

        } else if (!val.matches(checkRoll_no) || val.length() > 9 || val.length() < 7) {
            clgRollno.setError("Enter like CT19001, don't put T or D after branch");
            return false;
        } else {
            clgRollno.setError(null);
            clgRollno.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
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
            password.setError("Field can no be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 4 characters! ");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}