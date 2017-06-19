package com.example.sujoy.citi1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.sujoy.citi1.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    Toolbar t;
    private TextView forgotPassword;
    private TextView createAccount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Sign In");

        t = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(t);

        forgotPassword = (TextView) findViewById(R.id.text_forgotPassword);
        forgotPassword.setOnClickListener(clickListener);

        createAccount = (TextView) findViewById(R.id.text_createAccount);
        createAccount.setOnClickListener(clickListener);

    }


    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.text_forgotPassword:
                    resetPassword();
                    break;
                case R.id.text_createAccount:
                    showRegisterPage();
                    break;
            }
        }
    };

    public void resetPassword(){
        Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivity(intent);
    }


    private void showRegisterPage() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

}

