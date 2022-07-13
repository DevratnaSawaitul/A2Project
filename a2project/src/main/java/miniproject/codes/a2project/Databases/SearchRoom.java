package miniproject.codes.a2project.Databases;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import miniproject.codes.a2project.HelperClasses.SeatAllotment;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class SearchRoom extends AppCompatActivity {
    TextView rollNo, found;
    LinearLayout buttonLayout;
    ProgressBar pb;
    String description = null, mapLink = null;
    Button descBtn, mapBtn;
    String curr_Room = null,Rollno=null;


    ImageView close_dialog;
    Button connect_Internet, showSeatAllotment;
    Dialog aDialog;
    TextView dialogText;

    NotificationManager aNotificationManager;
    public final String NOTIFICATION_ID = "1";
    public final String NOTIFICATION_NAME = "Get A Way";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search_room);
        buttonLayout = (LinearLayout) findViewById(R.id.lin_layount);
        showSeatAllotment = (Button) findViewById(R.id.seatAllotment);
        aDialog = new Dialog(this);
        Rollno = getIntent().getStringExtra("clgRollNo").toUpperCase();

        aNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        rollNo = findViewById(R.id.searchRoomTxt);
        pb = findViewById(R.id.pb_srch_room);
        found = findViewById(R.id.room_found);

        pb.setVisibility(View.VISIBLE);
        descBtn = (Button) findViewById(R.id.showDescription);
        mapBtn = (Button) findViewById(R.id.mapOpen);

        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getApplicationContext())) {
            showCustomDialog();
            return;
        }

        if (!Rollno.isEmpty()) {
            rollNo.setText("Your Rollno: " + Rollno);
            if (Rollno.charAt(2) == 'D' || Rollno.charAt(2) == 'T') {
                Rollno = Rollno.substring(0, 2) + Rollno.substring(3);
            }
            searchRoom(Rollno);
        }

        descBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description != null) {
                    aDialog.setContentView(R.layout.desc_popup);
                    aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialogText = aDialog.findViewById(R.id.ourText);
                    dialogText.setText(description);
                    connect_Internet = aDialog.findViewById(R.id.dismiss_btn);
                    connect_Internet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            aDialog.dismiss();
                        }
                    });
                    aDialog.show();
                } else {
                    Toast.makeText(SearchRoom.this, "Unable to get Room Description", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapLink != null && mapLink.length() > 0) {
//                    Toast.makeText(SearchRoom.this, mapLink, Toast.LENGTH_SHORT).show();
                    if (!mapLink.equals(null) && mapLink.length() > 0) {
                        Uri uri = Uri.parse(mapLink);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        Toast.makeText(view.getContext(), "Redirecting to Google Map", Toast.LENGTH_SHORT).show();
                        view.getContext().startActivity(intent);
                    }
                } else {
                    Toast.makeText(SearchRoom.this, "Unable to get Room Navigation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showSeatAllotment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SeatAllotment.class).putExtra("Room_no", curr_Room).putExtra("Roll_no",Rollno));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        //create channel req 3 things id,name,imp state
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        //everything we can set like show on lockscreen ,etc
        channel.enableVibration(true);
        aNotificationManager.createNotificationChannel(channel);
    }

    private void searchRoom(String _rollno) {
        FirebaseDatabase.getInstance().getReference("SetRooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFound = false;
                String val = null;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    for (DataSnapshot s : snap.getChildren()) {
//                        if (s.getValue(String.class).toUpperCase().equals(_rollno)) {
////                            Log.d("Room_no", snap.getKey().toString());
//                            pb.setVisibility(View.GONE);
//                            found.setText("Go to Room Number " + snap.getKey().toString());
//                            isFound = true;
//                        }
////                        Log.d("Dev", s.getValue(String.class));

                        String our = s.getValue(String.class).toUpperCase();
                        if (our.contains("-")) {
                            String[] parts = our.split("-");
                            if (parts[0].charAt(2) == 'D' || parts[0].charAt(2) == 'T') {
                                parts[0] = parts[0].substring(0, 2) + parts[0].substring(3);
                            }
                            if (parts[1].charAt(2) == 'D' || parts[1].charAt(2) == 'T') {
                                parts[1] = parts[1].substring(0, 2) + parts[1].substring(3);
                            }
                            if (parts[0].equals(_rollno) || parts[1].equals(_rollno)) {
                                val = snap.getKey().toString().toUpperCase();
                                isFound = true;
                                break;
                            } else if (parts[0].compareToIgnoreCase(_rollno) < 0 && parts[1].compareToIgnoreCase(_rollno) > 0) {
                                val = snap.getKey().toString().toUpperCase();
                                isFound = true;
                                break;
                            }
                        } else {
                            if (our.charAt(2) == 'D' || our.charAt(2) == 'T') {
                                our = our.substring(0, 2) + our.substring(3);
                            }
                            if (our.equals(_rollno)) {
                                val = snap.getKey().toString().toUpperCase();
                                isFound = true;
                                break;
                            }
                        }

                    }
                }
                if (isFound && !val.isEmpty()) {
                    pb.setVisibility(View.GONE);
                    found.setTextColor(Color.parseColor("#2196F3"));
                    found.setText("Go to Room Number " + val);
                    curr_Room = val;

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_ID);
                    builder.setContentTitle("Get A Way");
                    builder.setContentText("Your Examination Room Number is " + val);
                    builder.setSmallIcon(R.drawable.ic_baseline_follow_the_signs_24);
                    //to send notification;
                    aNotificationManager.notify(1, builder.build());//here 1 is position to show and we build the notification

                    try {
                        FirebaseDatabase.getInstance().getReference("room_Details").child(val).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                description = snapshot.child("address").getValue(String.class);
                                mapLink = snapshot.child("murl").getValue(String.class);
//                                if (description != null && mapLink != null)
//                                    Toast.makeText(SearchRoom.this, description + "\n" + mapLink, Toast.LENGTH_SHORT).show();
//                                else
//                                    Toast.makeText(SearchRoom.this, "sry we unable", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(SearchRoom.this, "Error occurred While getting Room Details", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(View.GONE);
                    buttonLayout.setVisibility(View.VISIBLE);
                    showSeatAllotment.setVisibility(View.VISIBLE);
                } else if (!isFound) {
                    pb.setVisibility(View.GONE);
                    buttonLayout.setVisibility(View.INVISIBLE);
                    showSeatAllotment.setVisibility(View.INVISIBLE);
                    found.setTextColor(Color.parseColor("#FF0000"));
                    found.setText("please check your Rollno OR\ncontact Department for more Information");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_ID);
                    builder.setContentTitle("Get A Way");
                    builder.setContentText("We're unable to get your examination room number, Please contact Department for more Information");
                    builder.setSmallIcon(R.drawable.ic_baseline_error_24);
                    //to send notification;
                    aNotificationManager.notify(1, builder.build());//here 1 is position to show and we build the notification
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}