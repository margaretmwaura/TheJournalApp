package com.example.android.thechallenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Login extends AppCompatActivity  {

//    Using bindView libraries
    @InjectView(R.id.email)
    public EditText email;
    @InjectView(R.id.password)
    public EditText password;
    @InjectView(R.id.phoneNumber)
    public EditText phoneNumber;
    @InjectView(R.id.sign_in_via_email)
    public Button emailButton ;
    @InjectView(R.id.sign_in_via_mobile)
    public Button phoneNumberButton;
    @InjectView(R.id.button_login)
    public Button loginButton;
    private String emailString,passwordString,phoneNumberString;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mfirebaseDatabase;
    private String userId;
    private static String token;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

//    Will be used for the purposes of ensuring only one person is authenticated at a time
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

//        this code is necessary for getting the instannce of the firebase object
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
//        Will deal with the aspect of the user to be dealt with later
        currentUser = mAuth.getCurrentUser();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Login.this, new OnSuccessListener<InstanceIdResult>()
        {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult)
            {
                token = instanceIdResult.getToken();
            }
        });

    }



     @OnClick({R.id.sign_in_via_email})
    public void SignUpViaPasswordandEmail()
    {

        emailString = email.getText().toString();
        passwordString = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailString,passwordString)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(Login.this,"The signing was successful " , Toast.LENGTH_LONG).show();
                              Intent intent = new Intent(Login.this,AddAThought.class);
                              startActivity(intent);
                          }
                          else
                          {
                              Toast.makeText(Login.this,"Failed",Toast.LENGTH_LONG).show();
                              Toast.makeText(Login.this,"This is the entered email and password " + emailString + passwordString , Toast.LENGTH_LONG).show();
                              Log.d("Exception","This is the cause of the error ");
                          }
                    }
                });
    }

    @OnClick(R.id.sign_in_via_mobile)
    public void signInViaThePhoneNumber()
    {
        phoneNumberString = phoneNumber.getText().toString();

            if(phoneNumberString.isEmpty() || phoneNumberString.length() < 10)
            {
                phoneNumber.setError("Enter a valid phone number");
                phoneNumber.requestFocus();
            }

            Intent intent = new Intent(Login.this,Verify_phone_number_entered.class);
            intent.putExtra("The phone number ",phoneNumberString);
            startActivity(intent);
    }

    @OnClick(R.id.button_login)
    public void clickLoginButton()
    {
        Intent intent = new Intent(Login.this,LoginAfterCreatingAccount.class);
        startActivity(intent);
    }

    public static String returnRegistrationToken()
    {
        return token;
    }

}
