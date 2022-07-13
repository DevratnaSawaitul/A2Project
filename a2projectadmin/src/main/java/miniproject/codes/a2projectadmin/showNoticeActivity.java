package miniproject.codes.a2projectadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class showNoticeActivity extends AppCompatActivity implements NoticeAdapter.OnItemClickListener {
    private RecyclerView aRecyclerView;
    private ArrayList<NoticeModal> list;

    private FirebaseStorage aStorage;

    private NoticeAdapter aNoticeAdapter;
    private ValueEventListener aDBListner;
    ProgressBar pr;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_notice);

        aRecyclerView = findViewById(R.id.recycler_view);
        aRecyclerView.setHasFixedSize(true);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        pr = findViewById(R.id.progressbar_notice);

        aNoticeAdapter = new NoticeAdapter(this, list);
        aNoticeAdapter.setOnItemClickListener(showNoticeActivity.this);
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
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                        DownloadManager downloadManager = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDeleteClick(int position) {
        NoticeModal selected = list.get(position);
        String selectedKey = selected.getKey();
        String selectedUrl = selected.getImageUrl();
//        Toast.makeText(getApplicationContext(), selectedKey + "\n" + selectedUrl, Toast.LENGTH_LONG).show();
        StorageReference imgRef = aStorage.getReferenceFromUrl(selectedUrl);
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                root.child(selectedKey).removeValue();
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        root.removeEventListener(aDBListner);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else
            Toast.makeText(getApplicationContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
    }
}