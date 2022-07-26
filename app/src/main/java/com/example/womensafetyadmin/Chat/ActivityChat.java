package com.example.womensafetyadmin.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.womensafetyadmin.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityChat extends AppCompatActivity {
    TextView emptyText;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Initialize();
    }

    private void Initialize() {
        emptyText = findViewById(R.id.chatEmptyText);
        recyclerView = findViewById(R.id.chatRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        adminId = mAuth.getCurrentUser().getUid();
        emptyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityChat.this, ActivityMessage.class);
                intent.putExtra("UserId", "UserId");
                startActivity(intent);
            }
        });

    }
}