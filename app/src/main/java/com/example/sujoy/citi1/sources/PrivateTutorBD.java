package com.example.sujoy.citi1.sources;

import android.app.Activity;
import android.support.v4.util.Pair;

import com.example.sujoy.citi1.technical_classes.Agent;
import com.example.sujoy.citi1.technical_classes.Service;

import java.io.BufferedReader;
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

public class PrivateTutorBD extends Source {
    ArrayList <Agent> agents = new ArrayList<Agent>();
    final String url = "www.privatetutorbd.com", urlPre = "http://", urlPost = "/advance-search/en/";

    public PrivateTutorBD(Activity act, Service srv) {
        super(act, srv);
    }

    @Override
    public ArrayList<Agent> fetchResult(ArrayList<Pair<String, String>> chosenOptions) throws Exception {
        TuitionFilter tuiFil = new TuitionFilter();
        generateCodes(chosenOptions, tuiFil);

        System.out.println(tuiFil);

        showProgress();

        URL url = new URL(urlPre + this.url + urlPost);
        Map<String,Object> params = new LinkedHashMap<>();

        params.put("lookup", "teacher");
        //params.put("gender", 1);
        params.put("city", tuiFil.city);
        params.put("area[]", tuiFil.area);
        params.put("subject[]", tuiFil.subject);
        params.put("studentClass[]", tuiFil.class1);
        params.put("studyVersion[]", tuiFil.medium);
        params.put("teachingWay[]", 1);
        params.put("studentClassSelectionOptions", "any");
        params.put("urlToCitywiseAreas", "/locations/en/");
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

        parse(agents, response);
        loading.dismiss();
        return agents;
    }


    void parse(ArrayList<Agent>teachers, String response)
    {
        String result = "";
        int indexStart = response.indexOf("<div class="+"\""+"widget-inner"+"\""+">");
        int indexEnd = response.indexOf("</div> <!--  /.widget-inner -->");
        while (indexStart <= indexEnd) {
            result=result+response.charAt(indexStart);
            indexStart++;
        }
        result=result+"/div> <!--  /.widget-inner -->";
        System.out.println("*******" + result);

        Pattern pattern1 = Pattern.compile("<img(.)class=(.)img-thumbnail(..)src=(.)(.*?)(..)alt=(.)(.*?)(.)>");
        Pattern pattern2 = Pattern.compile("<span(.)class=(.)event-place small-text(.)><i class=(.)fa fa-globe(.)><(.)i>(.*?)<(.)span>");
        Pattern pattern3 = Pattern.compile("url:(..)http:(..)www(.)privatetutorbd(.)com(.)teacher(.)detail(.*)(.)");
        Pattern pattern4 = Pattern.compile("<b><i>Expected Remuneration<(.)i><(.)b>(.):(.)(.*?)<br><b>");
        Matcher matcher4 = pattern4.matcher(result);
        Matcher matcher3 = pattern3.matcher(result);
        Matcher matcher2 = pattern2.matcher(result);
        Matcher matcher1 = pattern1.matcher(result);

        while (matcher1.find() && matcher2.find() && matcher3.find()) {
            matcher3.find();
            String imgLink = "http://www.privatetutorbd.com"+matcher1.group(5); // Since (.*?) is capturing group 1
            String name = matcher1.group(8);
            String address = matcher2.group(7);
            String url = "http://www.privatetutorbd.com/teacher-detail"+matcher3.group(7);

            Agent temp = new Agent();
            temp.setAttr(name, "", "", url, address, "");
            teachers.add(temp);
        }
    }

    @Override
    protected String getUrl() {
        return this.url;
    }
}
