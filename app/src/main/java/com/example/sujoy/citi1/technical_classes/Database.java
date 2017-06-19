package com.example.sujoy.citi1.technical_classes;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Database {
    private final String PATH = "https://citiscope.000webhostapp.com/CITI/citi1/";
    private String file;
    private RetrievalData rd;
    private String toastMsg;

    private ProgressDialog loading;
    private String proMsg;

    private void accessDatabase(RetrievalData rd1, final VolleyCallback callback){
        String url = PATH + rd.file;
        //System.out.println("RD: " + rd);

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(loading!=null) loading.dismiss();
                        //deleteCache(rd.parent);
                        callback.onSuccessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError instanceof NetworkError) {
                            showToast("Cannot connect to Internet...Please check your connection!");
                        } else if (volleyError instanceof ServerError) {
                            showToast("The server could not be found. Please try again after some time!!");
                        } else if (volleyError instanceof AuthFailureError) {
                            showToast("Cannot connect to Internet...Please check your connection!");
                        } else if (volleyError instanceof ParseError) {
                            showToast("Parsing error! Please try again after some time!!");
                        } else if (volleyError instanceof NoConnectionError) {
                            showToast("Cannot connect to Internet...Please check your connection!");
                        } else if (volleyError instanceof TimeoutError) {
                            showToast("Connection TimeOut! Please check your internet connection.");
                        }
                        if(loading!=null) loading.dismiss();
                        //rd.parent.startActivity(new Intent(rd.parent, HomeActivity.class));
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                for(int i=0; i<rd.value.size(); i++){
                    params.put(rd.key.get(i), rd.value.get(i));
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(rd.parent);
        requestQueue.getCache().clear();
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


    public void retrieve(RetrievalData rd1, Boolean showProgress, VolleyCallback callback){
        rd = rd1;
        if(showProgress) showProgress("Fetching");
        accessDatabase(rd, callback);
    }

    public void update(RetrievalData rd1,  Boolean showProgress, VolleyCallback callback){
        rd = rd1;
        if(showProgress) showProgress("Updating");
        accessDatabase(rd, callback);
    }



    private void showProgress(String str){
        proMsg = str;
        rd.parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading = ProgressDialog.show(rd.parent,"Please wait...", proMsg + "...",false,false);
            }
        });
    }

    private void showToast(String str){
        toastMsg = str;
        rd.parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(rd.parent,toastMsg,Toast.LENGTH_LONG).show();
            }
        });
    }
}
