package miniproject.codes.a2project.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import java.security.PublicKey;
import java.util.HashMap;

import miniproject.codes.a2project.Common.LoginSignup.mainLoginActivity;
import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.Databases.SearchRoom;
import miniproject.codes.a2project.Databases.SessionManager;
import miniproject.codes.a2project.EnterRollno;
import miniproject.codes.a2project.R;
import miniproject.codes.a2project.ShowRoomsActivity;

public class ScanFragment extends Fragment {
    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final String qr_text = "KITS,Ramtek";
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;

    SessionManager sessionManager;
    String clgRollNo;
    Button showAllRooms;


    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);
        scannerView = v.findViewById(R.id.scanner_view);
        aDialog = new Dialog(getContext());
        showAllRooms = v.findViewById(R.id.showAllRooms);
        sessionManager = new SessionManager(getContext());
        if (sessionManager.checkLogin()) {
            HashMap<String, String> usersDetails = sessionManager.getUsersDetailsFromSession();
            clgRollNo = usersDetails.get(SessionManager.KEY_CLGROLLNO);
        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA/*any permission we want to ask using , */
        };
        if (!hasPermission(getContext(), PERMISSIONS))
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        else {
            runCodeScanner();
        }

        showAllRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInternet checkInternet = new CheckInternet();
                if (!checkInternet.isConnected(getContext())) {
                    showCustomDialog();
                    return;
                }

                sessionManager = new SessionManager(getContext());
                if (sessionManager.checkLogin()) {
                    startActivity(new Intent(getContext(), ShowRoomsActivity.class));
                } else {
                    Toast.makeText(getContext(), "Login To Use This Feature", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void runCodeScanner() {
        codeScanner = new CodeScanner(getContext(), scannerView);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = result.getText();
                        if (data.toUpperCase().trim().equals(qr_text.toUpperCase().trim())) {
                            if (clgRollNo != null) {
                                Intent intent = new Intent(getContext(), SearchRoom.class);
                                intent.putExtra("clgRollNo", clgRollNo);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getContext(), EnterRollno.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getContext(), "Please Scan a Valid QR Code...", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ScanFragment()).commit();
                        }

                    }
                });
            }
        });
    }

    private void showCustomDialog() {

        aDialog.setContentView(R.layout.popup);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close_dialog = aDialog.findViewById(R.id.close_cross);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aDialog.dismiss();
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