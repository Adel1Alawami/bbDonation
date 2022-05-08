package com.example.fetching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button Register;
private EditText Fullname, Email, Age , Password;
    private EditText Bloodtype;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register2);
        getSupportActionBar().hide();


        mAuth= FirebaseAuth.getInstance();
        Fullname= findViewById(R.id.txt_Fullname);
        Age= findViewById(R.id.txt_Age);
        Password= findViewById(R.id.txt_Password_RA);
        Email= findViewById(R.id.txt_Email_RA);
        Bloodtype= findViewById(R.id.txt_Password2_RA);
            Register= findViewById(R.id.btn_Register);


        Register.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = Email.getText().toString();
        String password= Password.getText().toString();
        String age = Age.getText().toString();
        String fullname= Fullname.getText().toString().trim();
        String bloodtype = Bloodtype.getText().toString().trim();
        if (fullname.isEmpty()) {
            Fullname.setError("Name Is Required!");
            Fullname.requestFocus();
        return;
        }
        if(age.isEmpty()){
            Age.setError("Age Is Required!");
            Age.requestFocus();
            return;
        }
        if(email.isEmpty()){
            Email.setError("Email Is Required!");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Provide A Valid Email");
            Email.requestFocus();
            return;
        }

        if (password.isEmpty()){
            Password.setError("Password Is Required!");
            Password.requestFocus();
        return;
        }
        if (password.length() < 6) {
            Password.setError("Password need to be Atleast 6 Characters");
            Password.requestFocus();
            return;
        }
            if (bloodtype.isEmpty()){
                Bloodtype.setError("Password Is Required!");
                Bloodtype.requestFocus();
                return;
        }

mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
    if(task.isSuccessful()){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
        Calendar s = Calendar.getInstance();
        String date = sdf.format(s.getTime());
        User user = new User(fullname , age , email, password  , "0", bloodtype , date , 0  );
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Has Been Registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                    }

                });


    }
    else {
        Toast.makeText(RegisterActivity.this, "Failed To Register User", Toast.LENGTH_SHORT).show();
    }

});

    }
}