package com.mervekarabulut.mezunuygulamasi.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityUpdateProfileBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityUpdateProfileBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;


    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseUser user;

    Uri imageData;
    private String myUri;
    private Bitmap bitmap;
    Button button;
    RadioButton selectedRadioButton;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        getData();
    }

    private void getData() {
        user = auth.getCurrentUser();
        String email = user.getEmail();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataHolder data  = task.getResult().getValue(DataHolder.class);
                binding.NameText.setText(data.getName());
                binding.SurnameText.setText(data.getSurname());
                binding.phoneNumber.setText(data.getPhone());
                binding.Department.setText(data.getDepartment());
                binding.Distance.setText(data.getDuration());
                binding.Duration.setText(data.getDuration());
                binding.Year.setText(data.getYear());
                if(data.getState() != null){
                    if(data.getState().equals("Kalacak Ev/Oda arıyor")){
                        binding.radioGroup.check(R.id.LookingForHome);
                    }else if(data.getState().equals("Ev/Oda arkadaşı arıyor")){
                        binding.radioGroup.check(R.id.LookingForMate);
                    }else{
                        binding.radioGroup.check(R.id.NotLooking);
                    }
                }
            }
        } );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Photos").document(email);
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
    }

    public void editProfile(View view){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String name = binding.NameText.getText().toString();
        String surname = binding.SurnameText.getText().toString();
        String phone = binding.phoneNumber.getText().toString();
        String department = binding.Department.getText().toString();
        String year = binding.Year.getText().toString();
        String distance = binding.Distance.getText().toString();
        String duration = binding.Duration.getText().toString();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        button= (Button) findViewById(R.id.NotLooking);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        button = (RadioButton) findViewById(selectedId);
        String state = button.getText().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("surname", surname);
        data.put("phone", phone);
        data.put("department", department);
        data.put("year", year);
        data.put("distance", distance);
        data.put("duration",duration);
        data.put("state",state);

        user = auth.getCurrentUser();
        String email = user.getEmail();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(user.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdateProfileActivity.this, "COMPLETED", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(UpdateProfileActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (imageData != null){
            UUID uuid = UUID.randomUUID();
            String imageName = "Photos/" + uuid +".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    storageReference = firebaseStorage.getReference(imageName);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();

                            FirebaseUser user = auth.getCurrentUser();

                            String email = user.getEmail();

                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("usermail", email);
                            postData.put("url", downloadUrl);
                            postData.put("date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Photos").document(email).set(postData);
                        }
                    });
                }
            });
        }
        Intent intent = new Intent(UpdateProfileActivity.this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Galeri için erişim gereklidirç", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);

        }
    }
    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        imageData = intentFromResult.getData();
                        binding.profileImage.setImageURI(imageData);
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivity(intentToGallery);
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "İzin gerekiyor!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
