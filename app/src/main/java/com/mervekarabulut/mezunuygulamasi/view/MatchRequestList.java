package com.mervekarabulut.mezunuygulamasi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.adapter.MatchRequestAdapter;
import com.mervekarabulut.mezunuygulamasi.model.MatchRequest;


import java.util.ArrayList;
import java.util.List;

public class MatchRequestList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_request_list);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMatchRequests();
    }

    private void loadMatchRequests() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("MatchRequests");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        dbRef.orderByChild("receiverId").equalTo(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MatchRequest> matchRequests = new ArrayList<>();
                for (DataSnapshot matchRequestSnapshot : dataSnapshot.getChildren()) {
                    MatchRequest matchRequest = matchRequestSnapshot.getValue(MatchRequest.class);
                    matchRequest.setKey(matchRequestSnapshot.getKey()); // Add this line
                    matchRequests.add(matchRequest);
                }

                // matchRequests listesi artık currentUser'a gönderilen tüm eşleşme taleplerini içeriyor.
                // Bu listeyi, RecyclerView adapter'ınızın güncellenmesi için kullanabilirsiniz.

                adapter = new MatchRequestAdapter(MatchRequestList.this, matchRequests);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // ...
            }
        });
    }

}




