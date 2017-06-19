package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Database;
import com.example.sujoy.citi1.technical_classes.RetrievalData;
import com.example.sujoy.citi1.technical_classes.User;
import com.example.sujoy.citi1.technical_classes.VolleyCallback;

import java.util.ArrayList;

public class AgentActivity extends Activity {
    Agent agent;
    TextView txtvName, txtvPhone1, txtvPhone2, txtvEmail, txtvLink, txtvMap;
    Button btnBookmark;
    Activity parent = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleAgent, new TitleFragment());
        fragmentTransaction.commit();

        Intent intent = getIntent();
        agent = (Agent) intent.getParcelableExtra("agent");

        if(agent!=null)
            setTexts();

        setListener();

    }

    private void setListener() {
        btnBookmark = (Button) findViewById(R.id.btnAgentBkmark);
        if(!User.loggedIn) btnBookmark.setEnabled(false);
        if(agent.id!=null)
            btnBookmark.setText("Unbookmark");

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnBookmark.getText().toString().equals("Bookmark")) {
                    User user = new User();
                    user.bookmark(agent, parent);
                }
                else {
                    User user = new User();
                    user.unbookmark(agent, parent);
                }
            }
        });
    }

    private void setTexts() {
        System.out.println(agent);

        txtvName = (TextView) findViewById(R.id.txtAgentName);
        txtvPhone1 = (TextView) findViewById(R.id.txtAgentPhone1);
        txtvPhone2 = (TextView) findViewById(R.id.txtAgentPhone2);
        txtvEmail = (TextView) findViewById(R.id.txtAgentEmail);
        txtvLink = (TextView) findViewById(R.id.txtAgentLink);
        txtvMap = (TextView) findViewById(R.id.txtAgentMap);

        txtvName.setText(agent.name);
        txtvPhone1.setText(agent.phone1);
        txtvPhone2.setText(agent.phone2);
        txtvEmail.setText(agent.email);
        txtvLink.setText(agent.url);
        txtvMap.setText(agent.address);

        txtvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, AgMapActivity.class);
                intent.putExtra("address", agent.address);
                parent.startActivity(intent);
            }
        });
    }
}
