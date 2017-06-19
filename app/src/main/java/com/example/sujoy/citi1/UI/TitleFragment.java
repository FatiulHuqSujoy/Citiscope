package com.example.sujoy.citi1.UI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sujoy.citi1.activities.HomeActivity;

import com.example.sujoy.citi1.R;

public class TitleFragment extends Fragment {
    TextView txtTitle;
    Activity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        parent = getActivity();

        txtTitle = (TextView) view.findViewById(R.id.titleTxt);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                parent.startActivity(intent);
                parent.finish();
            }
        });

        return view;
    }

}
