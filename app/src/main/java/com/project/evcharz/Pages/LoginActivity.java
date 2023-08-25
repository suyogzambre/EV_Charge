package com.project.evcharz.Pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.evcharz.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button sendOtpBtn = findViewById(R.id.btn_sendOtp);

        EditText phone_No = findViewById(R.id.txt_phoneNo);

        ProgressBar progressBar = findViewById(R.id.progressBar);

        sendOtpBtn.setOnClickListener(v -> {
            String num = phone_No.getText().toString();
            if(num.length() != 10 || num == null){
                Toast.makeText(this, "Please enter a valid Number", Toast.LENGTH_SHORT).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                sendOtpBtn.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phone_No.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        LoginActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                sendOtpBtn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                sendOtpBtn.setVisibility(View.VISIBLE);
                                System.out.println("error in otp "+e.getMessage());
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String backend_otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(backend_otp, forceResendingToken);

                                Intent i = new Intent(LoginActivity.this, OtpValidation.class);
                                i.putExtra("phoneNo", phone_No.getText().toString());
                                i.putExtra("otp", backend_otp);
                                startActivity(i);
                            }
                        }
                );

            }
        });

    }


}