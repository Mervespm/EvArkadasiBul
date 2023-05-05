package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityMainBinding;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            Intent intent = new Intent(SignInActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void signInClicked(View view){

        String email = binding.editTextEmailAddress.getText().toString();
        String pass = binding.editTextPassword.getText().toString();

        if(email.equals("") || pass.equals("")){
            Toast.makeText(this, "Email ya da şifre giriniz", Toast.LENGTH_LONG).show();
        }else{
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent intent = new Intent(SignInActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void changePassword(View view){

        Intent intent = new Intent(SignInActivity.this, PasswordChangeActivity.class);
        startActivity(intent);
        finish();

    }

}