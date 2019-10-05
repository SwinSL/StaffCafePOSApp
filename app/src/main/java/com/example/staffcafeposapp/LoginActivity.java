package com.example.staffcafeposapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private TextView textView_username, textView_password;
    private EditText editText_username, editText_pass;
    private Button btn_login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        firebaseAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editText_username.getText().toString();
                String password = editText_pass.getText().toString();

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(LoginActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this,"Please enter password.",Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    }

}
