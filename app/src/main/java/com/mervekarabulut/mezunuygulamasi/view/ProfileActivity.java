package com.mervekarabulut.mezunuygulamasi.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityFeedBinding;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityProfileBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.model.Post;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;

    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseUser user;

    Uri photoData;
    private String myUri;
    private Bitmap bitmap;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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
                binding.education.setText(data.getEducation());
                binding.enteringYear.setText(data.getEnteringYear());
                binding.graduateYear.setText(data.getGraduateYear());
                binding.location.setText(data.getCity());

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
                   }else{

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
        String enteringYear = binding.enteringYear.getText().toString();
        String graduateYear = binding.graduateYear.getText().toString();
        String education = binding.education.getText().toString();
        String city = binding.location.getText().toString();

        HashMap data = new HashMap();
        data.put("name", name);
        data.put("surname", surname);
        data.put("phone", phone);
        data.put("enteringYear", enteringYear);
        data.put("graduateYear", graduateYear);
        data.put("education", education);
        data.put("city", city);

        user = auth.getCurrentUser();
        String email = user.getEmail();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(user.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "COMPLETED", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(ProfileActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (photoData != null){
            UUID uuid = UUID.randomUUID();
            String imageName = "Photos/" + uuid +".jpg";
            storageReference.child(imageName).putFile(photoData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
            Intent intent = new Intent(ProfileActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }



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

    private void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        photoData = intentFromResult.getData();
                        binding.profileImage.setImageURI(photoData);

                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(ProfileActivity.this, "İzin gerekiyor!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}