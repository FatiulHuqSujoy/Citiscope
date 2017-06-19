package com.example.sujoy.citi1.technical_classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.example.sujoy.citi1.UI.AgentActivity;
import com.example.sujoy.citi1.UI.BookmarkActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Agent implements Parcelable{
    public String name, phone1, phone2, url, address, email;


    public String id;
    ArrayList<String> keys, vals;
    final String BKFILE = "bookmark.php", BKLISTFILE = "bookmarkList.php", BKREMOVEFILE = "removeBookmark.php";
    private Activity parent;


    ArrayList<Agent> bookmarks;
    public Agent() {}





    public void addBookmarker(Activity act){
        parent = act;
        initiateArrays();

        Database db = new Database();

        db.update(new RetrievalData(keys, vals, BKFILE, act), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("false"))
                    Toast.makeText(parent,"Bookmark cannot be added",Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(parent,"Bookmark successfully added",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void removeBookmarker(Activity act) {
        parent = act;

        keys = new ArrayList<>();
        keys.add("id");
        vals = new ArrayList<>();
        vals.add(this.id);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, BKREMOVEFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("false"))
                    Toast.makeText(parent,"Bookmark cannot be removed",Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(parent,"Bookmark successfully removed",Toast.LENGTH_LONG).show();
                    parent.finish();
                }
            }
        });
    }

    public void createList(Activity act){
        parent = act;
        keys = new ArrayList<>();
        keys.add("bkEmail");
        vals = new ArrayList<>();
        vals.add(User.Email);

        bookmarks = new ArrayList<Agent>();

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, BKLISTFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    if(result.length()==0){
                        Toast.makeText(parent,"You have no bookmarks",Toast.LENGTH_LONG).show();
                    }
                    else{
                        try {
                            for(int i=0; i<result.length(); i++){
                                JSONObject agentData = result.getJSONObject(i);
                                String id = agentData.getString("AgentID");
                                String name = agentData.getString("Name");
                                String phn1 = agentData.getString("Phone1");
                                String phn2 = agentData.getString("Phone2");
                                String email = agentData.getString("Email");
                                String url = agentData.getString("Link");
                                String address = agentData.getString("Location");

                                Agent temp = new Agent();
                                temp.setAttr(name, phn1, phn2, url, address, email);
                                temp.setID(id);
                                bookmarks.add(temp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showList();
            }
        });
    }

    public void showList(){
        Intent intent = new Intent(parent, BookmarkActivity.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("agents", bookmarks);
        intent.putExtra("agent", b);
        parent.startActivity(intent);
        parent.finish();
    }





    private ArrayList<String> multiplePhones(String phone)
    {
        String phones[] = new String[]{};
        boolean onePhone = true;
        if(phone.matches("(.*?),(.*?)") || phone.matches("(.*?)[\\s]{1}(.*?)"))
        {
            phones = phone.split(",");
            onePhone = false;
        }

        ArrayList<String> tempPhoneNo = new ArrayList<String>(Arrays.asList(phones));
        ArrayList<String> phoneNo = new ArrayList<String>();
        if(onePhone==true) phoneNo.add(phone);
        for(String singlePhone : tempPhoneNo)
        {
            if(!singlePhone.equals("")) phoneNo.add(singlePhone.trim());
        }
        return phoneNo;
    }

    public void setAttr(String name, String phone1, String phone2, String url, String address, String email){
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.url = url;
        this.address = address;
        this.email = email;

        ArrayList<String> phones = multiplePhones(phone1);
        this.phone1 = phones.get(0);
        if(phones.size()>1)
            this.phone2 = phones.get(1);

        if(!this.phone1.startsWith("+88") && !this.phone1.equals("")) this.phone1 = "+88" + this.phone1;
        if(!this.phone2.startsWith("+88") && !this.phone2.equals("")) this.phone2 = "+88" + this.phone2;
    }


    private void initiateArrays() {
        keys = new ArrayList<>();
        vals = new ArrayList<>();

        keys.add("name");
        keys.add("phone1");
        keys.add("phone2");
        keys.add("email");
        keys.add("url");
        keys.add("location");
        keys.add("bkEmail");

        getAttributes();
        vals.add(User.Email);
    }

    public String toString(){
        return "Name: " + name + " ";
    }

    public void addSeq(int seq) {
        name += seq;
    }

    public boolean nameLess() {
        if(name.endsWith("#"))
            return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return url;
    }


    public void getAttributes() {
        vals.add(name);
        vals.add(phone1);
        vals.add(phone2);
        vals.add(email);
        vals.add(url);
        vals.add(address);
    }

    public void setID(String ID) {
        this.id = ID;
    }





    public Agent(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone1 = in.readString();
        phone2 = in.readString();
        url = in.readString();
        address = in.readString();
        email = in.readString();
    }

    public static final Creator<Agent> CREATOR = new Creator<Agent>() {
        @Override
        public Agent createFromParcel(Parcel in) {
            return new Agent(in);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(phone1);
        parcel.writeString(phone2);
        parcel.writeString(url);
        parcel.writeString(address);
        parcel.writeString(email);
    }
}
