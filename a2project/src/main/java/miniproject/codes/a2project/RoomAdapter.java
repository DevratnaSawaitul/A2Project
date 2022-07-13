package miniproject.codes.a2project;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class RoomAdapter extends FirebaseRecyclerAdapter<RoomModal, RoomAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RoomAdapter(@NonNull FirebaseRecyclerOptions<RoomModal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RoomModal model) {
        holder.room_no.setText(model.getRoom_no());
        holder.murl.setText(model.getMurl());
        holder.address.setText(model.getAddress());


        String url = model.getMurl().trim();

        holder.murl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (!url.equals(null) && url.length() > 0)
                    cm.setText(url);
                else
                    cm.setText("");
                Toast.makeText(view.getContext(), "Copied :)", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        holder.murl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!url.equals(null) && url.length() > 0) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Toast.makeText(view.getContext(), "Redirecting to Google Map", Toast.LENGTH_SHORT).show();
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView room_no, murl, address;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            room_no = itemView.findViewById(R.id.Room_no_text);
            murl = itemView.findViewById(R.id.Roll_no_text);
            address = itemView.findViewById(R.id.address_text);
        }
    }
}
