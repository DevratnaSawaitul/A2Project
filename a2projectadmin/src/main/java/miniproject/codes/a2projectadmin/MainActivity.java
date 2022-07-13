package miniproject.codes.a2projectadmin;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import miniproject.codes.a2projectadmin.Fragments.NoticesFragment;
import miniproject.codes.a2projectadmin.Fragments.RollNoFragment;
import miniproject.codes.a2projectadmin.Fragments.RoomDetailsFragment;
import miniproject.codes.a2projectadmin.Fragments.Users_Fragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.nav_exam);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new RoomDetailsFragment()).commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_exam:
                        getSupportActionBar().hide();
                        fragment = new RoomDetailsFragment();
                        break;
                    case R.id.nav_notice:
                        getSupportActionBar().hide();
                        fragment = new NoticesFragment();
                        break;
                    case R.id.nav_roolno:
                        getSupportActionBar().hide();
                        fragment = new RollNoFragment();
                        break;
                    case R.id.nav_users:
                        getSupportActionBar().setTitle("Users");
                        fragment = new Users_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });
    }
}