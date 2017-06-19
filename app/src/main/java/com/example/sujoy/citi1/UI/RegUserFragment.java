package com.example.sujoy.citi1.UI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Authentication;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegUserFragment extends Fragment {
    Button btnPoll, btnBookmark, btnProfile, btnLogout;
    Activity parent;

    public RegUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_user, container, false);

        parent = getActivity();

        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnProfile = (Button) view.findViewById(R.id.btnProfile);
        btnBookmark = (Button) view.findViewById(R.id.btnSeeBookmarks);
        btnPoll = (Button) view.findViewById(R.id.btnPoll);

        setListeners();

        return view;
    }

    private void setListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authentication auth = new Authentication();
                auth.logout(getActivity());
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, ProfileActivity.class);
                parent.startActivity(intent);
            }
        });

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Agent ag = new Agent();
                ag.createList(parent);
            }
        });

        btnPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, PollActivity.class);
                parent.startActivity(intent);
            }
        });
    }

}
