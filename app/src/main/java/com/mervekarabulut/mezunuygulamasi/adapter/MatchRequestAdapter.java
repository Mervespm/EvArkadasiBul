package com.mervekarabulut.mezunuygulamasi.adapter;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.model.MatchRequest;
import com.mervekarabulut.mezunuygulamasi.view.MatchRequestList;
import com.mervekarabulut.mezunuygulamasi.view.UserDetailsActivity;

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
        String contactInfoText = "Phone: " + matchRequest.getSenderPhone();
        holder.contactInfo.setText(contactInfoText);

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

        if(matchRequest.getSenderPhone() != null && !matchRequest.getSenderPhone().equals("")) {
            holder.whatsappButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = matchRequest.getSenderPhone();
                    openWhatsapp(phoneNumber);
                }
            });
        }

    }

    private void openWhatsapp(String phoneNumber) {
        try {
            // Telefon numarasını düzenleme: "+" karakterini ve boşlukları kaldırma, "+90" ile başlatma
            phoneNumber = phoneNumber.replace("+", "").replace(" ", "");
            phoneNumber = "+90" + phoneNumber;

            // Whatsapp uygulamasını açma intent'i oluşturma
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
            whatsappIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));
            context.startActivity(whatsappIntent);
        } catch (ActivityNotFoundException e) {
            // Whatsapp uygulaması bulunamazsa veya açılamazsa hata durumunda buraya düşer
            Toast.makeText(context, "Whatsapp uygulaması bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        return matchRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button acceptButton;
        Button rejectButton;
        TextView name;
        TextView contactInfo;
        Button whatsappButton;

        public ViewHolder(View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            name = itemView.findViewById(R.id.tvSenderName);
            contactInfo = itemView.findViewById(R.id.tvContactInfo);
            whatsappButton = itemView.findViewById(R.id.whatsappButton);
        }
    }

}

