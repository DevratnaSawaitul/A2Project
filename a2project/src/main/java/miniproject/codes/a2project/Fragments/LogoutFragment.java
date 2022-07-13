package miniproject.codes.a2project.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import miniproject.codes.a2project.Common.LoginSignup.mainLoginActivity;
import miniproject.codes.a2project.Databases.SessionManager;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;

public class LogoutFragment extends Fragment {
    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btn;
    TextView txtRollno, txtName;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_logout, container, false);


        txtRollno = v.findViewById(R.id.rollno_login);
        txtName = v.findViewById(R.id.name_login);
        btn = v.findViewById(R.id.logout_btn);

        sessionManager = new SessionManager(getContext());

        HashMap<String, String> usersDetails = sessionManager.getUsersDetailsFromSession();
        String clgRollNo = usersDetails.get(SessionManager.KEY_CLGROLLNO);
        String name = usersDetails.get(SessionManager.KEY_FULLNAME);

        //to capitalize first word
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        txtRollno.setText(clgRollNo);
        txtName.setText(name);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutFromSession();
                Toast.makeText(getContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new LoginFragment()).commit();
            }
        });
        return v;
    }
}