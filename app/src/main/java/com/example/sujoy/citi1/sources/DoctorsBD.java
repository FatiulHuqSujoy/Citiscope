package com.example.sujoy.citi1.sources;

import android.app.Activity;
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

public class DoctorsBD extends Source {
    ArrayList <Agent> doctors = new ArrayList<Agent>();

    final String url = "www.doctorsbd.com", urlPre = "http://", urlPost = "/edoctor/doctorsAppoinment";

    public DoctorsBD(Activity act, Service srv) {
        super(act, srv);
    }

    @Override
    public ArrayList<Agent> fetchResult(ArrayList<Pair<String, String>> chosenOptions) throws Exception {
        DoctorFilter docFil = new DoctorFilter();
        generateCodes(chosenOptions, docFil);

        showProgress();

        URL url = new URL(urlPre + this.url + urlPost);

        Map<String,Object> params = new LinkedHashMap<>();

        params.put("csrf_token", "c4e223bfeceeaf683bfe9663af1edec6");
        params.put("specialty", docFil.speciality);
        params.put("district", docFil.city);
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
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");//"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        conn.setRequestProperty("Cookie", "csrf_cookie_name=c4e223bfeceeaf683bfe9663af1edec6; user_lang=en; ci_session=785f38f87c09290f3c96f0c53b6774746ad9054c" );
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
//        int time = conn.getConnectTimeout();
//        System.out.println(time);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

//        for (int c; (c = in.read()) >= 0;)
//            System.out.print((char)c);

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();

        parse(doctors, response);

        loading.dismiss();
        return doctors;
    }

    void parse(ArrayList<Agent>teachers, String response)
    {
        Pattern pattern1 = Pattern.compile("<img(.)src=(.)(.*?)(..)class=(.)img-thumbnail(...)style=(.)max-width:100%;(.)max-height:80px;(..)>");
        Pattern pattern2 = Pattern.compile("<h3 style=(.)padding:0;(.)margin:0;(.)line-height:17px;(.)margin-bottom:3px;(.)>(.*?)<(.)h3>");
        Pattern pattern3 = Pattern.compile("<h4(.)style=(.)padding:0;(.)margin:0;(.)line-height:17px;(.)>(.*?)(.)<br(..)>(.*?)<(.)h4>");
        Pattern pattern4 = Pattern.compile("<div>(.*?)<(.)div>");
        Pattern pattern5 = Pattern.compile("<a(.)href=(.)(.*)(.)>View(.)Profile<(.)a>");
        Matcher matcher5 = pattern5.matcher(response);
        Matcher matcher4 = pattern4.matcher(response);
        Matcher matcher3 = pattern3.matcher(response);
        Matcher matcher2 = pattern2.matcher(response);
        Matcher matcher1 = pattern1.matcher(response);

        while (matcher1.find() && matcher2.find() && matcher3.find() && matcher4.find() && matcher5.find()) {
            String name1 = matcher2.group(6);
            matcher4.find();
            String name4 = matcher4.group(1);
            String name5 = matcher5.group(3);
            System.out.println("-------");
            Agent temp = new Agent();
            temp.address=name4;
            String name=name1;
            String url=name5;

            String address="";

            temp.setAttr(name, "", "", url, address, "");
            teachers.add(temp);
            //System.out.println(temp.name + " " + temp.address+ "\n" + temp.detailsLink + " " + temp.photoLink);
            //System.out.println(name+" \n "+name1 + " \n " + name2 + " \n " + name6 + " \n " + name3 + " \n " + name4 + " \n " + name5 + " \n ");// + name6 + " \n " + name7 + " \n " + name8 + " \n " + name9);
            //System.out.println("-------");
        }
    }




    @Override
    protected String getUrl() {
        return this.url;
    }
}
