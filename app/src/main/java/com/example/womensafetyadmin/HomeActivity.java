package com.example.womensafetyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.womensafetyadmin.Chat.ActivityChat;
import com.example.womensafetyadmin.Scholar.ActivityScholarList;
import com.example.womensafetyadmin.Support.ActivitySupportList;
import com.example.womensafetyadmin.model.ClassUserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView scholar, complaint, chat, legalSupport;
    ArrayList<String> scholarList, chatList, supportList;
    ProgressBar dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scholar = (CardView) findViewById(R.id.scholarid);
        complaint = (CardView) findViewById(R.id.complaintid);
        chat = (CardView) findViewById(R.id.chatId);
        legalSupport = (CardView) findViewById(R.id.legalSupportId);
        dialog = findViewById(R.id.progressBar);

        scholarList = new ArrayList<>();
        chatList = new ArrayList<>();
        supportList = new ArrayList<>();

        dialog.setVisibility(View.VISIBLE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Scholar");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scholarList.clear();
                for (DataSnapshot snap : snapshot.getChildren()){
                    String userId = snap.getKey();
                    Log.e("User", userId);
                    assert userId != null;
                    DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ClassUserInfo userInfo = documentSnapshot.toObject(ClassUserInfo.class);
                            assert userInfo != null;
                            scholarList.add(userInfo.getName());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.setVisibility(View.GONE);

                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference supportRef = FirebaseDatabase.getInstance().getReference("LegalSupport");
        supportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                supportList.clear();
                for (DataSnapshot snap : snapshot.getChildren()){
                    String userId = snap.getKey();
                    Log.e("User", userId);
                    assert userId != null;
                    DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ClassUserInfo userInfo = documentSnapshot.toObject(ClassUserInfo.class);
                            assert userInfo != null;
                            supportList.add(userInfo.getName());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Admin");
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot snap : snapshot.getChildren()){
                    String userId = snap.getKey();
                    Log.e("User", userId);
                    assert userId != null;
                    DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ClassUserInfo userInfo = documentSnapshot.toObject(ClassUserInfo.class);
                            assert userInfo != null;
                            chatList.add(userInfo.getName());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.setVisibility(View.GONE);

                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setVisibility(View.GONE);

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
                i.putExtra("nameList", scholarList);
                Log.e("User", " " + scholarList.size());
                startActivity(i);
                break;
            case R.id.complaintid:
                i = new Intent(this, Complaint.class);
                startActivity(i);
                break;
            case R.id.chatId:
                i = new Intent(this, ActivityChat.class);
                i.putExtra("nameList", chatList);
                Log.e("User", " " + chatList.size());
                startActivity(i);
                break;
            case R.id.legalSupportId:
                i = new Intent(this, ActivitySupportList.class);
                i.putExtra("nameList", supportList);
                Log.e("User", " " + supportList.size());
                startActivity(i);

            default:
                break;
        }
    }

}
