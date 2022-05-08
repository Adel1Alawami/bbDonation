package com.example.fetching;

import android.content.Context;
import android.net.Uri;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private OnItemClickListener mListener;

    public FirebaseAuth mAuth;
    Context context ;
    ArrayList<Post> list ;


    public MyAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = list.get(position);
        holder.subject.setText(post.getSubject());
        holder.bloodtyperequired.setText(post.getBloodtypeReq());
        holder.info.setText(post.getInfo()); // things changed
        holder.date.setText(post.getDate());
        Picasso.get().load(post.getImageURl()).resize(1920 , 980)
                .into(holder.imageView);
        System.out.println("Image path"+post.getImageURl());
        System.out.println("post image" + post.getImageURl());
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser post1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts").child("picKey");
        DatabaseReference pickeyString = FirebaseDatabase.getInstance().getReference("Posts").child(post1.getUid()).child("picKey");

        System.out.println( " pick key string " + pickeyString);
        Uri postID = post1.getPhotoUrl();
        FirebaseStorage storage = FirebaseStorage.getInstance().getReference().getStorage();
        reference.getDatabase().getReference("images/");
        System.out.println("here it is " +postID);
        System.out.println("referebce " + reference);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,
            View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {

    public     TextView subject , bloodtyperequired , info, date;
         ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.tvsubject);
            bloodtyperequired = itemView.findViewById(R.id.tvBTRequired);
            info = itemView.findViewById(R.id.tvInfo);
            date = itemView.findViewById(R.id.tvDate);
            imageView= itemView.findViewById(R.id.imageViewV);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mListener != null) {

                int position = getAdapterPosition();
                if (position!= RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");

            // U Can Add More If u Want

            MenuItem delete = menu.add(Menu.NONE, 1 ,1 , "Delete");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {

                int position = getAdapterPosition();
                if (position!= RecyclerView.NO_POSITION){

                    switch ( item.getItemId()) {

                        case 1:
                            mListener.OnDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick (int position);

        void OnDeleteClick (int position);

    }
    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;

    };

}
