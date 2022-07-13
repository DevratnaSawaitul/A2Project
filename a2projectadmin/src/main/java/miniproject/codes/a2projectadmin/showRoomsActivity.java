package miniproject.codes.a2projectadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class showRoomsActivity extends AppCompatActivity {
    RecyclerView aRecyclerView;
    RoomAdapter aRoomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Room Details");
        setContentView(R.layout.activity_show_rooms);

        aRecyclerView = findViewById(R.id.rv);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
}