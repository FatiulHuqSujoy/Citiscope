package com.example.sujoy.citi1.sources;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.util.Pair;

import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sujoy on 5/1/2017.
 */

public class PropertyInBD extends Source {
    String transaction_type = "rent", min_bedrooms="1";
    ArrayList<Agent> agents = new ArrayList<Agent>();

    final String url = "www.propertyinbd.com", urlPre = "http://", urlPost = "/?s=+&page=1&min_area=&areaunit=&property_images=&property_type=";

    public PropertyInBD(Activity act, Service srv) {
        super(act, srv);
    }

    @Override
    protected String getUrl() {
        return this.url;
    }



    @Override
    public ArrayList<Agent> fetchResult(ArrayList<Pair<String, String>> chosenOptions) throws Exception {
        ApartmentRentFilter aptFil = new ApartmentRentFilter();
        generateCodes(chosenOptions, aptFil);

        showProgress();

        StringBuilder result = new StringBuilder();

        URL url = new URL(urlPre + this.url + urlPost + aptFil.propType+"&transaction_type="+transaction_type+"&property_locations="+aptFil.area+"&min_price="+"10000"+"&max_price="+"100000"+"&min_bedroom="+min_bedrooms+"&x=53&y=6");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        String response = result.toString();

        parse(agents, response);

        loading.dismiss();
        return agents;
    }

    void parse(ArrayList<Agent>agents, String response)
    {
        //System.out.println(result);
        Pattern pattern1 = Pattern.compile("<!--property listing details starts here-->(.*?)<h3>(.*?)<a(.)href=(.)(.*?)(.)>(.*?)<(.)a>(.*?)<(.)h3>(.*?)<p(.)class=(.*?)price(.)>(.*?)<(.)p>(.*?)<a(.*?)>more(.*?)<(.)a>(.*?)<img(.)src=(.)(.*?)(..)alt(.*?)>(.*?)<b>Contact(.)Name:<(.)b>(.*?)<br(..)>(.*?)<b>Contact(.)Number:(.)<(.)b>(.*?)<(.)p>(.*?)<!--property listing details ends here-->");
        Matcher matcher1 = pattern1.matcher(response);
        while (matcher1.find()) {
            Agent temp = new Agent();
            String url=matcher1.group(5);
            String address=matcher1.group(7);
            String name=matcher1.group(30).trim();
            String phone=matcher1.group(36).trim();
            String email="";

            if(address.startsWith("Apartment for Rent / Lease in")) address = address.substring(30);

            temp.setAttr(name, phone, "", url, address, email);
            agents.add(temp);
        }
    }
}
