package miniproject.codes.a2project.HelperClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import miniproject.codes.a2project.R;

public class SeatAllotment extends AppCompatActivity {
    String room_no;
    String roll_no;
    CardView aCardView;
    ProgressBar pr;
    int count = 0, flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_seat_allotment);
        pr = (ProgressBar) findViewById(R.id.my_progress_bar);
        room_no = getIntent().getStringExtra("Room_no");
        roll_no = getIntent().getStringExtra("Roll_no");

        FirebaseDatabase.getInstance().getReference("SetRooms").child(room_no).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String s = snap.getKey();
                    s = s.substring(2).toUpperCase();
                    if (snap.getValue(String.class).contains("-")) {
                        String[] parts = snap.getValue(String.class).split("-");
                        if (parts[0].charAt(2) == 'D' || parts[0].charAt(2) == 'T') {
                            parts[0] = parts[0].substring(0, 2) + parts[0].substring(3);
                        }
                        if (parts[1].charAt(2) == 'D' || parts[1].charAt(2) == 'T') {
                            parts[1] = parts[1].substring(0, 2) + parts[1].substring(3);
                        }
                        FirebaseDatabase.getInstance().getReference("ClgRegister").child(s).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for (DataSnapshot sn : snapshot1.getChildren()) {
                                    String preRoll = sn.getValue(String.class).toUpperCase();
                                    if (preRoll.charAt(2) == 'T' || preRoll.charAt(2) == 'D') {
                                        preRoll = preRoll.substring(0, 2) + preRoll.substring(3);
                                    }
                                    if (preRoll.equals(parts[0])) {
                                        flag = 1;
                                    }
                                    if (flag > 0) {
                                        count++;
                                    }
                                    if (preRoll.equals(roll_no)) {
                                        showmsg();
                                        break;
                                    }
                                    if (preRoll.equals(parts[1])) {
                                        flag = 0;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error1) {
                                Toast.makeText(SeatAllotment.this, error1.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
//                        Toast.makeText(getApplicationContext(), s + " " + parts[0] + " " + parts[1], Toast.LENGTH_LONG).show();
                    } else {
                        String parts = snap.getValue(String.class).toUpperCase();
                        FirebaseDatabase.getInstance().getReference("ClgRegister").child(s).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for (DataSnapshot sn : snapshot1.getChildren()) {
                                    String preRoll = sn.getValue(String.class).toUpperCase();
                                    if (preRoll.charAt(2) == 'T' || preRoll.charAt(2) == 'D') {
                                        preRoll = preRoll.substring(0, 2) + preRoll.substring(3);
                                    }
                                    if (preRoll.equals(parts)) {
                                        count++;
                                        showmsg();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error1) {
                                Toast.makeText(SeatAllotment.this, error1.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SeatAllotment.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showmsg() {
//        Toast.makeText(getApplicationContext(),"Yo Yo "+count,Toast.LENGTH_SHORT).show();
        pr.setVisibility(View.INVISIBLE);
        if (count > 0 && count < 41) {
            switch (count) {
                case 1:
                    aCardView = (CardView) findViewById(R.id.t1);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 2:
                    aCardView = (CardView) findViewById(R.id.t2);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 3:
                    aCardView = (CardView) findViewById(R.id.t3);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 4:
                    aCardView = (CardView) findViewById(R.id.t4);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 5:
                    aCardView = (CardView) findViewById(R.id.t5);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 6:
                    aCardView = (CardView) findViewById(R.id.t6);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 7:
                    aCardView = (CardView) findViewById(R.id.t7);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 8:
                    aCardView = (CardView) findViewById(R.id.t8);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 9:
                    aCardView = (CardView) findViewById(R.id.t9);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 10:
                    aCardView = (CardView) findViewById(R.id.t10);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 11:
                    aCardView = (CardView) findViewById(R.id.t11);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 12:
                    aCardView = (CardView) findViewById(R.id.t12);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 13:
                    aCardView = (CardView) findViewById(R.id.t13);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 14:
                    aCardView = (CardView) findViewById(R.id.t14);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 15:
                    aCardView = (CardView) findViewById(R.id.t15);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 16:
                    aCardView = (CardView) findViewById(R.id.t16);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 17:
                    aCardView = (CardView) findViewById(R.id.t17);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 18:
                    aCardView = (CardView) findViewById(R.id.t18);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 19:
                    aCardView = (CardView) findViewById(R.id.t19);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 20:
                    aCardView = (CardView) findViewById(R.id.t20);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 21:
                    aCardView = (CardView) findViewById(R.id.t21);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 22:
                    aCardView = (CardView) findViewById(R.id.t22);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 23:
                    aCardView = (CardView) findViewById(R.id.t23);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 24:
                    aCardView = (CardView) findViewById(R.id.t24);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 25:
                    aCardView = (CardView) findViewById(R.id.t25);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 26:
                    aCardView = (CardView) findViewById(R.id.t26);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 27:
                    aCardView = (CardView) findViewById(R.id.t27);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 28:
                    aCardView = (CardView) findViewById(R.id.t28);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 29:
                    aCardView = (CardView) findViewById(R.id.t29);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 30:
                    aCardView = (CardView) findViewById(R.id.t30);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 31:
                    aCardView = (CardView) findViewById(R.id.t31);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 32:
                    aCardView = (CardView) findViewById(R.id.t32);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 33:
                    aCardView = (CardView) findViewById(R.id.t33);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 34:
                    aCardView = (CardView) findViewById(R.id.t34);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 35:
                    aCardView = (CardView) findViewById(R.id.t35);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 36:
                    aCardView = (CardView) findViewById(R.id.t36);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 37:
                    aCardView = (CardView) findViewById(R.id.t37);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 38:
                    aCardView = (CardView) findViewById(R.id.t38);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 39:
                    aCardView = (CardView) findViewById(R.id.t39);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                case 40:
                    aCardView = (CardView) findViewById(R.id.t40);
                    aCardView.setBackgroundColor(Color.RED);
                    break;
                default:
                    break;
            }
        }
    }
}