package miniproject.codes.a2project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.HashMap;

import miniproject.codes.a2project.Common.LoginSignup.mainLoginActivity;
import miniproject.codes.a2project.Databases.SearchRoom;
import miniproject.codes.a2project.Databases.SessionManager;
import miniproject.codes.a2project.Fragments.LoginFragment;
import miniproject.codes.a2project.Fragments.LogoutFragment;
import miniproject.codes.a2project.Fragments.NoticeFragment;
import miniproject.codes.a2project.Fragments.ScanFragment;

public class MainActivity extends AppCompatActivity {
    //    private CodeScanner codeScanner;
//    private CodeScannerView scannerView;
//    TextView loginStatus;
//    SessionManager sessionManager;
//    String fullName, userName, clgRollNo, email, phoneNo, password, date, gender;
//    ImageView loginBtn, logoutBtn;
//
//
//    ImageView close_dialog;
//    Button searchRollno;
//    EditText inputRollno;
//    TextView showBranch;
//    Dialog aDialog;
//    Button connect_Internet;
    BottomNavigationView navigationView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        scannerView = findViewById(R.id.scanner_view);
//        loginStatus = findViewById(R.id.main_login_status);
//        loginBtn = findViewById(R.id.if_login_btn);
//        logoutBtn = findViewById(R.id.if_logout_btn);
//
//        aDialog = new Dialog(this);
//
//        //to know user logged in or not also getting data if logged in
//        sessionManager = new SessionManager(getApplicationContext());
//        if (sessionManager.checkLogin()) {
//            HashMap<String, String> usersDetails = sessionManager.getUsersDetailsFromSession();
//            fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
//            userName = usersDetails.get(SessionManager.KEY_USERNAME);
//            clgRollNo = usersDetails.get(SessionManager.KEY_CLGROLLNO);
//            email = usersDetails.get(SessionManager.KEY_EMAIL);
//            phoneNo = usersDetails.get(SessionManager.KEY_PHONENUMBER);
//            password = usersDetails.get(SessionManager.KEY_PASSWORD);
//            date = usersDetails.get(SessionManager.KEY_DATE);
//            gender = usersDetails.get(SessionManager.KEY_GENDER);
//
//            loginBtn.setVisibility(View.INVISIBLE);
//            logoutBtn.setVisibility(View.VISIBLE);
//            loginStatus.setText("abhi log in ho aap");
//
//        } else {
//            logoutBtn.setVisibility(View.INVISIBLE);
//            loginBtn.setVisibility(View.VISIBLE);
//            loginStatus.setText("log in karo aap ");
//        }
//
//        int PERMISSION_ALL = 1;
//        String[] PERMISSIONS = {
//                android.Manifest.permission.CAMERA/*any permission we want to ask using , */
//        };
//        if (!hasPermission(this, PERMISSIONS))
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        else {
//            runCodeScanner();
//        }

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.nav_exam);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ScanFragment()).commit();

        sessionManager = new SessionManager(getApplicationContext());

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_exam:
                        fragment = new ScanFragment();
                        break;
                    case R.id.nav_notice:
                        fragment = new NoticeFragment();
                        break;
                    case R.id.nav_login:
                        if (sessionManager.checkLogin()) {
                            fragment = new LogoutFragment();
                        } else {
                            fragment = new LoginFragment();
                        }
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });
//        FirebaseDatabase.getInstance().getReference("room_Details").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snap : snapshot.getChildren()) {
//                    for (DataSnapshot s : snap.getChildren()) {
//                        if (s.getValue(String.class).equals("CT19001")) {
//                            Log.d("Room_no", snap.getKey().toString());
//                        }
//                        Log.d("Dev", s.getValue(String.class));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

//    private void showCustomDialog(String branch) {
//        inputRollno = aDialog.findViewById(R.id.input_rollno);
//        showBranch = aDialog.findViewById(R.id.our_branch);
//        showBranch.setText(branch);
//
//        aDialog.setContentView(R.layout.rollno_popup);
//        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        close_dialog = aDialog.findViewById(R.id.close_cross);
//        close_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                aDialog.dismiss();
//            }
//        });
//
//        searchRollno = aDialog.findViewById(R.id.search_roll_no);
//        searchRollno.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String roll = inputRollno.getText().toString().trim();
////                if (validRollno(roll)) {
//                Intent intent = new Intent(getApplicationContext(), SearchRoom.class);
//                intent.putExtra("clgRollNo", branch + "" + roll);
//                aDialog.dismiss();
//                startActivity(intent);
////                } else {
////                    Toast.makeText(getApplicationContext(), "put Valid Roll Number", Toast.LENGTH_SHORT).show();
////                    return;
////                }
//            }
//        });
//        aDialog.show();
//    }

//    private void showCustomDialog() {
//
//        aDialog.setContentView(R.layout.popup);
//        aDialog.setCanceledOnTouchOutside(false);
//        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        close_dialog = aDialog.findViewById(R.id.close_cross);
//        close_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), mainLoginActivity.class));
//                finish();
//            }
//        });
//
//        connect_Internet = aDialog.findViewById(R.id.connect_internet);
//        connect_Internet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                aDialog.dismiss();
//            }
//        });
//        aDialog.show();
//    }
}