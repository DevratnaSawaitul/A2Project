package miniproject.codes.a2project.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 3000;

    ImageView backgroundImage, bg;
    TextView powerByLine;

    //animation
    Animation sideAnim, bottomAnim;

    //shared preference to avoid viewing On Boarding page multiple times
    SharedPreferences sharedPreferencesForOnBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        backgroundImage = findViewById(R.id.background_image);
        powerByLine = findViewById(R.id.powered_by_line);
        bg = findViewById(R.id.bg);

        //animations
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //set animation
        bg.setAnimation(bottomAnim);
        backgroundImage.setAnimation(sideAnim);
        powerByLine.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            sharedPreferencesForOnBoardingScreen = getSharedPreferences("sharedPreferencesForOnBoardingScreen", MODE_PRIVATE);

            boolean isFirstTime = sharedPreferencesForOnBoardingScreen.getBoolean("firstTime", true);
            //here if first time than this "firstTime" variable will automatically created;

            if (isFirstTime) {

                SharedPreferences.Editor editor = sharedPreferencesForOnBoardingScreen.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();

                startActivity(new Intent(getApplicationContext(), OnBoarding.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, SPLASH_TIMER);
    }
}