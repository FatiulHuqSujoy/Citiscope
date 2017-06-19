package com.example.sujoy.citi1.UI;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sujoy.citi1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfCodeFragment extends Fragment {
    Button submit, resend, cancel;
    EditText code;
    ConfirmationActivity confAct;

    public ConfCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf_code, container, false);

        confAct = (ConfirmationActivity)getActivity();

        submit = (Button) view.findViewById(R.id.btnSubmitConfCode);
        resend = (Button) view.findViewById(R.id.btnResendConfCode);
        cancel = (Button) view.findViewById(R.id.btnCancelConfCode);

        code = (EditText) view.findViewById(R.id.eTxtConfCode);
        code.setHintTextColor(Color.WHITE);
        code.setTextColor(Color.WHITE);

        setListeners();
        return view;
    }

    private void setListeners(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confAct.cancelConfCode();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confAct.resendConfCode();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCode = code.getText().toString();

                if(strCode.equals("")){
                    Toast.makeText(confAct,"Please fill in the required field",Toast.LENGTH_LONG).show();
                }
                else{
                    confAct.validateConfCode(strCode.toUpperCase());
                }
            }
        });
    }
}
