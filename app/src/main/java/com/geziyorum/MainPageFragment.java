package com.geziyorum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.geziyorum.adapter.PlaceAdapter;
import com.geziyorum.adapter.PlaceAdapter;
import com.geziyorum.pojos.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albur on 10.03.2018.
 */

public class MainPageFragment extends Fragment {
    private RecyclerView recyclerView;
   private PlaceAdapter adapter;
    private List<Place> placeList;
    private FloatingActionButton fab;
    private GoogleMap googleMap;
    private MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainpage_fragment, container, false);
        fab = view.findViewById(R.id.btn_fab);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intet = new Intent(getContext(),AddPlaceActivity.class);
                startActivity(intet);
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =  database.getReference("posts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot d1 : ds.getChildren()) {
                        Place place = d1.getValue(Place.class);
                        placeList.add(place);
                    }
                }

                adapter = new PlaceAdapter(getContext(), placeList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        return view;
    }

    public static Fragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
        return fragment;
    }


}
