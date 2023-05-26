package com.mervekarabulut.mezunuygulamasi.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityMainBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    Uri imageData;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        if(user != null){
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signUpClicked(View view){

        String email = binding.EmailAddress.getText().toString();
        String pass = binding.Password.getText().toString();
        String name = binding.NameText.getText().toString();
        String surname = binding.SurnameText.getText().toString();
        String state = "NotLooking";

        if (email.equals("") || pass.equals("")) {
            Toast.makeText(this, "Email ya da şifre giriniz", Toast.LENGTH_LONG).show();
        } else {
            if (!email.endsWith("@std.yildiz.edu.tr")) {
                sendVerificationEmail(email, name, surname, pass);
            } else {
                Toast.makeText(getApplicationContext(), "Mail adresiniz std.yildiz.edu.tr ile bitmeli .", Toast.LENGTH_LONG).show();
            }

        }

    }
    private void sendVerificationEmail(String email, String name, String surname, String pass) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> emailTask) {
                                if (emailTask.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Doğrulama e-postası gönderildi.", Toast.LENGTH_SHORT).show();
                                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                                    if (user != null) {
                                        String userId = user.getUid();
                                        String userEmail = user.getEmail();
                                        DataHolder newUser = new DataHolder(userEmail, name, surname, userId); // Örneğin bir "User" sınıfı oluşturduk
                                        referenceProfile.child(user.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, "Kullanıcı başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Kullanıcı kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Doğrulama e-postası gönderilemedi.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Kullanıcı oluşturulurken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    public void signInIntent(View view){
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

}

