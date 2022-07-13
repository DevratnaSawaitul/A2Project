package miniproject.codes.a2projectadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import miniproject.codes.a2projectadmin.R;
import miniproject.codes.a2projectadmin.ShowUsersActivity;
import miniproject.codes.a2projectadmin.UserHelperClass;
import miniproject.codes.a2projectadmin.UsersAdapter;

public class Users_Fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button showAll, add;
    EditText fullName, clgRollNo, email, gender, date, phoneNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users_, container, false);
        showAll = v.findViewById(R.id.add_user_btn_show);
        add = v.findViewById(R.id.add_user_btn);

        fullName = v.findViewById(R.id.txt_fullname);
        clgRollNo = v.findViewById(R.id.txt_clg_rollNo);
        email = v.findViewById(R.id.txt_email);
        gender = v.findViewById(R.id.txt_gender);
        date = v.findViewById(R.id.txt_date);
        phoneNo = v.findViewById(R.id.txt_phoneNo);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n1 = fullName.getText().toString().trim();
                String n2 = clgRollNo.getText().toString().trim();
                String n3 = email.getText().toString().trim();
                String n4 = gender.getText().toString().trim();
                String n5 = date.getText().toString().trim();
                String n6 = phoneNo.getText().toString().trim();

                if (n1.length() > 0 && n2.length() > 0 && n6.length() > 0) {
                    insertData();
                } else {
                    Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ShowUsersActivity.class));
            }
        });
        return v;
    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        String name = fullName.getText().toString().trim();
        if (name.length() > 1)
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        else if (name.length() == 1)
            name = name.substring(0, 1).toUpperCase();
        map.put("fullName", name);
        map.put("clgRollNo", clgRollNo.getText().toString().trim());
        map.put("email", email.getText().toString().trim());
        map.put("gender", gender.getText().toString().trim());
        map.put("date", date.getText().toString().trim());
        map.put("phoneNo", "+91" + phoneNo.getText().toString().trim());

        String key = phoneNo.getText().toString().trim();

        FirebaseDatabase.getInstance().getReference().child("Users").child(key).setValue(map)
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
        fullName.setText("");
        clgRollNo.setText("");
        email.setText("");
        phoneNo.setText("");
        gender.setText("");
        date.setText("");
    }

}