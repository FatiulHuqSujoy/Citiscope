package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.sujoy.citi1.UI.FilterActivity;
import com.example.sujoy.citi1.UI.PollActivity;

import java.util.ArrayList;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class User {
    public static String Email, Name, Phone, Bio;

    public static Boolean loggedIn = false, admin = false;

    private static final String ADMINPHP = "admin.php";

    private static Activity parent;



    public void selectService(String service, String district, Activity activity){
        Intent intent = new Intent(activity, FilterActivity.class);
        intent.putExtra("service", service);
        intent.putExtra("district", district);
        activity.startActivity(intent);
    }

    public void search(Service srv) {
        srv.fetchResult();
    }

    public void bookmark(Agent agent, Activity parent){
        agent.addBookmarker(parent);
    }

    public void unbookmark(Agent agent, Activity parent){
        agent.removeBookmarker(parent);
    }

    public void vote(Nomination nom, String tempPath, PollActivity parent){
        nom.updateVote(tempPath, parent);
    }

    public void addNomination(String name, String desc, Activity parent, ArrayList<String> sources){
        Poll poll = new Poll();
        poll.addNomination(name, desc, parent, sources);
    }

    public void editProfile(String name, String phone, String bio, Activity act){
        Profile prof = new Profile();
        prof.updateDatabase(name, phone, bio, act);
    }

    public void resetPassword(String name, String pwd, Authentication auth){
        auth.resetPassword(name, pwd);
    }







    public static void setAttributes(String email, String name, String phone, String bio, Activity act){
        parent = act;

        loggedIn = true;
        User.Email = email;
        User.Name = name;
        User.Phone = phone;
        User.Bio = bio;

        checkIfAdmin();
        System.out.println("Log In Successful");
    }

    private static void checkIfAdmin() {
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();
        keys.add("email");
        vals.add(Email);

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, ADMINPHP, parent), false, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true"))
                    admin = true;
                else
                    admin = false;
                System.out.println("ADMIN: " + admin + "    RESULT: " + result);

                addSession();
            }
        });
    }

    private static void addSession() {
        SharedPreferences shpr = parent.getSharedPreferences("Authentication", Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = shpr.edit();
        ed.putString("email", User.Email);
        ed.putString("name", User.Name);
        ed.putString("phone", User.Phone);
        ed.putString("bio", User.Bio);
        ed.putBoolean("loggedIn", true);
        ed.putBoolean("admin", User.admin);

        System.out.println("SHAREDPREF: " + ed);

        ed.commit();
    }

    public static void removeAttributes() {
        loggedIn = false;
        User.Email = "";
        User.Name = "";
        User.Phone = "";
        User.Bio = "";
        System.out.println("Log Out Successful");
    }
}
