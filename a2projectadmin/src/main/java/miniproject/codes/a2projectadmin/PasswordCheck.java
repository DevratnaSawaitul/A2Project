package miniproject.codes.a2projectadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordCheck extends AppCompatActivity {
    TextInputLayout aEditText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_password_check);
        aEditText = findViewById(R.id.signup_password);
        btn = findViewById(R.id.btnCheck);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aEditText.getEditText().getText().toString().toUpperCase().trim().equals("Kits,Ramtek".toUpperCase())) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    aEditText.setError("Enter a valid password");
                }
            }
        });
    }
}