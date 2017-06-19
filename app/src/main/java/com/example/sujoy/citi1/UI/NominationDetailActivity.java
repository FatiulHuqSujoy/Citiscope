package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Database;
import com.example.sujoy.citi1.technical_classes.Nomination;
import com.example.sujoy.citi1.technical_classes.RetrievalData;
import com.example.sujoy.citi1.technical_classes.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NominationDetailActivity extends Activity {
    Nomination nom;
    TextView txtNomName, txtNomDesc;
    LinearLayout linLay;

    final String URLPHP = "nomUrl.php";
    Activity parent = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomination_detail);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleNomination, new TitleFragment());
        fragmentTransaction.commit();

        linLay = (LinearLayout) findViewById(R.id.urlLinLay);
        txtNomName = (TextView) findViewById(R.id.txtNominationName);
        txtNomDesc = (TextView) findViewById(R.id.txtNomDetailDesc);

        Intent intent = getIntent();
        nom = intent.getBundleExtra("nom").getParcelable("nomination");

        getSources();
        txtNomName.setText(nom.name);
        txtNomDesc.setText(nom.description);
    }

    private void getSources() {
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();
        keys.add("name");
        vals.add(nom.name);

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, URLPHP, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    if(result.length()!=0){
                        //System.out.println(result + " X " + result.length());
                        for(int i=0; i<result.length(); i++) {
                            try {
                                JSONObject arr = result.getJSONObject(i);
                                String url = arr.getString("Url");

                                TextView txtUrl = new TextView(parent);
                                txtUrl.setText(url);

                                LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layPar.setMargins(10, 0, 10, 10);
                                txtUrl.setLayoutParams(layPar);

                                txtUrl.setTextColor(Color.WHITE);
                                txtUrl.setLinkTextColor(Color.WHITE);
                                txtUrl.setTextSize(20);
                                Linkify.addLinks(txtUrl, Linkify.WEB_URLS);
                                txtUrl.setLinksClickable(true);
                                txtUrl.setSingleLine(true);

                                LinearLayout llUrl = new LinearLayout(parent);
                                llUrl.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams layPar2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layPar2.setMargins(10, 0, 10, 10);
                                llUrl.setLayoutParams(layPar2);

                                llUrl.addView(txtUrl);

                                linLay.addView(llUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        //System.out.println("No Result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
