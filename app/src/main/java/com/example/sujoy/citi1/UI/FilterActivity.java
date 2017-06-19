package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Service;
import com.example.sujoy.citi1.technical_classes.User;

import java.util.ArrayList;

public class FilterActivity extends Activity {
    String service;
    Button btnFilterConfirm;
    Service srv;
    LinearLayout ll;
    TextView txtCaption;
    private String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleFilter, new TitleFragment());
        fragmentTransaction.commit();

        Intent intent = getIntent();
        service = intent.getStringExtra("service");
        district = intent.getStringExtra("district");

        txtCaption = (TextView) findViewById(R.id.txtFilterCaption);
        txtCaption.setText(txtCaption.getText().toString()+ " (" + service + ")");
        ll = (LinearLayout) findViewById(R.id.ll_filters);

        setFilters();
        setListeners();
    }

    private void setListeners() {
        btnFilterConfirm = (Button)findViewById(R.id.btnFilterConfirm);
        btnFilterConfirm.setEnabled(false);

        btnFilterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.search(srv);
            }
        });
    }

    private void setFilters() {
        srv = new Service(service, this, district);
        srv.fetchFilters();
    }

    public void showResult(ArrayList<Agent> agents){
        System.out.println("AAAAARRRREEEHHHH\n" + agents);
        Intent intent = new Intent(this, AgentListActivity.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("agents", agents);
        intent.putExtra("agent", b);
        intent.putExtra("service", service);
        startActivity(intent);
    }

    public void enableSearch() {
        btnFilterConfirm.setEnabled(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ll.removeAllViews();
        srv = new Service(service, this, district);
        srv.fetchFilters();
    }
}
