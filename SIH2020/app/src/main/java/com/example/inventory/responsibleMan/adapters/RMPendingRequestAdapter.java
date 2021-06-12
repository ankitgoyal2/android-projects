package com.example.inventory.responsibleMan.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory.R;
import com.example.inventory.model.Request;
import com.example.inventory.responsibleMan.RatingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RMPendingRequestAdapter extends RecyclerView.Adapter<RMPendingRequestAdapter.MyHolder> {

    Context c;
    public List<Request> x;

    DatabaseReference serviceMan, loadValue, complaintReference;
    String load;


    public RMPendingRequestAdapter(Context c, List<Request> x) {
        this.c = c;
        this.x = x;
    }

    public RMPendingRequestAdapter(Context c) {
        this.c = c;
    }

    public void setx(List<Request> x) {
        this.x = x;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_pending_request_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myholder, final int position) {


        myholder.serviceman1.setText(x.get(position).getServicemanName());
        myholder.requestid1.setText(x.get(position).getRequestid());
        myholder.complain_id.setText(x.get(position).getComplaintId());
        myholder.description.setText(x.get(position).getDescription());

        //complaintReference = FirebaseDatabase.getInstance().getReference("Complaints").child(x.get(position).getComplaintId());
        loadValue = FirebaseDatabase.getInstance().getReference("Users").child("ServiceMan").child(x.get(position).getServiceMan()).child("load");


        loadValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                load = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myholder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(x.get(position).isStatus())
            {
                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                //add data
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);


                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/complaintCompletedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/completedComplaintList/"+x.get(position).getComplaintId(),"true");
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/completedComplaintList/"+x.get(position).getComplaintId(),"true");
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/completedRequestList/"+x.get(position).getRequestid(),"true");
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/load",Integer.parseInt(load)-1);
                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/status",5);

                // delete data
                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingComplaintList/"+x.get(position).getComplaintId(),null);
                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingComplaintList/"+x.get(position).getComplaintId(),null);
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingRequestList/"+x.get(position).getRequestid(),null);

                FirebaseDatabase.getInstance().getReference("Complaints")
                        .child(x.get(position).getComplaintId()).child("complaintMachineId").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String machineId = dataSnapshot.getValue().toString();
                        Log.i("machineId",machineId);
                        updateDatabaseValue.put("/machines/"+machineId+"/pastRecords/"+x.get(position).getRequestid(),"true");
                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                Intent i = new Intent(c, RatingActivity.class);
                i.putExtra("serviceManUid",x.get(position).getServiceMan());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);

            }
            else
            {
                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                //add data

                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/completedRequestList/"+x.get(position).getRequestid(),"true");
                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/status",2);


                // delete data

                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingRequestList/"+x.get(position).getRequestid(),null);

                FirebaseDatabase.getInstance().getReference("Complaints")
                        .child(x.get(position).getComplaintId()).child("complaintMachineId").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String machineId = dataSnapshot.getValue().toString();
                        Log.i("machineId",machineId);
                        updateDatabaseValue.put("/machines/" + machineId + "/pastRecords/" + x.get(position).getRequestid(), "true");
                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }
            }
        });

        myholder.decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/status",2);

                // delete data

                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingRequestList/"+x.get(position).getRequestid(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("size",String.valueOf(x.size()));
        return x.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {


        TextView serviceman1,requestid1,complain_id,description, accept_button,decline_button;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            requestid1 = itemView.findViewById(R.id.request_id1);
            serviceman1 = itemView.findViewById(R.id.serviceman1);
            complain_id = itemView.findViewById(R.id.complain_id);
            description = itemView.findViewById(R.id.description);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);

        }

    }

}