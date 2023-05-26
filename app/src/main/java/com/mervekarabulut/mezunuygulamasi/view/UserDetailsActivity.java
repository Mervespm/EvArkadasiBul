package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityUserDetailsBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.model.MatchRequest;

public class UserDetailsActivity extends AppCompatActivity {

    private ActivityUserDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            DataHolder user = (DataHolder) intent.getSerializableExtra("user");
            binding.NameText.setText(user.getName() + " " + user.getSurname());
            binding.Year.setText("Sınıf: " + user.getYear());
            binding.phoneNumber.setText("Telefon no: " + user.getPhone());
            binding.Distance.setText("Max uzaklık: " + user.getDistance() + " km");
            binding.StateText.setText("Arama Durumu: " + user.getState());
            binding.Duration.setText("Süre:" + user.getDuration());
            binding.Department.setText("Bölüm: " + user.getDepartment());
        }

        Button btnSendMatchRequest = findViewById(R.id.btnSendMatchRequest);
        btnSendMatchRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                DataHolder user = (DataHolder) intent.getSerializableExtra("user");

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                String key = dbRef.child("MatchRequests").push().getKey();

                MatchRequest matchRequest = new MatchRequest();
                matchRequest.setKey(key);
                matchRequest.setSenderId(currentUser.getUid());
                matchRequest.setReceiverId(user.getUid());
                matchRequest.setSenderName(currentUser.getEmail());
                System.out.println(currentUser.getUid() + " "+ user.getUid());
                matchRequest.setStatus("pending");

                dbRef.child("MatchRequests").child(key).setValue(matchRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserDetailsActivity.this, "Eşleşme talebi başarıyla gönderildi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserDetailsActivity.this, "Eşleşme talebi gönderilemedi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }




}