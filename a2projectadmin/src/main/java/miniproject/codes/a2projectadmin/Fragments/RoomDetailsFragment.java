package miniproject.codes.a2projectadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import miniproject.codes.a2projectadmin.R;
import miniproject.codes.a2projectadmin.showRoomsActivity;

public class RoomDetailsFragment extends Fragment {

    public RoomDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText room_no, murl, address;
    Button btnAdd, showAllRooms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_room_details, container, false);
        room_no = v.findViewById(R.id.add_txt_Room_no);
        murl = v.findViewById(R.id.add_txt_url);
        address = v.findViewById(R.id.add_txt_Address);

        btnAdd = v.findViewById(R.id.add_btn);
        showAllRooms = v.findViewById(R.id.add_btn_show);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                String n1 = room_no.getText().toString().trim();
                String n2 = murl.getText().toString().trim();
                String n3 = address.getText().toString().trim();
                if (n1.length() > 0) {
                    insertData();
                } else {
                    Toast.makeText(getContext(), "Room number is mandatory ", Toast.LENGTH_LONG).show();
                }
            }
        });

        showAllRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), showRoomsActivity.class));
            }
        });

        return v;
    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        map.put("room_no", room_no.getText().toString().trim());
        map.put("murl", murl.getText().toString().trim());
        map.put("address", address.getText().toString().trim());

        String key = room_no.getText().toString().trim();

        FirebaseDatabase.getInstance().getReference().child("room_Details").child(key).setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        clearAll();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error While Insertion", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll() {
        room_no.setText("");
        address.setText("");
        murl.setText("");
    }
}