package com.geziyorum.pojos;

import android.media.Image;
import android.widget.ImageView;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by albur on 10.03.2018.
 */

public class Place {

    public String image;
    public String name;
    public String surname ;
    public Long date;
    public Double latitude;
    public Double longitude;
    public String thumbnail;
    public String dettail;

 public Place(){}
 public Place(String image,String name,String surname,Long date,Double latitude,Double longitude,String thumbnail,String detail){
     this.image=image;
     this.name = name;
     this.surname = surname;
     this.date = date;
     this.latitude = latitude;
     this.longitude = longitude;
     this.thumbnail = thumbnail;
     this.dettail = detail;
 }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("image", image);
        result.put("name", name);
        result.put("surname", surname);
        result.put("date", date);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("thumbnail", thumbnail);
        result.put("detail", dettail);



        return result;
    }


}