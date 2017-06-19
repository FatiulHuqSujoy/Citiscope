package com.example.sujoy.citi1.sources;

/**
 * Created by Sujoy on 5/1/2017.
 */

public class ApartmentRentFilter implements CitiFilter {
    String area, city, minPrice, maxPrice, propType;

    @Override
    public void setFilterCode(Object first, String code) {
        if(first.toString().equals("District")){
            city = code;
        }
        else if(first.toString().equals("Area")){
            area = code;
        }
        else if(first.toString().equals("Maximum Price")){
            maxPrice = code;
        }
        else if(first.toString().equals("Minimum Price")){
            minPrice = code;
        }
        else if(first.toString().equals("Property Type")){
            propType = code;
        }
    }

    @Override
    public String toString() {
        return "Area: " + area + " City: " + city + " minPrice: " + minPrice + " maxPrice: " + maxPrice + " Property Type: " + propType;
    }
}
