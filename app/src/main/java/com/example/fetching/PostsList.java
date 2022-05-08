package com.example.fetching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostsList extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    FloatingActionButton btnAdd;
    DatabaseReference database ;
    MyAdapter myAdapter ;
    ArrayList<Post> list ;
    private FirebaseStorage mStorage ;
    private StorageReference storageReference1;
    private ValueEventListener mDBListener;
    public Boolean idAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);




        // hide the Action Bar




        setContentView(R.layout.activity_posts_list);
        getSupportActionBar().hide();

        mStorage = FirebaseStorage.getInstance();
        storageReference1 = mStorage.getReference();
        btnAdd= findViewById(R.id.btnAddPost);
        recyclerView = findViewById(R.id.postsList);

        database = FirebaseDatabase.getInstance().getReference("Posts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        String userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile!= null){
                    if (userProfile.isAdmin.equals("1")){
                        btnAdd.setVisibility(View.VISIBLE);
                        idAdmin = true;

                    } else {
                        btnAdd.setVisibility(View.INVISIBLE);
                        idAdmin = false;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            }
        });

        list = new ArrayList<>();

        myAdapter = new MyAdapter(PostsList.this , list);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(PostsList.this);
        mStorage = FirebaseStorage.getInstance();





        database = FirebaseDatabase.getInstance().getReference("Posts");
        mDBListener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                Post post = dataSnapshot.getValue(Post.class);
                post.setKey(dataSnapshot.getKey());
                list.add(post);
            }

            myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void OnItemClick(int position) {
//        Toast.makeText(getApplicationContext(), "Normal Click At Position : " + position, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void OnDeleteClick(int position) {
        if (idAdmin==true) {


        Post selectedItem = list.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageURl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.child(selectedKey).removeValue();
                Toast.makeText(getApplicationContext(), "Post Deleted", Toast.LENGTH_SHORT).show();

            }
        });
    }
    else {
            startActivity(new Intent(getApplicationContext(), PostsList.class));
            Toast.makeText(getApplicationContext(), "You Don't Have Permission To Delete Posts", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.removeEventListener(mDBListener);
    }
}