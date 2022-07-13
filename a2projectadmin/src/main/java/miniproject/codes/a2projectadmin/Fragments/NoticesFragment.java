package miniproject.codes.a2projectadmin.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import miniproject.codes.a2projectadmin.NoticeModal;
import miniproject.codes.a2projectadmin.R;
import miniproject.codes.a2projectadmin.showNoticeActivity;

public class NoticesFragment extends Fragment {


    public NoticesFragment() {
        // Required empty public constructor
    }


    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
    private Button uploadBtn, showAllBtn;
    private ImageView imageView;
    private ProgressBar progressBar;
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Images");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    //to avoid same photo multiple time
    private StorageTask aUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notices, container, false);

        uploadBtn = v.findViewById(R.id.upload_btn);
        showAllBtn = v.findViewById(R.id.show_all_notices);
        imageView = v.findViewById(R.id.imageView_notices);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aUploadTask != null && aUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(getContext(), "No File Selected", Toast.LENGTH_SHORT).show();
                } else {
                    uploadToFirebase(imageUri);
                }
            }
        });
        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), showNoticeActivity.class));
            }
        });
        return v;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadToFirebase(Uri uri) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        aUploadTask = fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.INVISIBLE);
                        NoticeModal noticeModal = new NoticeModal(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(noticeModal);
                        Toast.makeText(getContext(), "Uploaded SuccessFully!", Toast.LENGTH_SHORT).show();
                        imageUri = null;
                        imageView.setImageResource(R.drawable.ic_baseline_image_search_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }
}