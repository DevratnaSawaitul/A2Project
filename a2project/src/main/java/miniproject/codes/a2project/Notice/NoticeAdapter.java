package miniproject.codes.a2project.Notice;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import miniproject.codes.a2project.R;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {
    private ArrayList<NoticeModal> mList;
    private Context context;

    private OnItemClickListener aListener;

    public NoticeAdapter(Context context, ArrayList<NoticeModal> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(mList.get(position)
                .getImageUrl())
//                .placeholder(R.drawable.ic_baseline_image_24)
                .into(holder.aImageView);
        holder.aTextView.setText("On: " + mList.get(position).getOn());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView aImageView;
        TextView aTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            aImageView = itemView.findViewById(R.id.m_image);
            aTextView = itemView.findViewById(R.id.item_text);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (aListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (menuItem.getItemId()) {
                        case 1:
                            aListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            aListener.onShareClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (aListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    aListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Download");
            MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Share");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onShareClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        aListener = itemClickListener;
    }

}
