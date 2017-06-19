package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Sujoy on 4/11/2017.
 */

public class RetrievalData {
    public ArrayList<String> key, value;
    public String file;
    Activity parent;

    public RetrievalData(ArrayList<String> k, ArrayList<String> v, String str, Activity act){
        key = k;
        value = v;
        file = str;
        parent = act;
    }

    public String toString(){
        String rd = file + " ii \n";
        for(int i=0; i<key.size(); i++){
            rd += "(" + key.get(i) + " , " + value.get(i) + ")\n";
        }
        rd += parent;

        return rd;
    }
}
