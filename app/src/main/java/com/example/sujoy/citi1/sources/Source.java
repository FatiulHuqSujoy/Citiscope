package com.example.sujoy.citi1.sources;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.util.Pair;

import com.example.sujoy.citi1.UI.FilterActivity;
import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Database;
import com.example.sujoy.citi1.technical_classes.OptionCode;
import com.example.sujoy.citi1.technical_classes.RetrievalData;
import com.example.sujoy.citi1.technical_classes.Service;
import com.example.sujoy.citi1.technical_classes.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Source {
	String url;
	ArrayList<OptionCode> codes;

	Activity parent;
	Service service;
	public Boolean done = false;

	final String phpFile = "code.php";

	ArrayList<Agent> agentList;
	ProgressDialog loading;

	public Source(Activity act, Service srv){
		parent = act;
		System.out.println(parent);
		service = srv;
		agentList = new ArrayList<>();

		codes = new ArrayList<>();
		getCodes();
	}




	public abstract ArrayList<Agent> fetchResult(ArrayList<Pair<String, String>> chosenOptions) throws Exception;
	
	public void getCodes(){
		ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();

		keys.add("url");
		vals.add(getUrl());

		Database db = new Database();
		db.retrieve(new RetrievalData(keys, vals, phpFile, parent), true, new VolleyCallback() {
			@Override
			public void onSuccessResponse(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray result = jsonObject.getJSONArray("result");

					if(result.length()!=0){
						System.out.println(result + " X " + result.length());
						for(int i=0; i<result.length(); i++) {
							try {
								JSONObject arr = result.getJSONObject(i);
								codes.add(new OptionCode(arr.getString("Filter"), arr.getString("Option"), arr.getString("Code")));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					else{
						System.out.println("No Result");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				FilterActivity act = (FilterActivity) parent;
				act.enableSearch();
			}
		});
	}




	protected abstract String getUrl();

	protected void generateCodes(ArrayList<Pair<String, String>> chosenOptions, CitiFilter filter){
		for(Pair p : chosenOptions){
			String code = findCode((String)p.first, (String)p.second);
			filter.setFilterCode(p.first, code);
		}
	}

	private String findCode(String first, String second) {
		for(OptionCode op: codes){
			if(op.match(first, second)){
				return op.getCode();
			}
		}
		return  null;
	}

	protected void showProgress(){
		parent.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loading = ProgressDialog.show(parent,"Please wait...","Fetching results from " + getUrl(),false,false);
			}
		});
	}
}
