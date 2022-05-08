package com.example.fetching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Button register;
    private Button login;
    private EditText email, password;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
//    ProgressBar pb;
//    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        login = findViewById(R.id.btn_Login);
        mAuth= FirebaseAuth.getInstance();
        email = findViewById(R.id.txt_Email);
        password = findViewById(R.id.txt_Password);
        register = findViewById(R.id.btn_Register_L);









        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    private void LoginUser() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loging In User");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();


        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if (Email.isEmpty()) {
            email.setError("Email Is Required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Provide A Valid Email");
            email.requestFocus();
            return;

        }/// Making An Admin Logs in to a specific Activity
//        if (Email.matches("Admin@gmail.com")){
//            startActivity(new Intent(MainActivity.this , AdminActivity.class));
//            return;
//        }

        if (Password.isEmpty()) {
            password.setError("Password Is Required!");
            password.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            password.setError("Password need to be Atleast 6 Characters");
            password.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                      DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                      String userID = user.getUid();
                      dialog.hide();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);
                            if (userProfile!= null){
                                if (userProfile.isAdmin.equals("1")){
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Failed To login User", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                
            }
        });
    }

    @Override
    public void onBackPressed() {
        sharedPreferences= getApplicationContext().getSharedPreferences("mySharedPreference", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        if(sharedPreferences != null)
        {
            boolean checkedState= sharedPreferences.getBoolean("Checked", false);

            if(checkedState)
            {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }



}