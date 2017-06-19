package com.example.sujoy.citi1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;


import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Database;
import com.example.sujoy.citi1.technical_classes.RetrievalData;
import com.example.sujoy.citi1.technical_classes.ServiceFeature;
import com.example.sujoy.citi1.technical_classes.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class HomeActivity extends BottomBarActivity implements ServiceListAdapter.ClickCallback, View.OnClickListener{
    private final String DISTRICTFILE = "districts.php";
    private LinearLayout linLay;
    private Spinner spnCity;

    Activity parent;
    ArrayList<String> serv;
    private String district;

    ServiceListAdapter adapter;
    ArrayList<ServiceFeature> items = new ArrayList<>();

    private RecyclerView serviceview;
    int size;



    public void addServices(){
        ServiceFeature s1 = new ServiceFeature("Tuition", R.drawable.desk);
        ServiceFeature s2 = new ServiceFeature("Apartment",R.drawable.flats);
        ServiceFeature s3 = new ServiceFeature("Blood Donation",R.drawable.blood);
        ServiceFeature s4 = new ServiceFeature("Doctor",R.drawable.greendoctor);

        items.add(s1);
        items.add(s2);
        items.add(s3);
        items.add(s4);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        ButterKnife.bind(this);
        addServices();

        spnCity = (Spinner) findViewById(R.id.spn_city);
        serviceview = (RecyclerView) findViewById(R.id.services);
        linLay = (LinearLayout) findViewById(R.id.serviceGridView);

        GridLayoutManager manager = new GridLayoutManager(this,2);
        serviceview.setLayoutManager(manager);
        ViewTreeObserver vto = serviceview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                serviceview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                size = serviceview.getMeasuredWidth();
                setRecycler();
            }
        });
        fillSpinner();
    }

    private void setRecycler() {
        adapter = new ServiceListAdapter(this, items, this, size / 2);
        serviceview.setAdapter(adapter);
        serviceview.addItemDecoration(new GridSpacingItemDecoration(2, 3, true));
    }







    private void fillSpinner() {
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
                district = parent.getItemAtPosition(pos).toString();
                getServices(district);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void getServices(String city) {
        serv = new ArrayList<>();

        ArrayList<String> vals = new ArrayList<>();
        vals.add(city);

        ArrayList<String> keys = new ArrayList<>();
        keys.add("district");

        String file = "serviceNames.php";

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, file, parent), true, new VolleyCallback() {
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

    private void createButtons() {
        items.clear();
        for(String s: serv){
            items.add(new ServiceFeature(s, R.drawable.desk));
        }

        setRecycler();
    }














    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
    }


    @Override
    public void onItemClick(ServiceFeature s) {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("servicename",s.getServiceName());
        startActivity(intent);
    }

}