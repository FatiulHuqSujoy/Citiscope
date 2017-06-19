package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Agent;

import java.util.ArrayList;

public class AgentListActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private String service;
    private TextView txtCaption;

    ArrayList<Agent> agents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_list);

        recyclerView = (RecyclerView) findViewById(R.id.agRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        agents = intent.getBundleExtra("agent").getParcelableArrayList("agents");
        service = intent.getStringExtra("service");

        txtCaption = (TextView) findViewById(R.id.txtAgListCaption);
        if(!service.equals("")){
            txtCaption.setText(txtCaption.getText().toString() + " (" + service + ")");
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleAgentList, new TitleFragment());
        fragmentTransaction.commit();

        System.out.println(agents);

        showData();
    }


    public void showData(){
        adapter = new AgentCard(agents, this);
        recyclerView.setAdapter(adapter);
    }
}
