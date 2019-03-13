package com.example.android.thechallenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Verify_phone_number_entered extends AppCompatActivity {

    private String enteredPhoneNumber,mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private FirebaseAuth mAuth;
    @InjectView(R.id.verification_code)
    public EditText verficationCodeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number_entered);

        ButterKnife.inject(this);

        mAuth= FirebaseAuth.getInstance();
//        The phone number of the user is gotten
        Intent intent = getIntent();
        enteredPhoneNumber = intent.getStringExtra("The phone number ");

        setUpTheCallBacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                enteredPhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBacks);

    }

    private void setUpTheCallBacks()
    {
         mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
             @Override
             public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
             {

                 String code = phoneAuthCredential.getSmsCode();

                 if (code != null) {
                     verficationCodeEditText.setText(code);
                     //verifying the code
                     verifyVerificationCode(code);
                 }


             }

             @Override
             public void onVerificationFailed(FirebaseException e)
             {
                 Toast.makeText(Verify_phone_number_entered.this,"Verification failed " + e.getMessage(),Toast.LENGTH_LONG).show();
             }

             @Override
             public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                 super.onCodeSent(s, forceResendingToken);
                 mVerificationId = s;
             }
         };

    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
       mAuth.signInWithCredential(credential)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task)
                   {
                     if(task.isSuccessful())
                     {
                         Toast.makeText(Verify_phone_number_entered.this,"Successful " , Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(Verify_phone_number_entered.this,AddAThought.class);
                         startActivity(intent);
                     }
                     else
                     {
                         if (task.getException() instanceof
                                 FirebaseAuthInvalidCredentialsException)
                         {
                             // The verification code entered was invalid
                             Log.d("ErrorVerfiying","This is the error " + task.getException());
                         }

                         Toast.makeText(Verify_phone_number_entered.this,"Failed",Toast.LENGTH_LONG).show();
                     }
                   }
               });
    }

}
