package com.geziyorum;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geziyorum.pojos.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;

public class AddPlaceActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CAMERA_REQUEST = 1888;
    public static final int PICK_IMAGE = 1;
    private Button btn_camera,btn_share,btn_getLcation;
    private EditText edt_thumbnail, edt_detail;
    private double longitude;
    private  double latitude;
    private  LocationManager lm;
    private String provider;
    private SharedPreferences preferences;
    private  Bitmap photo;
    private  GPSTracker gps;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        database = FirebaseDatabase.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getComponent();
        btn_getLcation.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_share.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addplace_btn_image:
                openDialog();
                break;
            case R.id.addplace_btn_location:

                gps = new GPSTracker(AddPlaceActivity.this);
                latitude =  gps.getLatitude();
                longitude =  gps.getLongitude();
                break;
            case R.id.addplace_btn_share:

                String name = preferences.getString("name", "");
                String surname = preferences.getString("surname", "");
                String thumbnail = edt_thumbnail.getText().toString();
                String detail = edt_detail.getText().toString();
                long time= System.currentTimeMillis();
                btn_camera.buildDrawingCache();
                Bitmap bmap = btn_camera.getDrawingCache();
                String encodedImageData =getEncoded64ImageStringFromBitmap(bmap);

                Place place = new Place(encodedImageData,name,surname,time,latitude,longitude,thumbnail,detail);
                Map<String, Object> placeValues = place.toMap();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                DatabaseReference myRef = database.getReference("posts").child(user.getUid()).child(thumbnail);
                myRef.setValue(placeValues);
                break;
        }
    }
    private void getComponent(){
        btn_camera = findViewById(R.id.addplace_btn_image);
        btn_share = findViewById(R.id.addplace_btn_share);
        btn_getLcation = findViewById(R.id.addplace_btn_location);
        edt_thumbnail = findViewById(R.id.addplace_edt_thumbnail);
        edt_detail = findViewById(R.id.addplace_edt_detail);

    }
    private void openDialog(){
        final Dialog dialog = new Dialog(AddPlaceActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("");

        // set values for custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.dialog_text);


        dialog.show();

        Button takepic = dialog.findViewById(R.id.dialog_btn_takepic);
        Button choosepic = dialog.findViewById(R.id.dialog_btn_choosepic);

        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                dialog.dismiss();

            }
        });
        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                dialog.dismiss();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            BitmapDrawable bdrawable = new BitmapDrawable(getApplicationContext().getResources(),photo);
            btn_camera.setBackground(bdrawable);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data && data.getData()!= null) {
            try{
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                BitmapDrawable bdrawable = new BitmapDrawable(getApplicationContext().getResources(),selectedImage);
                btn_camera.setBackground(bdrawable);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

}
