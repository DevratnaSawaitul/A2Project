package miniproject.codes.a2project.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import miniproject.codes.a2project.R;

public class SignUp2 extends AppCompatActivity {

    ImageView backBtn;
    Button next, login;
    TextView titleText;

    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2);
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);

        radioGroup = findViewById(R.id.signup_genderPicker);
        datePicker = findViewById(R.id.signup_dob);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void call3rdSignupScreen(View view) {
        if (!validateAge() | !validateGender()) {
            return;
        }
        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String gender = selectedGender.getText().toString().trim();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String date = day + "/" + month + "/" + year;
        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());


        Intent intent = new Intent(getApplicationContext(), SignUp3.class);
        intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
        intent.putExtra("clgRollNo", getIntent().getStringExtra("clgRollNo"));
        intent.putExtra("email", getIntent().getStringExtra("email"));
        intent.putExtra("password", getIntent().getStringExtra("password"));
        intent.putExtra("gender", selectedGender.getText().toString());
        intent.putExtra("date", date);

        //add transition
//        Pair[] pairs = new Pair[4];
//        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
//        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
//        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
//        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2.this, pairs);
//            startActivity(intent, options.toBundle());
//        } else {
        startActivity(intent);
//        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = Math.abs(currentYear - userAge);
        if (isAgeValid <= 15) {
            Toast.makeText(getApplicationContext(), "You are underAge", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}