package com.geziyorum.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.Base64;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geziyorum.R;
import com.geziyorum.pojos.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

/**
 * Created by albur on 10.03.2018.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    private Context mContext;
    private List<Place> placeList;
    private GoogleMap googleMap;
    private MapView mMapView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, surname, date,thumbnail;
        public String longitude,latitude;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            mMapView = (MapView) view.findViewById(R.id.map);
            try {
                MapsInitializer.initialize(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }

            name = (TextView) view.findViewById(R.id.namee);
            surname = (TextView) view.findViewById(R.id.surnamee);
            thumbnail = (TextView) view.findViewById(R.id.thumbnaaill);
            image = (ImageView) view.findViewById(R.id.imagee);
            date = (TextView) view.findViewById(R.id.datee);
        }
    }


    public PlaceAdapter(Context mContext, List<Place> placeList) {
        this.mContext = mContext;
        this.placeList = placeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recview_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Place place = placeList.get(position);

        byte[] decodedString = Base64.decode(place.image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        String dateString = DateFormat.format("MM/dd/yyyy", new Date(place.date)).toString();




        holder.image.setImageBitmap(decodedByte);
        holder.name.setText(place.name);
        holder.surname.setText(place.surname);
        holder.date.setText(dateString);
        ;

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng marker = new LatLng(place.latitude, place.longitude);
                googleMap.addMarker(new MarkerOptions().position(marker).title(place.thumbnail).snippet(place.dettail));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }


     @Override
   public int getItemCount() {
        return placeList.size();
    }
}
