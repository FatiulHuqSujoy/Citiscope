package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Nomination;
import com.example.sujoy.citi1.technical_classes.Poll;

import java.util.ArrayList;

import static com.example.sujoy.citi1.R.attr.layoutManager;

public class PollActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    Button btnAddNewNom;

    ArrayList<Nomination> nominations;
    public Poll poll;

    Activity parent = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitlePoll, new TitleFragment());
        fragmentTransaction.commit();

        nominations = new ArrayList<>();
        setListener();

        recyclerView = (RecyclerView) findViewById(R.id.pollRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NominationCard(nominations, this);
        recyclerView.setAdapter(adapter);

        poll = new Poll(this);
        poll.createPoll();
    }

    private void setListener() {
        btnAddNewNom = (Button) findViewById(R.id.btnAddNewNom);
        btnAddNewNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, NewNomActivity.class);
                parent.startActivity(intent);
            }
        });
    }


    public void showData(ArrayList<Nomination> noms){
        nominations = noms;

        adapter = new NominationCard(nominations, this);
        recyclerView.swapAdapter(adapter, true);
    }
}
