package com.application.resoluteai.Authantication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.application.resoluteai.Authantication.Login.Login;
import com.application.resoluteai.Authantication.OTP.FragmentOTP;
import com.application.resoluteai.Authantication.Signup.Signup;
import com.application.resoluteai.MainActivity;
import com.application.resoluteai.R;
import com.application.resoluteai.ViewModel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity{

    private FragmentManager fragmentManager;
    private Login login;
    private Signup signup;
    private FragmentOTP otp_fragment;
    private FirebaseAuth mAuth;
    private String verification_Id;
    private AuthViewModel authViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();
        // Initialize Firebase Auth

        authViewModel= new ViewModelProvider(this).get(AuthViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        Intent intent =getIntent();

        if (intent!=null) {
            String tag = intent.getStringExtra("TAG");


            if (tag != null && tag.equals("TAG_LOGOUT")) {
                logout();
            }
            if (checkLogin()) {
                    OnAuthCompleted();
//                  loadOtpVerification();
            }


            login = new Login();
            loadFragment(login);
//            loadOtpVerification();

            }




    }

    private void init(){
        fragmentManager= getSupportFragmentManager();

    }

    public void onLogin() {
        Map<String, String> x = login.getCredential();
        String email = x.get("email");
        String pass = x.get("pass");
        if (email == null || pass == null) {
            Toast.makeText(this, "Please check email or password filled correctly.", Toast.LENGTH_SHORT).show();
        }
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            OnAuthCompleted();
                        } else {
                            login.PasswordError();
                            login.EmailError();
                        }
                    }
                });


    }


    private void loadFragment(Fragment x){

            FragmentTransaction ft= fragmentManager.beginTransaction();
            ft.replace(R.id.auth_frame,x);
            ft.commit();
    }

    public void loadSignup(){
        if(signup==null)
                signup=new Signup();
        loadFragment(signup);
    }

    public void loadLogin(){
        if(login == null)
        {
             login=new Login();
        }
        loadFragment(login);
    }

    void loadOtpVerification(){
        if(otp_fragment==null)
            otp_fragment= FragmentOTP.newInstance();
        loadFragment(otp_fragment);
    }

    public void createUser(String name,String email, String pass){

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                onEmailRegistered();
                                            }
                                            else {
                                                Toast.makeText(AuthActivity.this, "Account Creation failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(e ->  Toast.makeText(AuthActivity.this, ""+e.getMessage().toString(),
                        Toast.LENGTH_SHORT).show());

    }



    void OnAuthCompleted(){

        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public boolean checkLogin(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           return true;
        }
        else return false;
    }

    private void logout(){
        mAuth.signOut();
    }

    public void send_otp(String phoneNumber){

        Toast.makeText(AuthActivity.this, "Please wait at moment.we are checking you are not robot.", Toast.LENGTH_SHORT).show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setActivity(AuthActivity.this)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            //In case Verification done through Google Play service
            Toast.makeText(AuthActivity.this, "Verification completed successfully.", Toast.LENGTH_SHORT).show();
            OnVerificationCompleted();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.w("TAG", "onVerificationFailed", e);
            Toast.makeText(AuthActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            verification_Id=verificationId;
            otp_fragment.enableVerifyBtn();
            Toast.makeText(AuthActivity.this, "Otp sent successfully.", Toast.LENGTH_SHORT).show();
        }
    };

    public void onVerify(String code){
        if(verification_Id!=null) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_Id, code);

            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "signInWithCredential:success");

                                OnVerificationCompleted();

                            } else {
                                // Sign in failed, display a message and update the UI
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(AuthActivity.this, "Wrong OTP. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }


    }

    void OnVerificationCompleted(){
        OnAuthCompleted();
    }

    void onEmailRegistered(){
       loadOtpVerification();
    }
}