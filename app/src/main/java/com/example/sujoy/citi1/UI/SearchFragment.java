package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Database;
import com.example.sujoy.citi1.technical_classes.RetrievalData;
import com.example.sujoy.citi1.technical_classes.User;
import com.example.sujoy.citi1.technical_classes.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private final String DISTRICTFILE = "districts.php";
    private LinearLayout linLay;
    private Spinner spnCity;

    Activity parent;
    ArrayList<String> serv;
    private String district;

    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, viewGroup, false);
        parent = getActivity();

        linLay = (LinearLayout) view.findViewById(R.id.ll_services);
        spnCity = (Spinner) view.findViewById(R.id.spn_city);

        fillSpinner(view);
        return view;
    }

    private void fillSpinner(View view) {
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, DISTRICTFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    ArrayAdapter<String> spnAdapter;
                    ArrayList<String> districts = new ArrayList<String>();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    if(result.length()!=0){
                        System.out.println(result + " X " + result.length());
                        for(int i=0; i<result.length(); i++) {
                            try {
                                JSONObject res = result.getJSONObject(i);
                                districts.add(res.getString("District"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spnAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item, districts);
                        spnCity.setAdapter(spnAdapter);
                    }

                    setSpinnerListener();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSpinnerListener() {
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                linLay.removeAllViews();
                String selected = parent.getItemAtPosition(pos).toString();
                district = selected;
                getServices(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createButtons() {
        //System.out.println(serv + " " + "SERV");
        ArrayList<String> services = serv;
        for(int j=0; j<services.size(); j++)
        {
            Button btn = new Button(getActivity());
            btn.setText(services.get(j));
            LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layPar.setMargins(0, 20, 0, 0);
            btn.setLayoutParams(layPar);
            btn.setBackgroundColor(ContextCompat.getColor(parent, R.color.CitiFont));
            linLay.addView(btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button btn = (Button) view;
                    String service = ((Button) view).getText().toString();

                    User user = new User();
                    user.selectService(service, district, getActivity());
                }
            });
        }
    }


    private void getServices(String city) {
        serv = new ArrayList<>();

        ArrayList<String> vals = new ArrayList<>();
        vals.add(city);

        ArrayList<String> keys = new ArrayList<>();
        keys.add("district");

        String file = "serviceNames.php";

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, file, getActivity()), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    if(result.length()!=0){
                        System.out.println(result + " X " + result.length());
                        for(int i=0; i<result.length(); i++) {
                            try {
                                JSONObject districts = result.getJSONObject(i);
                                serv.add(districts.getString("ServiceName"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    createButtons();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
