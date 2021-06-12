package com.example.inventory.responsibleMan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory.R;
import com.example.inventory.model.Complaint;
import com.example.inventory.responsibleMan.adapters.RMPendingComplaintAdapter;
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

public class RMComplaintPendingFragment extends Fragment {

    RecyclerView rm_recyclerView_pending_complaint;
    RMPendingComplaintAdapter rmPendingComplaintAdapter;

    List<Complaint> pendingComplaintObjectList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference complaintReference, responsibleManReference, pendingComplaintListReference;

    FirebaseAuth auth;
    FirebaseUser user;

    public RMComplaintPendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.rm_complaint_pending, container, false);
        rm_recyclerView_pending_complaint = rootView.findViewById(R.id.rm_recyclerView_pending_complaint);
        rm_recyclerView_pending_complaint.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        pendingComplaintObjectList = new ArrayList<>();

        rmPendingComplaintAdapter = new RMPendingComplaintAdapter(getActivity().getApplicationContext(), pendingComplaintObjectList);
        rm_recyclerView_pending_complaint.setAdapter(rmPendingComplaintAdapter);


        firebaseDatabase =  FirebaseDatabase.getInstance();
        responsibleManReference = firebaseDatabase.getReference("Users").child("ResponsibleMan").child(user.getUid());
        pendingComplaintListReference = responsibleManReference.child("pendingComplaintList");
        complaintReference = firebaseDatabase.getReference("Complaints");

        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                complaintReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint pendingComplaint = new Complaint();
                        pendingComplaint = dataSnapshot.getValue(Complaint.class);

                        pendingComplaintObjectList.add(0,pendingComplaint);
                        rmPendingComplaintAdapter.notifyDataSetChanged();
//                        Log.i("danda ghus gya",pendingComplaint.getComplaintAllocatedTo());
                        //Log.i("machine id", request.getComplaintMachineId());
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
        return rootView;
    }
}
