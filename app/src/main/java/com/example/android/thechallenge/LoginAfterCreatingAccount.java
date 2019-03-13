package com.example.android.thechallenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginAfterCreatingAccount extends AppCompatActivity {

    @InjectView(R.id.login_email)
    public EditText email;
    @InjectView(R.id.login_password)
    public EditText password;
    @InjectView(R.id.login_button_loginActivity)
    public Button loginButton;
    private String emailString,passwordString;
    private FirebaseAuth mAuth;
    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after_creating_account);

        ButterKnife.inject(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.login_button_loginActivity)
    public void login()
    {
        emailString = email.getText().toString();
        passwordString = password.getText().toString();

        mAuth.signInWithEmailAndPassword(emailString,passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginAfterCreatingAccount.this,"The signing was successful " , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginAfterCreatingAccount.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginAfterCreatingAccount.this,"Failed",Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginAfterCreatingAccount.this,"This is the entered email and password " + emailString + passwordString , Toast.LENGTH_LONG).show();
                            Log.d("Exception","This is the cause of the error ");
                        }
                    }
                });

    }
}
