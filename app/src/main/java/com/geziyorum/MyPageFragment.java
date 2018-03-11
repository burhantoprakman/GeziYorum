package com.geziyorum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geziyorum.adapter.PlaceAdapter;
import com.geziyorum.pojos.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MyPageFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private List<Place> placeList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =  database.getReference("posts");
        myRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
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
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }
}
