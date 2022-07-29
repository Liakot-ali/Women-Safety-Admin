package com.example.womensafetyadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.womensafetyadmin.Chat.ActivityChat;
import com.example.womensafetyadmin.Scholar.ActivityScholarList;
import com.example.womensafetyadmin.Support.ActivitySupportList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView scholar, complaint, chat, legalSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scholar = (CardView) findViewById(R.id.scholarid);
        complaint = (CardView) findViewById(R.id.complaintid);
        chat = (CardView) findViewById(R.id.chatId);
        legalSupport = (CardView) findViewById(R.id.legalSupportId);


        scholar.setOnClickListener(this);
        complaint.setOnClickListener(this);
        chat.setOnClickListener(this);
        legalSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.scholarid:
                i = new Intent(this, ActivityScholarList.class);
                startActivity(i);
                break;
            case R.id.complaintid:
                i = new Intent(this, Complaint.class);
                startActivity(i);
                break;
            case R.id.chatId:
                i = new Intent(this, ActivityChat.class);
                startActivity(i);
                break;
            case R.id.legalSupportId:
                i = new Intent(this, ActivitySupportList.class);
                startActivity(i);

            default:
                break;
        }
    }

}
