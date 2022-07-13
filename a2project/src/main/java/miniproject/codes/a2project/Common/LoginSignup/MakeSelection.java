package miniproject.codes.a2project.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import miniproject.codes.a2project.R;

public class MakeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_make_selection);
    }
}