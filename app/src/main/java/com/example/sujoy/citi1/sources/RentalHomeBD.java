package com.example.sujoy.citi1.sources;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.util.Pair;

import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sujoy on 5/1/2017.
 */

public class RentalHomeBD extends Source{
    ArrayList <Agent> agents = new ArrayList<Agent>();

    final String url = "www.rentalhomebd.com", urlPre = "http://", urlPost = "/searchrent/en/";

    public RentalHomeBD(Activity act, Service srv) {
        super(act, srv);
    }

    @Override
    protected String getUrl() {
        return url;
    }




    public ArrayList<Agent> fetchResult(ArrayList<Pair<String, String>> chosenOptions) throws Exception {
        ApartmentRentFilter aptFil = new ApartmentRentFilter();
        generateCodes(chosenOptions, aptFil);

        System.out.println(aptFil);

        showProgress();

        URL url = new URL(urlPre + this.url + urlPost);

        Map<String,Object> params = new LinkedHashMap<>();

        params.put("parent_property_type", 10);
        params.put("property_type", aptFil.propType);
        //Area
        params.put("district", aptFil.city);
        params.put("area[]", aptFil.area);
        //Price
        params.put("min_price", 10000);
        params.put("max_price", 100000);
        //Search
        params.put("submit", "Search");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
        //System.out.println(response);

        parse(agents, response);

        loading.dismiss();
        return agents;
    }

    void parse(ArrayList<Agent>houses, String response)
    {
        String result = "";
        int indexStart = response.indexOf("<div class="+"\""+"block-content"+"\""+"><!--List Items-->");
        int indexEnd = response.indexOf("</div><!--List Items-->");
        while (indexStart <= indexEnd) {
            if(response.charAt(indexStart)!='\n')result=result+response.charAt(indexStart);
            indexStart++;
        }
        result=result+"/div><!--List Items-->";
        //System.out.println(result);
        Pattern pattern1 = Pattern.compile("<address>(.*?)(..)address>");
        Pattern pattern2 = Pattern.compile("<li><i class=(.)fa(.)fa-arrows-alt(.)><(.)i><span>(.*?)&nbsp;<(.)span><(.)li>");
        Pattern pattern3 = Pattern.compile("<div(.)class=(.)title(.)>(.*?)(.[|])(.*?)<(.)div>(.*?)<div(.)class=(.)attribute(.)>");
        Pattern pattern4 = Pattern.compile("<a class=(.)btn(.)btn-primary(.)btn-detail(..)href=(.)(.*?)(.)>Detail<(.)a>");
        Pattern pattern5 = Pattern.compile("<img(.*?)src=(.)(.*?)(.)>");
        Matcher matcher5 = pattern5.matcher(result);
        Matcher matcher4 = pattern4.matcher(result);
        Matcher matcher3 = pattern3.matcher(result);
        Matcher matcher2 = pattern2.matcher(result);
        Matcher matcher1 = pattern1.matcher(result);
        while (matcher1.find() && matcher2.find() && matcher3.find() && matcher4.find() && matcher5.find()) {
            Agent temp = new Agent();
            String address = matcher1.group(1);
            String url="http://www.rentalhomebd.com"+matcher4.group(6);
            System.out.println(url);

            String name = matcher3.group(4).trim();
            String email = "";
            String phone = "";

            System.out.println("-------");
			System.out.println(name+" \n "+ url + " \n " + address);
			System.out.println("-------");

            temp.setAttr(name, phone, "", url, address, email);
            houses.add(temp);
        }
    }
}
