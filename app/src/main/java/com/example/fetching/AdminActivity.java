package com.example.fetching;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
public EditText post, bloodTypeReqAd , subjectAd  ;
public EditText infoAd ;
public Button publish , GotoDashboard ;
private ImageView postPic;
public Uri imageUri ;
private FirebaseStorage storage ;
private StorageReference  storageReference;
Date currentTime = Calendar.getInstance().getTime();
public String picKey ;
public URL keykey;



    public  String FileLink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hid
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        Objects.requireNonNull(getSupportActionBar()).hide();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth= FirebaseAuth.getInstance();
        bloodTypeReqAd= findViewById(R.id.bloodTypeReqAd);
        subjectAd = findViewById(R.id.subjectAdmin);
        publish = findViewById(R.id.btn_Publish);
        postPic= findViewById(R.id.postImage);
        infoAd = findViewById(R.id.infoAdmin);
        GotoDashboard = findViewById(R.id.btn_gotoDashboard);



        // The button That Goes To Dashboard From AdminA
        GotoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        String userID = user.getUid();





        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        postPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

publish.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AddPost();

    }
});
    }










    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent ,1);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 || resultCode == RESULT_OK || data!=null || data.getData()!=null){
            imageUri = data.getData();
            picKey = imageUri.toString();
            System.out.println( " testing url " + picKey);

            postPic.setImageURI(imageUri);
            Picasso.get().load(imageUri).fit().centerInside().into(postPic);


            UploadPicture();
        }

    }





    private void UploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();



        // Create a reference to "mountains.jpg"
        StorageReference fileReference = storageReference.child("images/" + randomKey + ".jpeg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        FileLink = task.getResult().toString();
                        final String DownloadUrl = FileLink;

                    }
                });
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), " Image Uploaded" , BaseTransientBottomBar.LENGTH_LONG).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), " Failed To Upload" , Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progessPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage(" Percantage:  " + (int) progessPercent +  "%");
            }
        });


    }
    private void AddPost() {
        String subject = subjectAd.getText().toString();
        String info = infoAd.getText().toString();
        String bloodTReq = bloodTypeReqAd.getText().toString();
        String filelink1 = FileLink.toString().trim();

        //fetching current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        if (subject.equals("") || (info.equals("") && (info.getBytes(StandardCharsets.UTF_8).length <= 300)) || bloodTReq.equals("") || date.equals("")){
            Toast.makeText(getApplicationContext(), "Please make sure fields are not empty", Toast.LENGTH_SHORT).show();

        } else {
            FirebaseDatabase.getInstance().getReference().child("Posts").push().setValue(new Post(info , subject , bloodTReq, date,FileLink  )).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Some Error Happened", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Post Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), PostsList.class));
                    finish();

                }
            });


        }




    }

}