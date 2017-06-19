package com.example.sujoy.citi1.sources;

/**
 * Created by Sujoy on 5/1/2017.
 */

public class DoctorFilter implements CitiFilter {
    String city, speciality;
    @Override
    public void setFilterCode(Object first, String code) {
        if(first.toString().equals("District")){
            city = code;
        }
        else{
            speciality = code;
        }
    }
}
