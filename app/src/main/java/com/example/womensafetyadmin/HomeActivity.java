package com.example.womensafetyadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView scholar,complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scholar = (CardView) findViewById(R.id.scholarid);
        complaint =(CardView) findViewById(R.id.complaintid);


        scholar.setOnClickListener(this);
        complaint.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.scholarid : i= new Intent(this,Scholar.class);startActivity(i); break;
//            case R.id.policeid : i= new Intent(this,NearbyPolice.class);startActivity(i); break;

            default:break;
        }


    }

    }
