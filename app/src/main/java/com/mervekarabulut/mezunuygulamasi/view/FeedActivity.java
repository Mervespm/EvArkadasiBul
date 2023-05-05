package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.adapter.PostAdapter;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityFeedBinding;
import com.mervekarabulut.mezunuygulamasi.model.Post;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    PostAdapter postAdapter;
    private ActivityFeedBinding binding;
    static Post chosenPost;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_post){
            Intent intentUpload = new Intent(this, UploadActivity.class);
            intentUpload.putExtra("info", "name");
            startActivity(intentUpload);

        }else if(item.getItemId() == R.id.signOut){
            auth.signOut();
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();
        }else if(item.getItemId() == R.id.editProfile){
            Intent intentMain = new Intent(this, ProfileActivity.class);
            startActivity(intentMain);
        }else if(item.getItemId() == R.id.searchUser){
            Intent intentMain = new Intent(this, SearchUserActivity.class);
            startActivity(intentMain);
        }else if(item.getItemId() == R.id.myPosts){
            Intent intentMain = new Intent(this, MyPostsActivity.class);
            startActivity(intentMain);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();
        postAdapter = new PostAdapter(postArrayList,FeedActivity.this );

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(postAdapter);

    }

    private void getData(){
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if(value != null){
                    for(DocumentSnapshot ds: value.getDocuments()){

                        Map<String , Object> data = ds.getData();
                        String email = (String) data.get("usermail");
                        String comment = (String) data.get("comment");
                        String url = (String) data.get("url");
                        Post post = new Post(email, comment, url);
                        postArrayList.add(post);

                    }
                    postAdapter.notifyDataSetChanged();
                }

            }
        });

    }




}