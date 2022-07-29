package com.example.womensafetyadmin.Support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafetyadmin.R;
import com.example.womensafetyadmin.adapter.AdapterChatMessage;
import com.example.womensafetyadmin.Chat.ClassChatMessage;
import com.example.womensafetyadmin.model.ClassUserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ActivityLegalSupport extends AppCompatActivity {
    Toolbar toolbar;
    TextView title, emptyText;
    RecyclerView recyclerView;
    ImageView submit;
    EditText messageEditText;
    LinearLayoutManager manager;

    String adminId, userId, userName;
    ArrayList<ClassChatMessage> messageList;
    AdapterChatMessage adapter;

    FirebaseDatabase database, userDatabase;
    FirebaseAuth mAuth;
    DatabaseReference adminRef, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_support);
        Initialize();
        submit.setEnabled(false);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submit.setEnabled(!(s.length() == 0));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //submit.setEnabled(!messageEditText.getText().toString().equals(""));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                long timeInMills = System.currentTimeMillis();
//                String date = java.text.DateFormat.getDateTimeInstance().format(new Date());

                ClassChatMessage newMsg = new ClassChatMessage(message, "Admin", userId, adminId, adminId);
                Log.e("FireBase:", adminRef.toString());
                adminRef.child(String.valueOf(timeInMills)).setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            manager.setStackFromEnd(true);
//                            manager.setReverseLayout(false);
//                            recyclerView.setLayoutManager(manager);
                            recyclerView.scrollToPosition(messageList.size() - 1);
                            messageEditText.setText("");
                        }else{
                            Toast.makeText(ActivityLegalSupport.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityLegalSupport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void Initialize() {
        toolbar = findViewById(R.id.supportToolbar);
        title = findViewById(R.id.supportToolbarTitle);
        emptyText = findViewById(R.id.supportEmptyText);
        recyclerView = findViewById(R.id.supportRecyclerView);
        messageEditText = findViewById(R.id.supportEditText);
        submit = findViewById(R.id.supportSubmitBtn);

        manager = new LinearLayoutManager(ActivityLegalSupport.this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);


        userId = getIntent().getStringExtra("UserId");
        userName = getIntent().getStringExtra("UserName");
//        userId = "UserId";
        title.setText(userName);
        mAuth = FirebaseAuth.getInstance();
        adminId = mAuth.getUid();
        database = FirebaseDatabase.getInstance("https://womensafetyapp-2af0f-default-rtdb.firebaseio.com/");

        adminRef = database.getReference("LegalSupport").child(userId);
        messageList = new ArrayList<>();
        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
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
                Toast.makeText(ActivityLegalSupport.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new AdapterChatMessage(ActivityLegalSupport.this, messageList);
        recyclerView.setAdapter(adapter);

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ClassUserInfo userInfo = documentSnapshot.toObject(ClassUserInfo.class);
                assert userInfo != null;
                title.setText(userInfo.getName());
            }
        });
//        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}