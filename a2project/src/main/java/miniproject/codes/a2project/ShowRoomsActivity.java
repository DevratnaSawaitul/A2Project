package miniproject.codes.a2project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import miniproject.codes.a2project.Databases.CheckInternet;

public class ShowRoomsActivity extends AppCompatActivity {

    RecyclerView aRecyclerView;
    RoomAdapter aRoomAdapter;
    ImageView close_dialog;
    Button connect_Internet;
    Dialog aDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Rooms");
        setContentView(R.layout.activity_show_rooms);

        aRecyclerView = findViewById(R.id.rv);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        aDialog = new Dialog(this);
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(getApplicationContext())) {
            showCustomDialog();
            return;
        }

        FirebaseRecyclerOptions<RoomModal> options = new FirebaseRecyclerOptions.Builder<RoomModal>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("room_Details"), RoomModal.class)
                .build();

        aRoomAdapter = new RoomAdapter(options);
        aRecyclerView.setAdapter(aRoomAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        aRoomAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        aRoomAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<RoomModal> options = new FirebaseRecyclerOptions.Builder<RoomModal>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("room_Details").orderByChild("room_no").startAt(str).endAt(str + "~"), RoomModal.class)
                .build();
        aRoomAdapter = new RoomAdapter(options);
        aRoomAdapter.startListening();
        aRecyclerView.setAdapter(aRoomAdapter);

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