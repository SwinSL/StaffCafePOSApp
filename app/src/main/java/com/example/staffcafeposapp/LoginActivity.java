package com.example.staffcafeposapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

    private TextView textView_username, textView_password,textView_error;
    private EditText editText_username, editText_pass;
    private Button btn_login;
    private ProgressDialog progressDialog;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editText_username.getText().toString();
                final String pass = editText_pass.getText().toString();


                progressDialog.show();

                mReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Staff");
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(user.getUsername().equals(username))
                        {
                            if(user.getPassword().equals(pass))
                            {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(),"Wrong password",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"User do not exist",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
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
        textView_username = findViewById(R.id.textView_username);
        textView_password = findViewById(R.id.textView_pass);
        textView_error = findViewById(R.id.textView_error);
    }

    private void newUser(String email, String pass, String status)
    {
        User user = new User(email,pass,status);

        mReference.child("Users").child(status).setValue(user);
    }

    /*private void login()
    {
        final String username = editText_username.getText().toString();
        final String password = editText_pass.getText().toString();
        //ref = FirebaseDatabase.getInstance().getReference().child(dbname);

        ref.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    User user = dataSnapshot.getValue(User.class);
                    if (password.equals(user.getPassword())) {
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter correct pass", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ess", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "Value is :" + username);
                    Log.i("TAG","Pass is : " + password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        /*if(TextUtils.isEmpty(username))
        {
            Toast.makeText(LoginActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Please enter password.",Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {
            validate(username,password);
        }

                progressDialog.setMessage("Logging in.");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            textView_error.setText(null);
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                            textView_error.setText("User not available.");
                            progressDialog.dismiss();
                        }
                    }
                });
    }*/

}
