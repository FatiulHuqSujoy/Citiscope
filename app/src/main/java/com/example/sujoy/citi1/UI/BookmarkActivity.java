package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.activities.HomeActivity;

import java.util.ArrayList;

public class BookmarkActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    ArrayList<Agent> agents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleBookmark, new TitleFragment());
        fragmentTransaction.commit();

        recyclerView = (RecyclerView) findViewById(R.id.agRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        agents = intent.getBundleExtra("agent").getParcelableArrayList("agents");
        System.out.println(agents);

        showData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("a g e n t s > > " + agents);
        agents = null;
        Agent ag = new Agent();
        ag.createList(this);
    }

    public void showData(){
        adapter = new AgentCard(agents, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
