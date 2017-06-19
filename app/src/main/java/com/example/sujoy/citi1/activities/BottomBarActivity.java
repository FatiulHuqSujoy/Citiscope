package com.example.sujoy.citi1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.example.sujoy.citi1.R;

import butterknife.OnClick;
import butterknife.Optional;

public class BottomBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_in:
                showLoginPage();
                return true;
            case R.id.action_sign_out:

                return true;
            case R.id.action_sign_up:
                showRegisterPage();
                return true;
            case R.id.action_password_change:
                // password change option selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLoginPage() {

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }


    private void showRegisterPage() {

        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);

    }


//    @Optional
//    @OnClick(R.id.img_notification)
//    public void showNotification() {
//        if(!(this instanceof NotificationActivity)) {
//            startActivity(new Intent(this,NotificationActivity.class));
//            finish();
//        }
//    }

    @Optional
    @OnClick(R.id.img_home)
    public void showHome() {
        if(!(this instanceof com.example.sujoy.citi1.activities.HomeActivity)) {
            startActivity(new Intent(this, com.example.sujoy.citi1.activities.HomeActivity.class));
            finish();
        }
    }
//
//    @Optional
//    @OnClick(R.id.img_profile)
//    public void showProfile() {
//        if(!(this instanceof ProfileActivity)) {
//            startActivity(new Intent(this,ProfileActivity.class));
//            finish();
//        }
//    }
//
//    @Optional
//    @OnClick(R.id.img_poll)
//    public void showPoll() {
//        if(!(this instanceof PollActivity)) {
//            startActivity(new Intent(this,PollActivity.class));
//            finish();
//        }
//    }

}
