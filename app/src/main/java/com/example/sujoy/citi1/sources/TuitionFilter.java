package com.example.sujoy.citi1.sources;

/**
 * Created by Sujoy on 5/1/2017.
 */

public class TuitionFilter implements CitiFilter {
    String area, class1, city, medium, subject;
    @Override
    public void setFilterCode(Object first, String code) {
        if(first.toString().equals("District")){
            city = code;
        }
        else if(first.toString().equals("Area")){
            area = code;
        }
        else if(first.toString().equals("Class")){
            class1 = code;
        }
        else if(first.toString().equals("Medium")){
            medium = code;
        }
        else if(first.toString().equals("Subject")){
            subject = code;
        }
    }

    public String toString() {
        return "Area: " + area + " City: " + city + " Medium: " + medium + " Class: " + class1 + " Subject: " + subject;
    }
}
