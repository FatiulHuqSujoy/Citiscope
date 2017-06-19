package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.sujoy.citi1.activities.HomeActivity;
import com.example.sujoy.citi1.UI.RegistrationActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Registration {
    String code;
    final long valTime = 15;


    ArrayList<String> keysReg, keysConf;
    String regFile = "reg.php", confFile = "regConfirmation.php";
    String email="", password="";
    RegistrationActivity parent;

    public Registration(){
        initKeys();
    }

    private void initKeys() {
        keysReg = new ArrayList<>();
        keysReg.add("email");
        keysReg.add("password");

        keysConf = new ArrayList<>();
        keysConf.add("email");
    }










    public void verifyRegistrationInput(String email, String pwd, RegistrationActivity act){
        parent = act;
        ArrayList<String> vals = new ArrayList<>();
        vals.add(email);
        this.email = email;
        password = pwd;

        Database db = new Database();
        db.retrieve(new RetrievalData(keysConf, vals, confFile, act), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("false"))
                    Toast.makeText(parent,"Email Already Exists",Toast.LENGTH_LONG).show();
                else{
                    parent.showConfFragment();
                    sendConfCode();
                    Toast.makeText(parent,"A Confirmation Code was sent to the email address you specified",Toast.LENGTH_LONG).show();
                    Toast.makeText(parent,"The Confirmation Code is valid for " + valTime + " minutes",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Boolean verifyConfirmationCode(String str){
        if(str.equals(code))
            return true;
        return false;
    }

    public void register(){
        ArrayList<String> vals = new ArrayList<>();
        vals.add(email);
        vals.add(password);

        Database db = new Database();
        db.update(new RetrievalData(keysReg, vals, regFile, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("false"))
                    Toast.makeText(parent,"Sorry something went wrong. Try again.",Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(parent,"You are successfully registered",Toast.LENGTH_LONG).show();
                    login();
                }
            }
        });
    }

    public void login(){
        User.setAttributes(email,email,"","", parent);
    }





    public void sendConfCode(){
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
        parent.runOnUiThread(new Runnable() {
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
            statusDialog = new ProgressDialog(parent);
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
                parent.startActivity(new Intent(parent, HomeActivity.class));
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
}
