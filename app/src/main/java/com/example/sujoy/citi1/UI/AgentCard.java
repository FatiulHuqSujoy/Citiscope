package com.example.sujoy.citi1.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Agent;

import java.util.ArrayList;

/**
 * Created by Sujoy on 4/21/2017.
 */

public class AgentCard extends RecyclerView.Adapter <AgentCard.ViewHolder>{
    private Activity parent;
    ArrayList<Agent> agents;

    public AgentCard(ArrayList<Agent> ag, Activity act){
        super();
        agents = ag;
        parent = act;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Agent ag =  agents.get(position);

        holder.agent = ag;
        holder.nameTxtV.setText(ag.getName());
        holder.linkTxtV.setText(ag.getLink());
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTxtV, linkTxtV;
        Agent agent;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTxtV = (TextView) itemView.findViewById(R.id.txtAgCardName);
            linkTxtV = (TextView) itemView.findViewById(R.id.txtAgCardLink);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(parent, agent.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(parent, AgentActivity.class);
                    intent.putExtra("agent", agent);
                    parent.startActivity(intent);
                }
            });
        }
    }
}
