package com.example.womensafetyadmin.Scholar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafetyadmin.Chat.ActivityChat;
import com.example.womensafetyadmin.Chat.AdapterChatList;
import com.example.womensafetyadmin.Chat.ClassChatList;
import com.example.womensafetyadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ActivityScholarList extends AppCompatActivity {

    TextView emptyText;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String adminId;
    LinearLayoutManager manager;

    ArrayList<ClassChatList> scholarList;
    AdapterScholarList adapter;

    FirebaseDatabase database;
    DatabaseReference scholarListRef;
    FirebaseFirestore fireStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar_list);
        Initialize();

    }
    private void Initialize() {
        emptyText = findViewById(R.id.scholarEmptyText);
        recyclerView = findViewById(R.id.scholarRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        adminId = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        scholarListRef = database.getReference("Scholar");
        fireStore = FirebaseFirestore.getInstance();

        manager = new LinearLayoutManager(ActivityScholarList.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        scholarList = new ArrayList<>();
        scholarListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scholarList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    String userId = snap.getKey();
                    Log.e("User", userId);
                    ClassChatList chat = new ClassChatList(userId, "This is last message", "22 jul 10:50pm", userId);
                    scholarList.add(chat);
                }
                if(scholarList.size() > 0){
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
                Toast.makeText(ActivityScholarList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new AdapterScholarList(ActivityScholarList.this, scholarList);
        recyclerView.setAdapter(adapter);

    }
}