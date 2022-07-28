package com.example.womensafetyadmin.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafetyadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ActivityChat extends AppCompatActivity {
    TextView emptyText;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String adminId;
    LinearLayoutManager manager;

    ArrayList<ClassChatList> chatList;
    AdapterChatList adapter;

    FirebaseDatabase database;
    DatabaseReference chatListRef;
    FirebaseFirestore fireStore;

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
        database = FirebaseDatabase.getInstance();
        chatListRef = database.getReference("Admin");
        fireStore = FirebaseFirestore.getInstance();

        manager = new LinearLayoutManager(ActivityChat.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        chatList = new ArrayList<>();
        chatListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    String userId = snap.getKey();
                    Log.e("User", userId);
                    ClassChatList chat = new ClassChatList(userId, "This is last message", "22 jul 10:50pm", userId);
                    chatList.add(chat);
                }
                if(chatList.size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityChat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new AdapterChatList(ActivityChat.this, chatList);
        recyclerView.setAdapter(adapter);

    }
}