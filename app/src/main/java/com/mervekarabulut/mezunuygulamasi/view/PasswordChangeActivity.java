package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityPasswordChangeBinding;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivitySignInBinding;

public class PasswordChangeActivity extends AppCompatActivity {
    private ActivityPasswordChangeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordChangeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FirebaseAuth auth = FirebaseAuth.getInstance();


    }
    public void changePass(View view){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = binding.editTextTextEmailAddress.getText().toString();
        String pass = binding.oldPassword1.getText().toString();
        String pass2 = binding.oldPassword2.getText().toString();
        String newPass = binding.newPassword.getText().toString();

        if(email.equals("") || pass.equals("")){
            Toast.makeText(this, "Email ya da şifre giriniz", Toast.LENGTH_LONG).show();
        }else{
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    if(pass.equals(pass2)){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        user.updatePassword(newPass)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PasswordChangeActivity.this,"Parola Degistirildi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    Intent intent = new Intent(PasswordChangeActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PasswordChangeActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}