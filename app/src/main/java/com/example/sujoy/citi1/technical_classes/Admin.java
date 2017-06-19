package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;

import com.example.sujoy.citi1.UI.PollActivity;

import java.util.ArrayList;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Admin extends User{
    private PollActivity parent;
    private final String RMVUSERPHP = "removeUser.php", RMVNOMPHP = "removeNom.php";
    private ArrayList<String> keys, vals;

    public Admin(PollActivity act){
        parent = act;
    }






    public void removeUser(String nominator){
        keys = new ArrayList<>();
        keys.add("email");

        vals = new ArrayList<>();
        vals.add(nominator);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, RMVUSERPHP, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                parent.finish();
                parent.startActivity(parent.getIntent());
            }
        });
    }

    public void removeNomination(String nomName){
        keys = new ArrayList<>();
        keys.add("name");

        vals = new ArrayList<>();
        vals.add(nomName);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, RMVNOMPHP, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                parent.finish();
                parent.startActivity(parent.getIntent());
            }
        });
    }
}
