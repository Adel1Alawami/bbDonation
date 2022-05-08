package com.example.fetching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserProfile extends AppCompatActivity {
    private Button justDonated;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        justDonated = findViewById(R.id.btnUpdateDate);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        final TextView FullnameTextView = findViewById(R.id.txtusernameUP);
        final TextView BloodTypeTextView = findViewById(R.id.txtusernameUP3);
        final TextView EmailTextView = findViewById(R.id.txtusernameUP2);
        final TextView LasttimedonatedTextView = findViewById(R.id.txtusernameUP4);
        final TextView noDonated = findViewById(R.id.txtNoDonated);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
                Calendar s = Calendar.getInstance();
                String date = sdf.format(s.getTime());
                if (userProfile != null) {
                    String fullname = userProfile.fullname;
                    String bloodtype = userProfile.bloodtype;
                    String email = userProfile.email;
                    date = userProfile.lasttimeDonated.toString();
                    int Nodonated = userProfile.noDonated;
                    FullnameTextView.setText(fullname);
                    BloodTypeTextView.setText(bloodtype);
                    EmailTextView.setText(email);
                    LasttimedonatedTextView.setText(date);
                    noDonated.setText(String.valueOf(Nodonated));
//                    noDonated.setText((Nodonated));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something Wrong Happened!", Toast.LENGTH_SHORT).show();
            }
        });

        //    The COUNTER FOR NoDonated
        justDonated.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Refresh();
            }
        });
    }

    //Refresh Counter/LastTimeDonated

    private void Refresh() {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
            Calendar s = Calendar.getInstance();
            String date = sdf.format(s.getTime());
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    int c = userProfile.noDonated;
                    c++;
                    FirebaseDatabase.getInstance().getReference("Users").child(userID).child("lasttimeDonated").setValue(date);
                    FirebaseDatabase.getInstance().getReference("Users").child(userID).child("noDonated").setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                            startActivity(intent);
                        }
                    });
                 //   System.out.println(c);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
    }
}