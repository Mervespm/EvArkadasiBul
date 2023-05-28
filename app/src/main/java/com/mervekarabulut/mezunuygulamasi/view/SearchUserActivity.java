package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.adapter.SearchUserAdapter;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class SearchUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText distanceEditText;
    private EditText searchEditText;
    private Spinner stateSpinner;
    private Button searchButton;
    private RecyclerView recyclerView;
    private SearchUserAdapter userAdapter;

    private DatabaseReference userReference;
    private ValueEventListener valueEventListener;

    private List<DataHolder> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        searchEditText = findViewById(R.id.searchEditText);
        distanceEditText = findViewById(R.id.distanceEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new SearchUserAdapter(this);
        recyclerView.setAdapter(userAdapter);


        // Get a reference to the "Registered Users" node
        userReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Add a ValueEventListener to listen for changes in the user data
        valueEventListener = userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                // Iterate through the snapshot to retrieve user data
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataHolder user = userSnapshot.getValue(DataHolder.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }

                // Update the user list in the adapter
                userAdapter.updateUserList(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Remove the ValueEventListener when the activity is stopped
        if (valueEventListener != null) {
            userReference.removeEventListener(valueEventListener);
        }
    }

    private void searchUsers() {
        String searchQuery = searchEditText.getText().toString();
        String distanceText = distanceEditText.getText().toString();
        int maxDistance = 100;
        if (!distanceText.isEmpty()) {
            maxDistance = Integer.parseInt(distanceText);
        }
        // Kullanıcıları arama ve filtreleme işlemleri
        List<DataHolder> filteredUsers = new ArrayList<>();
        for (DataHolder user : userList) {
            if(user.getDistance() != null){
                if(!user.getDistance().equals(""))
                {
                    if (user.getName().toLowerCase().contains(searchQuery.toLowerCase()) && Integer.parseInt(user.getDistance()) <= maxDistance) {
                        filteredUsers.add(user);
                    }

                }
            }else{
                if (user.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    filteredUsers.add(user);
                }
            }

        }

        // Update the user list in the adapter with the filtered results
        userAdapter.updateUserList(filteredUsers);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Perform search when a spinner item is selected
        searchUsers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing if no spinner item is selected
    }
}
