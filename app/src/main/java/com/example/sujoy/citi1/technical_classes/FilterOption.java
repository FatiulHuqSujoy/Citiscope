package com.example.sujoy.citi1.technical_classes;

import java.util.ArrayList;

/**
 * Created by Sujoy on 5/2/2017.
 */

public class FilterOption {
    String filter;
    ArrayList<String> options;

    public FilterOption(String fil, String op) {
        filter = fil;
        options = new ArrayList<>();
        options.add(op);
    }

    public void add(String op){
        options.add(op);
    }

    @Override
    public String toString() {
        return filter + ": " + options;
    }
}
