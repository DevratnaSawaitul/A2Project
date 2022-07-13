package miniproject.codes.a2project.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import miniproject.codes.a2project.R;

public class SuccessLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_success_login);
    }

    public void goToLoginFromPassWordUpdate(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}