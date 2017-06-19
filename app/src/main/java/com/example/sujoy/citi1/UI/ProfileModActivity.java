package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Profile;
import com.example.sujoy.citi1.technical_classes.User;

public class ProfileModActivity extends Activity {
    EditText eTxtName, eTxtPhone, eTxtBio;
    Button btnModify, btnReset;
    Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_mod);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleProfMod, new TitleFragment());
        fragmentTransaction.commit();

        eTxtName = (EditText) findViewById(R.id.eTxtProfName);
        eTxtPhone = (EditText) findViewById(R.id.eTxtProfPhone);
        eTxtBio = (EditText) findViewById(R.id.eTxtProfBio);
        setBasicInput();

        btnModify = (Button) findViewById(R.id.btnModSubmit);
        btnReset =  (Button) findViewById(R.id.btnModReset);
        setListeners();
    }

    private void setListeners() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBasicInput();
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.editProfile(eTxtName.getText().toString().trim(),eTxtPhone.getText().toString().trim(),eTxtBio.getText().toString().trim(),act);
            }
        });
    }

    private void setBasicInput() {
        eTxtName.setText(User.Name);
        eTxtPhone.setText(User.Phone);
        eTxtBio.setText(User.Bio);
    }
}
