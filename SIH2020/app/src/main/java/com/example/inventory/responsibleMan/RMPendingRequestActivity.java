package com.example.inventory.responsibleMan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.inventory.R;
import com.example.inventory.model.Request;
import com.example.inventory.responsibleMan.adapters.RMPendingRequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RMPendingRequestActivity extends AppCompatActivity {


    RecyclerView rmPendingRequestRecycler;
    RMPendingRequestAdapter rmPendingRequestAdapter;

    List<Request> pendingRequestList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requestReference, responsibleManReference, pendimgRequestListReference,serviceManReference;

    FirebaseAuth auth;
    FirebaseUser user;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmpending_request);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        pendingRequestList = new ArrayList<Request>();

        rmPendingRequestRecycler = findViewById(R.id.rmpending_request);
        rmPendingRequestRecycler.setLayoutManager(new LinearLayoutManager(this));

        rmPendingRequestAdapter = new RMPendingRequestAdapter(getApplicationContext(),pendingRequestList);
        rmPendingRequestRecycler.setAdapter(rmPendingRequestAdapter);

        Log.i("danda ghus gya","Hello");

        firebaseDatabase = FirebaseDatabase.getInstance();
        responsibleManReference = firebaseDatabase.getReference("Users").child("ResponsibleMan").child(user.getUid());
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan");
        pendimgRequestListReference = responsibleManReference.child("pendingRequestList");
        requestReference = firebaseDatabase.getReference("Requests");

        pendimgRequestListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey().toString();
                requestReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request request = new Request();
                        request = dataSnapshot.getValue(Request.class);

                        pendingRequestList.add(0,request);
                        rmPendingRequestAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                    });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {



            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
