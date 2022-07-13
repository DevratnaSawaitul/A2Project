package miniproject.codes.a2project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import miniproject.codes.a2project.Common.LoginSignup.mainLoginActivity;
import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.Databases.SearchRoom;

public class EnterRollno extends AppCompatActivity {
    Button seeRoom;
    TextInputLayout inputRollno;


    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_enter_rollno);

        seeRoom = findViewById(R.id.see_room);
        inputRollno = findViewById(R.id.input_rollno);

        aDialog = new Dialog(this);

        seeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateRollno()) {
                    return;
                }

                CheckInternet checkInternet = new CheckInternet();
                if (!checkInternet.isConnected(getApplicationContext())) {
                    showCustomDialog();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), SearchRoom.class);
                intent.putExtra("clgRollNo", inputRollno.getEditText().getText().toString().trim());

                startActivity(intent);

            }
        });
    }

    private boolean validateRollno() {
        String val = inputRollno.getEditText().getText().toString().trim();
        String checkRoll_no = "[A-Z]{2,4}[0-9]{5}$";
        if (val.isEmpty()) {
            inputRollno.setError("Field can not be empty");
            return false;

        } else if (!val.matches(checkRoll_no) || val.length() > 9 || val.length() < 7) {
            inputRollno.setError("Enter like CT19001, don't put T or D after branch");
            return false;
        } else {
            inputRollno.setError(null);
            inputRollno.setErrorEnabled(false);
            return true;
        }
    }

    private void showCustomDialog() {
        aDialog.setContentView(R.layout.popup);
        aDialog.setCanceledOnTouchOutside(false);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close_dialog = aDialog.findViewById(R.id.close_cross);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        connect_Internet = aDialog.findViewById(R.id.connect_internet);
        connect_Internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                aDialog.dismiss();
            }
        });
        aDialog.show();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}