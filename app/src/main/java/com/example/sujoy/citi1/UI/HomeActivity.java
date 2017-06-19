//package com.example.sujoy.citi1.UI;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.example.sujoy.citi1.R;
//import com.example.sujoy.citi1.technical_classes.Authentication;
//import com.example.sujoy.citi1.technical_classes.User;
//
//import java.io.File;
//
//public class HomeActivity extends AppCompatActivity {
//    Fragment fr=null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.add(R.id.frmTitleHome, new TitleFragment());
//        fragmentTransaction.commit();
//
//        setContentView(R.layout.activity_home);
//
//        getSession();
//
//        Button btnTag2 = (Button) findViewById(R.id.btnHomeTag2);
//        if(User.loggedIn) btnTag2.setText("Options");
//
//
//        fm = getFragmentManager();
//        fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.add(R.id.fragment_home, new SearchFragment());
//        fragmentTransaction.commit();
//    }
//
//    private void getSession() {
//        SharedPreferences shpr=this.getSharedPreferences("Authentication", Activity.MODE_PRIVATE);
//        Boolean loggedIn = shpr.getBoolean("loggedIn", false);
//
//        if(loggedIn){
//            String email = shpr.getString("email", "");
//            String name = shpr.getString("name", "");
//            String phone = shpr.getString("phone", "");
//            String bio = shpr.getString("bio", "");
//            Boolean admin = shpr.getBoolean("admin", false);
//
//            User.setAttributes(email, name, phone, bio, this);
//        }
//    }
//
//    public void selectFrag(View view) {
//        if(view == findViewById(R.id.btnHomeTag1)) {
//            fr = new SearchFragment();
//        }else if(view == findViewById(R.id.btnHomeTag2)) {
//            if(User.loggedIn)
//                fr = new RegUserFragment();
//            else
//                fr = new LoginFragment();
//        }
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragment_home, fr);
//        //ft.addToBackStack(null);
//        ft.commit();
//    }
//
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            trimCache(this);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public static void trimCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            if (dir != null && dir.isDirectory()) {
//                deleteDir(dir);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        return dir.delete();
//    }
//
//
//}
