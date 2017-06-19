package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewNomActivity extends Activity {
    EditText eTxtName, eTxtDesc;
    Button btnAddUrl, btnAddNom;
    LinearLayout llNewUrl;
    Activity parent = this;

    ArrayList<EditText> edtSrcs;
    ArrayList<String> sources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nom);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleNewNom, new TitleFragment());
        fragmentTransaction.commit();

        eTxtName = (EditText) findViewById(R.id.txtNewNomName);
        eTxtDesc = (EditText) findViewById(R.id.txtNewNomDesc);

        btnAddUrl = (Button) findViewById(R.id.btnAddUrl);
        btnAddNom = (Button) findViewById(R.id.btnNewNomConfirm);

        llNewUrl = (LinearLayout) findViewById(R.id.llSrc);

        edtSrcs = new ArrayList<>();
        edtSrcs.add((EditText) llNewUrl.getChildAt(0));
        edtSrcs.add((EditText) llNewUrl.getChildAt(1));

        setListeners();
    }

    private void setListeners() {
        btnAddNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eTxtName.getText().toString().trim(), desc = eTxtDesc.getText().toString().trim();
                sources = getSources();
                if(name.equals("") || desc.equals("") || sources.size()<2){
                    Toast.makeText(parent,"Please fill in the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User();
                    user.addNomination(name, desc, parent, sources);
                }

            }
        });

        btnAddUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtTemp = new EditText(parent);
                edtTemp.setHint("URL");
                LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                edtTemp.setLayoutParams(layPar);
                setEditTextMaxLength(edtTemp, 49);
                llNewUrl.addView(edtTemp);
                edtSrcs.add(edtTemp);
            }
        });
    }

    public void setEditTextMaxLength(EditText edt, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        edt.setFilters(FilterArray);
    }


    private ArrayList<String> getSources() {
        ArrayList<String> temp = new ArrayList<>();

        for(EditText eTxt: edtSrcs){
            temp.add(eTxt.getText().toString());
            System.out.println(">>>> " + temp.get(temp.size()-1));
        }

        temp = filterSources(temp);

        return temp;
    }

    private ArrayList<String> filterSources(ArrayList<String> temp) {
        ArrayList <String> newUrls = new ArrayList<String>();
        Pattern pat = Pattern.compile("(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");
        for(String s : temp)
        {
            if(!s.startsWith("https://") && !s.startsWith("http://") &&  !s.startsWith("ftp://"))
                s = "https://" + s;
            Matcher mat = pat.matcher(s);
            if(mat.find() && !newUrls.contains(s)) newUrls.add(s);
        }

        System.out.println("sources:::: " + newUrls);
        return newUrls;
    }
}
