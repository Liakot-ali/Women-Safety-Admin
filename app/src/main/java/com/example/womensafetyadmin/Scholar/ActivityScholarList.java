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
import com.example.womensafetyadmin.model.ClassUserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActivityScholarList extends AppCompatActivity {

    TextView emptyText;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String adminId;
    LinearLayoutManager manager;

    ArrayList<ClassChatList> scholarList;
    ArrayList<String> nameList;
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

        nameList = new ArrayList<>();
        nameList = getIntent().getStringArrayListExtra("nameList");

        scholarList = new ArrayList<>();
        scholarListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scholarList.clear();
                int i = 0;
                for(DataSnapshot snap : snapshot.getChildren()){
                    String userId = snap.getKey();
                    assert userId != null;
                    String name = "";
                    name = nameList.get(i++);
                    Log.e("User", name);
                    ClassChatList chat = new ClassChatList(name, "This is last message", "22 jul 10:50pm", userId);
                    scholarList.add(chat);
                }
                Log.e("User","ArraySize" + scholarList.size());
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