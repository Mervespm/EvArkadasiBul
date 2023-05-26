package com.mervekarabulut.mezunuygulamasi.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.model.MatchRequest;

import java.util.List;

public class MatchRequestAdapter extends RecyclerView.Adapter<MatchRequestAdapter.ViewHolder> {

    private List<MatchRequest> matchRequests;
    private Context context;

    public MatchRequestAdapter(Context context, List<MatchRequest> matchRequests) {
        this.context = context;
        this.matchRequests = matchRequests;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchRequest matchRequest = matchRequests.get(position);
        holder.name.setText(matchRequest.getSenderName());
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accept the match request
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("MatchRequests").child(matchRequest.getKey()).child("status").setValue("accepted");
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reject the match request
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("MatchRequests").child(matchRequest.getKey()).child("status").setValue("rejected");
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button acceptButton;
        Button rejectButton;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            name = itemView.findViewById(R.id.tvSenderName);
        }
    }
}

