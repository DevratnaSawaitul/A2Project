package miniproject.codes.a2project.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

import miniproject.codes.a2project.Databases.CheckInternet;
import miniproject.codes.a2project.MainActivity;
import miniproject.codes.a2project.Notice.NoticeAdapter;
import miniproject.codes.a2project.Notice.NoticeModal;
import miniproject.codes.a2project.R;

public class NoticeFragment extends Fragment implements NoticeAdapter.OnItemClickListener {

    public NoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView aRecyclerView;
    private ArrayList<NoticeModal> list;

    private FirebaseStorage aStorage;

    private NoticeAdapter aNoticeAdapter;
    private ValueEventListener aDBListner;
    ProgressBar pr;

    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Images");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notice, container, false);

        aRecyclerView = v.findViewById(R.id.recycler_view);
        aRecyclerView.setHasFixedSize(true);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        pr = v.findViewById(R.id.progressbar_notice);

        aNoticeAdapter = new NoticeAdapter(getContext(), list);
        aNoticeAdapter.setOnItemClickListener(NoticeFragment.this);

        aDialog = new Dialog(getContext());

        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getContext())) {
            showCustomDialog();
            return v;
        }

        aRecyclerView.setAdapter(aNoticeAdapter);
        aStorage = FirebaseStorage.getInstance();

        aDBListner = root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pr.setVisibility(View.VISIBLE);
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoticeModal noticeModal = dataSnapshot.getValue(NoticeModal.class);
                    noticeModal.setKey(dataSnapshot.getKey());
                    list.add(noticeModal);
                }
                Collections.reverse(list);
                aNoticeAdapter.notifyDataSetChanged();
                pr.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {
        NoticeModal selected = list.get(position);
        String selectedKey = selected.getKey();
        String selectedUrl = selected.getImageUrl();
        aStorage.getReferenceFromUrl(selectedUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                Log.d("our url is", "Download url is " + uri.toString());
                if (checkAndRequestPermissions() == true) {
                    try {
                        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(getContext().DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                        request.setTitle("Download " + selected.getOn() + ".jpg");
                        request.setDescription("wait while image get downloaded");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/images/" + "/" + selected.getOn() + ".jpg");
                        request.setMimeType("*/*");
                        downloadManager.enqueue(request);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
        });
    }

    @Override
    public void onShareClick(int position) {
        NoticeModal selected = list.get(position);
        String selectedKey = selected.getKey();
        String selectedUrl = selected.getImageUrl();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this new Notice\n\n" + selectedUrl + "\n\n-Get a Way");
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else
            Toast.makeText(getContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
    }

    private void showCustomDialog() {
        aDialog.setContentView(R.layout.popup);
        aDialog.setCanceledOnTouchOutside(false);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close_dialog = aDialog.findViewById(R.id.close_cross);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
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