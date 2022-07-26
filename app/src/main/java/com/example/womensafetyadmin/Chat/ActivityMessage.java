package com.example.womensafetyadmin.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.womensafetyadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;

public class ActivityMessage extends AppCompatActivity {
    Toolbar toolbar;
    TextView title, emptyText;
    RecyclerView recyclerView;
    ImageView submit;
    EditText messageEditText;

    String adminId, userId;
    ArrayList<ClassChatMessage> messageList;
    AdapterChatMessage adapter;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference adminRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Initialize();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                long timeInMills = System.currentTimeMillis();
                String date = java.text.DateFormat.getDateTimeInstance().format(new Date());

                ClassChatMessage newMsg = new ClassChatMessage(message, date, userId, adminId, adminId);
                adminRef.child(String.valueOf(timeInMills)).setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            messageEditText.setText("");
                        }else{
                            Toast.makeText(ActivityMessage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void Initialize() {
        toolbar = findViewById(R.id.messageToolbar);
        title = findViewById(R.id.messageToolbarTitle);
        emptyText = findViewById(R.id.messageEmptyText);
        recyclerView = findViewById(R.id.messageRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        submit = findViewById(R.id.messageSubmitBtn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityMessage.this));

        userId = getIntent().getStringExtra("UserId");
        mAuth = FirebaseAuth.getInstance();
        adminId = mAuth.getUid();
        database = FirebaseDatabase.getInstance();

        adminRef = database.getReference("Admin").child(adminId).child(userId);

        messageList = new ArrayList<>();
        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    ClassChatMessage msg = snap.getValue(ClassChatMessage.class);
                    messageList.add(msg);
                }
                if(messageList.size() != 0){
                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityMessage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new AdapterChatMessage(ActivityMessage.this, messageList);
        recyclerView.setAdapter(adapter);
    }
}