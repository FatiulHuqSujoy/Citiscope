package com.example.sujoy.citi1.sources;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.util.Pair;

import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;
import java.util.*;

public class DonateBloodBd extends Source {
	final String url = "www.donatebloodbd.com", urlPre = "http://", urlPost = "/save-people/result.html?catid=";

	public DonateBloodBd(Activity act, Service service) {
		super(act, service);
	}

	protected String getUrl() {
		return url;
	}


	public ArrayList<Agent> fetchResult(ArrayList<Pair<String, String>> chosenOptions) throws Exception {
    	BloodDonationFilter bldFil = new BloodDonationFilter();
		generateCodes(chosenOptions, bldFil);
		System.out.println(chosenOptions + " ______________________ " + bldFil.bGroup + " , " + bldFil.city);

		showProgress();

    	URL link = new URL(urlPre + url + urlPost + bldFil.bGroup);
    	Map<String,Object> params = new LinkedHashMap<>();
        
    	params.put("ad_city", bldFil.city);
    	params.put("new_search", "1");
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)link.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();

		agentList = parse(agentList, response);

		loading.dismiss();
		return agentList;
	}

    ArrayList<Agent> parse(ArrayList<Agent>agents, String response)
    {
    	Pattern pattern1 = Pattern.compile("<a(.)href=(.)(.*)(.)><img(.)class=(.)adimage(..)" +
											"src=(.)(.*)(..)alt=(.)nopic(...)><(.)a>(.*)<div>");

		Pattern pattern2 = Pattern.compile("<td class=(.)tdcenter(.)column_8(.)>(.*?)<br(.)>" +
											"<(.)td><td(.)class=(.)tdcenter(.)column_3(.)>" +
											"Area(....)(.*?)<br(.)>District(....)(.*?)<br(.)>" +
											"<(.)td><td(.)class=(.)tdcenter(.)column_9(.)>" +
											"(.*?)<br(.)><(.)td>");
		Matcher matcher2 = pattern2.matcher(response);
		Matcher matcher1 = pattern1.matcher(response);
		int i=0;
		while (matcher1.find() && matcher2.find()){
			String detailsLink = matcher1.group(3);
			String phone = matcher2.group(4);
			if(!phone.startsWith("+88")) phone = "+88" + phone;
			String address = matcher2.group(12)+","+matcher2.group(15);
			Agent temp = new Agent();

			String url= "http://www.donatebloodbd.com"+detailsLink;
			String phone1 = phone;
			String phone2 = "";
			String name = "Blood Donor #";
			String email="";

			temp.setAttr(name, phone, "", url, address, email);
			agents.add(temp);
		}
		return agents;
	}
}
