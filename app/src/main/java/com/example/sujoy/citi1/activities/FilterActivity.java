package com.example.sujoy.citi1.activities;

/**
 * Created by imm on 6/14/2017.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujoy.citi1.R;

public class FilterActivity extends AppCompatActivity {
    String service;
    Button btnFilterConfirm;
    //Service srv;
    LinearLayout ll;
    TextView txtCaption;
    private String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        String name = getIntent().getStringExtra("servicename");
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

      /*  FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleFilter, new TitleFragment());
        fragmentTransaction.commit();

        Intent intent = getIntent();
        service = intent.getStringExtra("service");
        district = intent.getStringExtra("district");

        txtCaption = (TextView) findViewById(R.id.txtFilterCaption);
        txtCaption.setText(txtCaption.getText().toString()+ " (" + service + ")");
        ll = (LinearLayout) findViewById(R.id.ll_filters);

        setFilters();
        setListeners();
*/    }

/*    private void setListeners() {
        btnFilterConfirm = (Button)findViewById(R.id.btnFilterConfirm);
        btnFilterConfirm.setEnabled(false);

        btnFilterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.search(srv);
            }
        });
    }

    private void setFilters() {
        srv = new Service(service, this, district);
        srv.fetchFilters();
    }

    public void showResult(ArrayList<Agent> agents){
        System.out.println("AAAAARRRREEEHHHH\n" + agents);
        Intent intent = new Intent(this, AgentListActivity.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("agents", agents);
        intent.putExtra("agent", b);
        intent.putExtra("service", service);
        startActivity(intent);
    }

    public void enableSearch() {
        btnFilterConfirm.setEnabled(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ll.removeAllViews();
        srv = new Service(service, this, district);
        srv.fetchFilters();
    }*/
}
