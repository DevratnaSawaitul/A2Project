package miniproject.codes.a2projectadmin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import miniproject.codes.a2projectadmin.R;

public class RollNoFragment extends Fragment {

    public RollNoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ProgressBar pb;
    WebView aWebView;
    FirebaseDatabase aFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference aDatabaseReference = aFirebaseDatabase.getReference();
    DatabaseReference childReference = aDatabaseReference.child("url");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_roll_no_fragment, container, false);
        aWebView = v.findViewById(R.id.web_view);
        pb = v.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        WebSettings webSettings = aWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        aWebView.setWebViewClient(new WebViewClient());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.getValue(String.class);
                aWebView.loadUrl(url);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Unable to load url", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * onbackpressed
     * if(webview.canGoBack()){
     * webview.goback();}
     * else{
     * super.onBackPressed();}*/
}