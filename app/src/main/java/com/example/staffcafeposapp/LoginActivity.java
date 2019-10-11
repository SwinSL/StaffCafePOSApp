package com.example.staffcafeposapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.staffcafeposapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class LoginActivity extends AppCompatActivity {

    private TextView textView_error;
    private EditText editText_username, editText_pass;
    private Button btn_login;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editText_username.getText().toString();
                final String pass = editText_pass.getText().toString();

                mReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Staff");
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(user.getUsername().equals(username))
                        {
                            if(user.getPassword().equals(pass))
                            {
                                textView_error.setText(null);
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                textView_error.setText("Wrong password");
                                Toast.makeText(getApplicationContext(),"Wrong password",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            textView_error.setText("User do not exist");
                            Toast.makeText(getApplicationContext(),"User do not exist",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                mReference.addValueEventListener(listener);
            }

        });
    }

    private void initUI()
    {
        editText_username = findViewById(R.id.editText_username);
        editText_pass = findViewById(R.id.editText_pass);
        btn_login = findViewById(R.id.button_login);
        textView_error = findViewById(R.id.textView_error);
    }

    private void newUser(String email, String pass, String status)
    {
        User user = new User(email,pass,status);
        mReference.child("Users").child(status).setValue(user);
    }
}
