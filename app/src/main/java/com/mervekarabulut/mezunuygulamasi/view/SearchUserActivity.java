package com.mervekarabulut.mezunuygulamasi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mervekarabulut.mezunuygulamasi.adapter.UserAdapter;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivitySearchUserBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Locale;

public class SearchUserActivity extends AppCompatActivity {
    private ActivitySearchUserBinding binding;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList <DataHolder> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.rc.setHasFixedSize(true);
        binding.rc.setLayoutManager(new LinearLayoutManager(SearchUserActivity.this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registered Users");


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(databaseReference != null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            list.add(ds.getValue(DataHolder.class));
                        }
                        UserAdapter userAdapter = new UserAdapter(list);
                        binding.rc.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchUserActivity.this,  error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        if(binding.search != null){
            binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String s){

        ArrayList<DataHolder> myList = new ArrayList<>();
        for(DataHolder o: list){

            if(o.getName().toLowerCase().contains(s.toLowerCase())){
                myList.add(o);
            }
        }
        UserAdapter userAdapter = new UserAdapter(myList);

        binding.rc.setAdapter(userAdapter);

    }
}
