package miniproject.codes.a2projectadmin;

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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

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
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for this this dependency req implementation 'com.orhanobut:dialogplus:1.11@aar'
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.room_no.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
//                        .setExpanded(true, 1200)
                        .create();
                View view1 = dialogPlus.getHolderView();
                EditText room_no1 = view1.findViewById(R.id.update_txt_Room_no);
                EditText murl1 = view1.findViewById(R.id.update_txt_url);
                EditText address1 = view1.findViewById(R.id.update_txt_Address);
                Button btnUpdate = view1.findViewById(R.id.update_btn);

                room_no1.setText(model.getRoom_no());
                murl1.setText(model.getMurl());
                address1.setText(model.getAddress());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("room_no", room_no1.getText().toString());
                        map.put("murl", murl1.getText().toString());
                        map.put("address", address1.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("room_Details")
                                .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)

//                              .child(getRef(position).getKey()).updateChildren(map)

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.room_no.getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.room_no.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.room_no.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undo.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("room_Details")
                                .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(holder.name.getContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
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
        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            room_no = itemView.findViewById(R.id.Room_no_text);
            murl = itemView.findViewById(R.id.Roll_no_text);
            address = itemView.findViewById(R.id.address_text);

            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
