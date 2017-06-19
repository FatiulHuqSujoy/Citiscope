package com.example.sujoy.citi1.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujoy.citi1.R;
import com.example.sujoy.citi1.technical_classes.Admin;
import com.example.sujoy.citi1.technical_classes.Database;
import com.example.sujoy.citi1.technical_classes.Nomination;
import com.example.sujoy.citi1.technical_classes.RetrievalData;
import com.example.sujoy.citi1.technical_classes.User;
import com.example.sujoy.citi1.technical_classes.VolleyCallback;

import java.util.ArrayList;

/**
 * Created by Sujoy on 4/24/2017.
 */
public class NominationCard extends RecyclerView.Adapter <NominationCard.ViewHolder>{
    PollActivity parent;
    ArrayList<Nomination> nominations;

    public NominationCard(ArrayList<Nomination> noms, PollActivity pollActivity) {
        parent = pollActivity;
        nominations = noms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nominator_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Nomination nom = nominations.get(position);

        holder.setTexts(nom);
    }

    @Override
    public int getItemCount() {
        return nominations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTxtV, descTxtV;
        public EditText eTxtVoteCount;
        public Button btnVote, btnDetails;
        public LinearLayout linLay;
        Nomination nom;
        private static final String ADMINPHP = "admin.php";

        final String VOTECHECKFILE = "voteCheck.php", VOTEFILE = "vote.php", UNVOTEFILE = "unvote.php";

        ArrayList<String> keys, vals;
        private String tempPath;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTxtV = (TextView) itemView.findViewById(R.id.txtNomCardName);
            descTxtV = (TextView) itemView.findViewById(R.id.txtNomCardDesc);
            eTxtVoteCount = (EditText) itemView.findViewById(R.id.txtNomCardVoteCount);
            btnVote = (Button) itemView.findViewById(R.id.btnNomCardVote);
            btnDetails = (Button) itemView.findViewById(R.id.btnNomCardDetails);
            linLay = (LinearLayout) itemView.findViewById(R.id.llAdminBtn);
        }

        public void setTexts(final Nomination nom) {
            this.nom = nom;
            nameTxtV.setText(nom.name);
            descTxtV.setText(nom.description);
            eTxtVoteCount.setText(String.valueOf(nom.voteCount));

            setButton();

            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(parent, NominationDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("nomination", nom);
                    intent.putExtra("nom", b);
                    parent.startActivity(intent);
                }
            });

            if(User.admin)
                setAdminButtons();
        }

        private void setAdminButtons() {
            linLay.removeAllViews();

            Button btn = new Button(parent);
            btn.setText("Remove User");
            LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layPar.setMargins(15, 10, 50, 10);
            btn.setLayoutParams(layPar);
            btn.setBackgroundColor(Color.RED);
            btn.setPadding(25,0,25,0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAdmin(nom.nominator);
                }
            });
            linLay.addView(btn);

            btn = new Button(parent);
            btn.setText("Remove Nomination");
            layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layPar.setMargins(0, 10, 10, 10);
            btn.setLayoutParams(layPar);
            btn.setBackgroundColor(Color.RED);
            btn.setPadding(5,0,5,0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Admin admin = new Admin(parent);
                    admin.removeNomination(nom.name);
                }
            });
            linLay.addView(btn);
        }

        private void checkAdmin(String email) {
            ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();
            keys.add("email");
            vals.add(email);

            Database db = new Database();
            db.retrieve(new RetrievalData(keys, vals, ADMINPHP, parent), true, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    if(result.equals("true"))
                        Toast.makeText(parent, "Sorry you cannot remove Admin Profile", Toast.LENGTH_SHORT).show();
                    else {
                        Admin admin = new Admin(parent);
                        admin.removeUser(nom.nominator);
                    }
                }
            });
        }

        private void setButton() {
            keys = new ArrayList<>();
            vals = new ArrayList<>();

            keys.add("email");
            keys.add("name");

            vals.add(User.Email);
            vals.add(nom.name);

            Database db = new Database();
            db.retrieve(new RetrievalData(keys, vals, VOTECHECKFILE, parent), false, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    if(result.equals("false"))
                        btnVote.setText("Vote");
                    else{
                        btnVote.setText("Unvote");
                    }

                    setVoteListener();
                }
            });

        }

        private void setVoteListener() {
            keys = new ArrayList<>();
            vals = new ArrayList<>();

            keys.add("email");
            keys.add("name");

            vals.add(User.Email);
            vals.add(nom.name);

            btnVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btnVote.getText().toString().equals("Vote")){
                        tempPath = VOTEFILE;
                    }
                    else{
                        tempPath = UNVOTEFILE;
                    }

                    User user = new User();
                    user.vote(nom, tempPath, parent);
                }
            });
        }
    }
}
