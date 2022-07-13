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

import java.util.Locale;

public class ShowUsersActivity extends AppCompatActivity {

    RecyclerView aRecyclerView;
    UsersAdapter aUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        getSupportActionBar().setTitle("Users");
        aRecyclerView = (RecyclerView) findViewById(R.id.user_rec_view);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<UserHelperClass> options = new FirebaseRecyclerOptions.Builder<UserHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), UserHelperClass.class)
                .build();
        aUsersAdapter = new UsersAdapter(options);
        aRecyclerView.setAdapter(aUsersAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        aUsersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        aUsersAdapter.startListening();
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
        if (str.length() > 1) {
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        } else if (str.length() == 1) {
            str = str.substring(0, 1).toUpperCase();
        }
        FirebaseRecyclerOptions<UserHelperClass> options = new FirebaseRecyclerOptions.Builder<UserHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("fullName").startAt(str).endAt(str + "~"), UserHelperClass.class)
                .build();
        aUsersAdapter = new UsersAdapter(options);
        aUsersAdapter.startListening();
        aRecyclerView.setAdapter(aUsersAdapter);

    }
}