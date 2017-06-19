package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.sujoy.citi1.activities.HomeActivity;
import com.example.sujoy.citi1.UI.ResetPwdActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Authentication {
    final long valTime = 15;

    ArrayList<String> keys;
    String file = "showInfo.php", confFile = "regConfirmation.php";
    private static final String RESPWDFILE = "resPwd.php";
    String email="", name="", phone="", bio = "";
    Activity parent;

    String code;
    ResetPwdActivity resAct;

    public Authentication(){
        initKeys();
    }

    private void initKeys() {
        keys = new ArrayList<>();
        keys.add("email");
        keys.add("password");
    }




    public void verifyLoginCredentials(String email, String password, Activity act){
        ArrayList<String> vals = new ArrayList<>();
        vals.add(email);
        vals.add(password);
        this.email = email;
        parent = act;

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, file, act), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    if(result.length()==0){
                        Toast.makeText(parent,"Wrong Email/Password",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(parent,"Logged In",Toast.LENGTH_LONG).show();
                        try {
                            JSONObject collegeData = result.getJSONObject(0);
                            name = collegeData.getString("User Name");
                            phone = collegeData.getString("Phone Number");
                            bio = collegeData.getString("Bio");
                            login();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void resetPassword(String email, String pwd){
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();

        keys.add("email");
        keys.add("password");

        vals.add(email);
        vals.add(pwd);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, RESPWDFILE, resAct), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {

            }
        });
    }

    private void login(){
        User.setAttributes(email, name, phone, bio, parent);

        parent.finish();
        parent.startActivity(parent.getIntent());
    }

    public void logout(Activity act){
        User.removeAttributes();

        SharedPreferences shpr = act.getSharedPreferences("Authentication", Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = shpr.edit();
        ed.putString("email", "");
        ed.putString("name", "");
        ed.putString("phone", "");
        ed.putString("bio", "");
        ed.putBoolean("loggedIn", false);
        ed.putBoolean("admin", false);
        ed.commit();


        act.finish();
        act.startActivity(act.getIntent());
    }







    public void verifyInput(String email, String pwd, ResetPwdActivity act){
        resAct = act;
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();
        keys.add("email");
        vals.add(email);
        this.email = email;

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, confFile, act), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true"))
                    Toast.makeText(resAct,"Email does not exist",Toast.LENGTH_LONG).show();
                else{
                    resAct.showConfFragment();
                    sendConfCode();
                    Toast.makeText(resAct,"A Confirmation Code was sent to the email address you specified",Toast.LENGTH_LONG).show();
                    Toast.makeText(resAct,"The Confirmation Code is valid for " + valTime + " minutes",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public boolean verifyConfirmationCode(String str) {
        if(str.equals(code))
            return true;
        return false;
    }

    public void sendConfCode() {
        new SendMailTask().execute(email);
    }

    private String getConfCode() {
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int N = alphabet.length();

        Random rand = new Random();
        code="";
        for(int i=0; i<6; i++){
            code += alphabet.charAt(rand.nextInt(N));
        }
        resAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(valTime*60*1000, 12000) {
                    @Override
                    public void onTick(long l) {

                    }

                    public void onFinish() {
                        code="";
                        Toast.makeText(parent,"The Confirmation Code has expired",Toast.LENGTH_LONG).show();
                    }
                }.start();
            }
        });

        return code;
    }

    class SendMailTask extends AsyncTask {

        private ProgressDialog statusDialog;
        private Activity sendMailActivity;

        protected void onPreExecute() {
            statusDialog = new ProgressDialog(resAct);
            statusDialog.setMessage("Getting ready...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();
        }

        @Override
        protected Object doInBackground(Object... args) {
            try {
                Log.i("SendMailTask", "About to instantiate GMail...");

                publishProgress("Processing input....");
                CitiMail androidEmail = new CitiMail(args[0].toString(), getConfCode());
                publishProgress("Preparing mail message....");
                androidEmail.createEmailMessage();
                publishProgress("Sending email....");
                androidEmail.sendEmail();
                publishProgress("Email Sent.");

                Log.i("SendMailTask", "Mail Sent.");
            } catch (Exception e) {
                publishProgress(e.getMessage());
                Log.e("SendMailTask", e.getMessage(), e);
                resAct.startActivity(new Intent(parent, HomeActivity.class));
            }
            return "done";
        }

        @Override
        public void onProgressUpdate(Object... values) {
            statusDialog.setMessage(values[0].toString());
        }

        @Override
        public void onPostExecute(Object result) {
            statusDialog.dismiss();
        }

    }

    public void verifyResetPwdInput(String s, String s1, ResetPwdActivity resPwdAct) {
    }
}
