package miniproject.codes.a2project.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import miniproject.codes.a2project.Databases.UserHelperClass;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class VerifyOTP extends AppCompatActivity {

    PinView pinFromUser;
    String codeBySystem;

    TextView sendAgain, counter;

    String fullName, clgRollNo, email, password, gender, date, phoneNo, whatToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify_otp);
        pinFromUser = findViewById(R.id.pin_view);
        sendAgain = findViewById(R.id.again_otp);
        counter = findViewById(R.id.counter);

        fullName = getIntent().getStringExtra("fullName");
        clgRollNo = getIntent().getStringExtra("clgRollNo");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        date = getIntent().getStringExtra("date");
        phoneNo = getIntent().getStringExtra("phoneNo");
        whatToDo = getIntent().getStringExtra("whatToDo");

        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAgainOtp();
            }
        });
        sendVerificationCodeToUser(phoneNo);
    }

    private void startTimer() {
        sendAgain.setVisibility(View.GONE);
        counter.setVisibility(View.VISIBLE);
        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {

            @Override
            public void onTick(long l) {
                String aDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                counter.setText("resend OTP after " + aDuration);
            }

            @Override
            public void onFinish() {
                counter.setVisibility(View.GONE);
                sendAgain.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void goToHomeFromOTP(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp1.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        startTimer();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,//number to verify
                60,//timeout duration
                TimeUnit.SECONDS,//unit duration
                this,//activity for call binding
                mCallbacks//OnVerificationStateChangedCallbacks
        );
    }

    public void sendAgainOtp() {
        Toast.makeText(this, "Sending OTP on registered PhoneNo", Toast.LENGTH_SHORT).show();
        sendVerificationCodeToUser(phoneNo);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinFromUser.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInUsingCredential(credential);
    }

    private void signInUsingCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "Verification Completed", Toast.LENGTH_SHORT).show();
                    if (whatToDo != null && whatToDo.equals("updateData")) {
                        updateOldUserData();
                    } else {
                        storeNewUsersData();
                        Toast.makeText(getApplicationContext(), "Registered SuccessFully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getApplicationContext(), "Verification Not Completed! Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateOldUserData() {
        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class).putExtra("phoneNo", phoneNo);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
        finish();
    }


    //to store data in db
    private void storeNewUsersData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        //this will refer not to table but to node
        DatabaseReference reference = rootNode.getReference("Users");
        //in single line DatabaseReference reference=FirebaseDatabase.getInstance().getReference("name");

        fullName = fullName.substring(0, 1).toUpperCase() + fullName.substring(1);
        UserHelperClass addNewUser = new UserHelperClass(fullName, clgRollNo, email, password, gender, date, phoneNo);
        reference.child(phoneNo).setValue(addNewUser);
        Intent intent = new Intent(getApplicationContext(), SuccessLogin.class);
        startActivity(intent);
        finish();
    }

    public void callNextScreenFromOTP(View view) {
        Toast.makeText(this, "Verifying OTP please wait", Toast.LENGTH_SHORT).show();
        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
    }
}