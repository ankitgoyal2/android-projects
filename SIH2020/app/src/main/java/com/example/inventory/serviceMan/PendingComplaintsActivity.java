package com.example.inventory.serviceMan;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory.R;
import com.example.inventory.model.Complaint;
import com.example.inventory.serviceMan.adapters.PendingComplaintAdapter;
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

public class PendingComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView_complaints;
    PendingComplaintAdapter myPendingComplaintAdapter;

    List<String> pendingComplaintList;

    List<Complaint> pendingComplaintObjectList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference serviceManReference, pendingComplaintListReference, complaintReference, responsibleManReference;

    FirebaseAuth auth;
    FirebaseUser user;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaints);

        recyclerView_complaints = findViewById(R.id.recyclerView_complaints);
        recyclerView_complaints.setLayoutManager(new LinearLayoutManager(this));



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        pendingComplaintList = new ArrayList<String>();
        pendingComplaintObjectList = new ArrayList<Complaint>();

        myPendingComplaintAdapter = new PendingComplaintAdapter(getApplicationContext(),pendingComplaintObjectList);
        recyclerView_complaints.setAdapter(myPendingComplaintAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan").child(user.getUid());
        pendingComplaintListReference = serviceManReference.child("pendingComplaintList");
        responsibleManReference = firebaseDatabase.getReference("Users").child("ResponsibleMan");
        complaintReference = firebaseDatabase.getReference("Complaints");



        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                Log.i("vikas key", key);
                complaintReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint complaint = new Complaint();
                        complaint = dataSnapshot.getValue(Complaint.class);
                        pendingComplaintObjectList.add(0,complaint);
                        myPendingComplaintAdapter.notifyDataSetChanged();


                        //Log.i("machine id", complaint.getComplaintMachineId());
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
