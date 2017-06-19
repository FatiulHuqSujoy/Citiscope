package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Authentication;

public class LoginFragment extends Fragment {
    private EditText emailText, pwdText;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, viewGroup, false);

        emailText = (EditText) view.findViewById(R.id.etxtEmail);
        pwdText = (EditText) view.findViewById(R.id.etxtPassword);

        setListeners(view);

        return view;
    }

    private void setListeners(View view) {
        Button btnLogIn = (Button) view.findViewById(R.id.btnLoginSubmit);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = emailText.getText().toString();
                String txtPwd = pwdText.getText().toString();

                if (txtEmail.equals("") || txtPwd.equals("")) {
                    Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                Authentication auth = new Authentication();
                auth.verifyLoginCredentials(txtEmail, txtPwd, getActivity());
            }
        });

        TextView txtRegister = (TextView) view.findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity parent = getActivity();
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        TextView txtResPwd = (TextView) view.findViewById(R.id.txtResPwd);
        txtResPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity parent = getActivity();
                Intent intent = new Intent(getActivity(), ResetPwdActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean fieldUnentered(ArrayList<String> value) {
        for(String str: value){
            if(str.equals(""))
                return false;
        }
        return true;
    }
}
