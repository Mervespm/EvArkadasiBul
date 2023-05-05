package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.adapter.PostAdapter;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityFeedBinding;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityMyPostsBinding;
import com.mervekarabulut.mezunuygulamasi.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MyPostsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    PostAdapter postAdapter;
    private ActivityMyPostsBinding binding;
    static Post chosenPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyPostsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();
        postAdapter = new PostAdapter(postArrayList,MyPostsActivity.this );

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerView.setAdapter(postAdapter);
    }

    private void getData(){
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        String email =  auth.getCurrentUser().getEmail();
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(MyPostsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if(value != null){
                    for(DocumentSnapshot ds: value.getDocuments()){

                        Map<String , Object> data = ds.getData();
                        if( email.equals((String) data.get("usermail")))
                        {
                            String email = (String) data.get("usermail");
                            String comment = (String) data.get("comment");
                            String url = (String) data.get("url");
                            Post post = new Post(email, comment, url);
                            postArrayList.add(post);
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }

            }
        });

    }
}