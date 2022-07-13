package miniproject.codes.a2project.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class mainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_login);
        //  login = findViewById(R.id.login_btn);
        //  signup = findViewById(R.id.signup_btn);
    }

    public void callLoginScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);

        //animation
//        Pair[] pairs = new Pair[1];
//        pairs[0] = new Pair<View, String>(findViewById(R.id.login_btn), "transition_login");
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mainLoginActivity.this, pairs);
//            startActivity(intent, options.toBundle());
//        } else {
        startActivity(intent);
//      }
    }

    public void callSignupScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp1.class);

        //animation
//        Pair[] pairs = new Pair[1];
//        pairs[0] = new Pair<View, String>(findViewById(R.id.login_btn), "transition_signup");
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mainLoginActivity.this, pairs);
//            startActivity(intent, options.toBundle());
//        } else {
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
//        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}