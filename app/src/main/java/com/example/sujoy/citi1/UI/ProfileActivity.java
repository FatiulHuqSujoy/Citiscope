package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.User;

public class ProfileActivity extends Activity {
    Button btnModProf, btnResetPwd;
    TextView txtEmail, txtName, txtPhone, txtBio;
    Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleProfile, new TitleFragment());
        fragmentTransaction.commit();

        setTexts();

        btnModProf = (Button) findViewById(R.id.btnModifyProfile);
        btnResetPwd = (Button) findViewById(R.id.btnResetPassword);
        setListeners();
    }

    private void setTexts() {
        txtEmail = (TextView) findViewById(R.id.txtProfileEmail);
        txtName = (TextView) findViewById(R.id.txtProfileUserName);
        txtPhone = (TextView) findViewById(R.id.txtProfilePhone);
        txtBio = (TextView) findViewById(R.id.txtProfileBio);

        txtEmail.setText(User.Email);
        txtName.setText(User.Name);
        txtPhone.setText(User.Phone);
        txtBio.setText(User.Bio);
    }

    private void setListeners() {
        btnModProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, ProfileModActivity.class);
                act.startActivity(intent);
            }
        });

        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, ResetPwdActivity.class);
                act.startActivity(intent);
            }
        });
    }
}
