package miniproject.codes.a2project.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import miniproject.codes.a2project.Common.LoginSignup.Login;
import miniproject.codes.a2project.Common.LoginSignup.SignUp1;
import miniproject.codes.a2project.Common.LoginSignup.mainLoginActivity;
import miniproject.codes.a2project.Databases.SessionManager;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.R;


public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button login, signup;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        login = (Button) v.findViewById(R.id.login_btn);
        signup = (Button) v.findViewById(R.id.signup_btn);

        sessionManager = new SessionManager(getContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignUp1.class));
                getActivity().finish();
            }
        });
        return v;
    }
}