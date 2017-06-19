package com.example.sujoy.citi1.technical_classes;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.sujoy.citi1.UI.PollActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Poll{
    PollActivity parent;
    Activity act;

    ArrayList<Nomination> noms;

    ArrayList<String> keys, vals, tempSrcs;
    final String CRTPOLLFILE = "createPoll.php", NEWNOMFILE = "addNom.php", ADDSOURCESFILE = "addNomUrl.php";
    private String tempName;

    public Poll(PollActivity pollAct){
        parent = pollAct;
    }
    public Poll(){}

    public void createPoll(){
        noms = new ArrayList<>();

        keys = new ArrayList<>();
        vals = new ArrayList<>();

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, CRTPOLLFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    if(result.length()==0){
                        Toast.makeText(parent,"There are no Nominations",Toast.LENGTH_LONG).show();
                    }
                    else{
                        try {
                            for(int i=0; i<result.length(); i++){
                                JSONObject agentData = result.getJSONObject(i);
                                String name = agentData.getString("Name");
                                String desc = agentData.getString("Description");
                                String nom = agentData.getString("Nominator");
                                int votes = Integer.parseInt(agentData.getString("VoteCount"));

                                Nomination temp = new Nomination();
                                temp.setAttributes(name, desc, null, nom, votes);
                                noms.add(temp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sort();
            }
        });

    }

    public void sort(){
        Collections.sort(noms, new Comparator<Nomination>() {
            @Override
            public int compare(Nomination nom1, Nomination nom2) {
                return nom2.voteCount-nom1.voteCount;
            }
        });

        parent.showData(noms);
    }

    public void addNomination(String name, String desc, Activity parent, ArrayList<String> sources){
        act = parent;
        tempSrcs = sources;
        tempName = name;

        keys = new ArrayList<>();
        keys.add("name");
        keys.add("desc");
        keys.add("email");

        vals = new ArrayList<>();
        vals.add(name);
        vals.add(desc);
        vals.add(User.Email);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, NEWNOMFILE, act), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true")){
                    addSources(tempName, tempSrcs);

                    Toast.makeText(act, "Nomination added successfully", Toast.LENGTH_LONG);
                    Intent intent = new Intent(act, PollActivity.class);
                    act.startActivity(intent);
                }
                else{
                    Toast.makeText(act, "Nomination Name already exists", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void addSources(String name, ArrayList<String> tempSrcs) {
        for(String s: tempSrcs){
            keys = new ArrayList<>();
            keys.add("url");
            keys.add("name");

            vals = new ArrayList<>();
            vals.add(s);
            vals.add(tempName);

            Database db = new Database();
            db.retrieve(new RetrievalData(keys, vals, ADDSOURCESFILE, act), true, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {

                }
            });
        }

    }
}
