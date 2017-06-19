package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.sujoy.citi1.activities.HomeActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Profile {
    ArrayList<String> keys;
    final String MODFILE = "profMod.php";
    Activity parent;
    String name, phone, bio;

    public Profile(){
        keys = new ArrayList<>();
        keys.add("email");
        keys.add("name");
        keys.add("phone");
        keys.add("bio");
    }








    public Boolean verifyInput(String name1, String phone1){
        Boolean correctPhone = verifyPhone(phone1);
        Boolean correctName = verifyName(name1);

        return correctName&&correctPhone;
    }

    public void updateDatabase(String name1, String phone1, String bio1, Activity act){
        parent = act;
        if(!verifyInput(name1, phone1)) {
            return;
        }

        ArrayList<String> vals = new ArrayList<>();
        vals.add(User.Email);
        if(name1.equals("")) name1 = User.Email;
        vals.add(name1);
        vals.add(phone1);
        vals.add(bio1);

        this.name = name1;
        this.phone = phone1;
        this.bio = bio1;

        parent = act;

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, MODFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("false"))
                    Toast.makeText(parent,"Sorry something went wrong. Try again.",Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(parent,"Your account has been successfully modiified",Toast.LENGTH_LONG).show();
                    User.setAttributes(User.Email, name, phone, bio, parent);
                    Intent intent = new Intent(parent, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    parent.startActivity(intent);
                    parent.finish();
                }
            }
        });
    }








    private Boolean verifyName(String name1) {
        System.out.println("n a m e > > " + name1);
        Pattern pat = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher mat = pat.matcher(name1);

        boolean incorrectName = mat.find();
        if(incorrectName)
            Toast.makeText(parent,"No special character allowed in Name",Toast.LENGTH_SHORT).show();

        if(name1.length()<2){
            Toast.makeText(parent,"Name has to be at least 2 characters long",Toast.LENGTH_SHORT).show();
            incorrectName = true;
        }
        return !incorrectName;
    }

    private Boolean verifyPhone(String phone1) {
        if(phone1.equals("")) return true;
        String prefix="";
        Pattern pat = Pattern.compile("(.*?)01[56789]{1}[0-9]{8}");
        Matcher mat = pat.matcher(phone1);
        if(mat.find()==true)
        {
            prefix = mat.group(1);
            if(prefix.equals("") || prefix.equals("+88")) return true;
        }
        Toast.makeText(parent,"Invalid Phone Number",Toast.LENGTH_LONG).show();
        return false;
    }
}
