package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityUserDetailsBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.model.MatchRequest;
import com.squareup.picasso.Picasso;

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
            binding.Distance.setText("Max uzaklık: " + user.getDistance() + " km");
            binding.StateText.setText("Arama Durumu: " + user.getState());
            binding.Duration.setText("Süre:" + user.getDuration());
            binding.Department.setText("Bölüm: " + user.getDepartment());
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("Photos").document(user.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String url = (String) document.getData().get("url");
                            Picasso.get().load(url).into(binding.profileImage);
                        }
                    }
                }
            });

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

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(currentUser.getUid()).child("phone").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot dataSnapshot = task.getResult();
                                if (dataSnapshot.exists()) {
                                    String phone = dataSnapshot.getValue(String.class);
                                    matchRequest.setSenderPhone(phone);
                                    matchRequest.setSenderId(currentUser.getUid());
                                    matchRequest.setReceiverId(user.getUid());
                                    matchRequest.setSenderName(currentUser.getEmail());
                                    matchRequest.setSenderEmail(currentUser.getEmail());
                                    matchRequest.setStatus("pending");

                                    // matchRequest objesi tamamen doldurulduktan sonra setValue() işlemi gerçekleştirilir
                                    dbRef.child("MatchRequests").child(key).setValue(matchRequest)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(UserDetailsActivity.this, "Eşleşme talebi başarıyla gönderildi", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UserDetailsActivity.this, "Eşleşme talebi gönderilemedi", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }
                    });





//                    dbRef.child("MatchRequests").child(key).setValue(matchRequest).addOnSuccessListener(aVoid -> Toast.makeText(UserDetailsActivity.this, "Eşleşme talebi başarıyla gönderildi", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(UserDetailsActivity.this, "Eşleşme talebi gönderilemedi", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            });
        }

    }

}

