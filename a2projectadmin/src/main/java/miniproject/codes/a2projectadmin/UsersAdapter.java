package miniproject.codes.a2projectadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Locale;
import java.util.Map;

public class UsersAdapter extends FirebaseRecyclerAdapter<UserHelperClass, UsersAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UsersAdapter(@NonNull FirebaseRecyclerOptions<UserHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull UserHelperClass model) {
        holder.fullName.setText(model.getFullName());
        holder.clgRollNo.setText(model.getClgRollNo());
        holder.email.setText(model.getEmail());
        holder.phoneNo.setText(model.getPhoneNo());
        holder.gender.setText(model.getGender());
        holder.date.setText(model.getDate());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for this this dependency req implementation 'com.orhanobut:dialogplus:1.11@aar'
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.fullName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.users_update_popup))
//                        .setExpanded(true, 1200)
                        .create();
                View view1 = dialogPlus.getHolderView();
                EditText fullName1 = view1.findViewById(R.id.update_txt_fullname1);
                EditText clgRollNo1 = view1.findViewById(R.id.update_txt_clg_rollNo1);
                EditText email1 = view1.findViewById(R.id.update_txt_email1);
                EditText gender1 = view1.findViewById(R.id.update_txt_gender1);
                EditText date1 = view1.findViewById(R.id.update_txt_date1);
                EditText phoneNo1 = view1.findViewById(R.id.update_txt_phoneNo1);

                Button btnUpdate = view1.findViewById(R.id.users_update_btn);

                fullName1.setText(model.getFullName());
                clgRollNo1.setText(model.getClgRollNo());
                email1.setText(model.getEmail());
                gender1.setText(model.getGender());
                date1.setText(model.getDate());
                phoneNo1.setText(model.getPhoneNo());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("fullName", fullName1.getText().toString());
                        map.put("clgRollNo", clgRollNo1.getText().toString().toUpperCase());
                        map.put("email", email1.getText().toString());
                        map.put("gender", gender1.getText().toString());
                        map.put("date", date1.getText().toString());
                        map.put("phoneNo", phoneNo1.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)

//                              .child(getRef(position).getKey()).updateChildren(map)

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.fullName.getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.fullName.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.fullName.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undo.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Users")
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        Button update, delete;
        TextView fullName, clgRollNo, email, gender, date, phoneNo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.full_name_txt);
            clgRollNo = (TextView) itemView.findViewById(R.id.roll_no_text);
            email = (TextView) itemView.findViewById(R.id.email_text);
            gender = (TextView) itemView.findViewById(R.id.gender_text);
            date = (TextView) itemView.findViewById(R.id.date_text);
            phoneNo = (TextView) itemView.findViewById(R.id.phone_no_text);

            update = (Button) itemView.findViewById(R.id.btn_edit);
            delete = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }
}