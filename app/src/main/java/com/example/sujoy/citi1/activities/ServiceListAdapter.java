package com.example.sujoy.citi1.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.ServiceFeature;

import java.util.ArrayList;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder>{

    private Context mContext;
    private ArrayList<ServiceFeature> items;
    private ClickCallback mCallback;
    private LinearLayout service;
    private  int size;


    public interface ClickCallback{
        void onItemClick(ServiceFeature s);
    }

    public ServiceListAdapter(Context mContext, ArrayList<ServiceFeature> items, ClickCallback mCallback, int size) {
        this.mContext = mContext;
        this.items = items;
        this.mCallback = mCallback;
        this.size = size;
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView logo;
        TextView name;
        ServiceFeature current;

        public ServiceViewHolder(View itemView) {
            super(itemView);

            logo = (ImageView) itemView.findViewById(R.id.servicelogo);
            name = (TextView) itemView.findViewById(R.id.servicename);

            service = (LinearLayout) itemView.findViewById(R.id.serviceGridView);
            service.setOnClickListener(this);
            service.getLayoutParams().height = size;
        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.serviceGridView:
                    startFilterPage();
                    break;
            }
        }


        public void startFilterPage(){

                if (mCallback != null) {
                    int position = getAdapterPosition();
                    current = (ServiceFeature) items.get(position);
                    mCallback.onItemClick(current);
                }
            }
        }


    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {

        ServiceFeature s = (ServiceFeature) items.get(position);
        holder.name.setText(s.getServiceName());
        holder.logo.setImageResource(s.getImageURL());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilter(ArrayList<ServiceFeature> services){

        this.items.clear();
        if(services == null) services = new ArrayList<ServiceFeature>();
        else this.items.addAll(services);

        notifyDataSetChanged();
    }
}
