package com.mervekarabulut.mezunuygulamasi.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mervekarabulut.mezunuygulamasi.R;

import org.jetbrains.annotations.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.model.Marker;


import java.util.HashMap;
import java.util.Map;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference userLocationsRef;
    private ChildEventListener childEventListener;
    private Map<String, Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocationsRef = FirebaseDatabase.getInstance().getReference().child("userLocations");
        markers = new HashMap<>();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//                String userId = dataSnapshot.getKey();
//                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
//                double longitude = dataSnapshot.child("longitude").getValue(Double.class);
//
//                LatLng location = new LatLng(latitude, longitude);
//                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(userId));
//                markers.put(userId, marker);

                String userId = dataSnapshot.getKey();
                String userEmail = dataSnapshot.child("email").getValue(String.class); // Kullanıcı e-postasını al

                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                LatLng location = new LatLng(latitude, longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(userEmail)); // Kullanıcı e-postasını başlık olarak kullan
                markers.put(userId, marker);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String userId = dataSnapshot.getKey();
                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                LatLng location = new LatLng(latitude, longitude);
                Marker marker = markers.get(userId);
                if (marker != null) {
                    marker.setPosition(location);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();
                Marker marker = markers.get(userId);
                if (marker != null) {
                    marker.remove();
                    markers.remove(userId);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            // Diğer ChildEventListener metotlarını da gerektiği gibi implemente edebilirsiniz
            // ...
        };

        userLocationsRef.addChildEventListener(childEventListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Haritayı başlangıç konumuna getir
        LatLng initialLocation = new LatLng(41.012945, 28.9130583); // Örnek bir başlangıç konumu
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userLocationsRef.removeEventListener(childEventListener);
        markers.clear();
    }
}
