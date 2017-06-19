package com.example.sujoy.citi1.sources;

/**
 * Created by Sujoy on 5/1/2017.
 */

public class BloodDonationFilter implements CitiFilter {
    String city, bGroup;
    @Override
    public void setFilterCode(Object first, String code) {
        if(first.toString().equals("District")){
            city = code;
        }
        else{
            bGroup = code;
        }
    }
}
