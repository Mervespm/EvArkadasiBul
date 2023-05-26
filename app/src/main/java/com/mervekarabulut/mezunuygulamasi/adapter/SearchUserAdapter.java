package com.mervekarabulut.mezunuygulamasi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.view.UserDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.UserViewHolder> {
    private Context context;
    private List<DataHolder> userList;

    // Constructor
    public SearchUserAdapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    // Inner class for holding the views of each item
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView distanceTextView;
        TextView stateTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
        }
    }

    // Method for updating the user list
    public void updateUserList(List<DataHolder> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_holder, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        DataHolder user = userList.get(position);
        holder.nameTextView.setText(user.getName() +" "+ user.getSurname() );
        if(user.getDistance() == null){
            holder.distanceTextView.setText("Uzaklık: Bilinmiyor");
        }else {
            holder.distanceTextView.setText("Uzaklık:" + String.valueOf(user.getDistance()));
        }
        if(user.getState() == null){
            holder.stateTextView.setText("Durum: Bilinmiyor");
        }else {
            holder.stateTextView.setText("Durum: " + user.getState());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tıklanan kullanıcının profil sayfasına geçiş yapma
                DataHolder clickedUser = userList.get(position);
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("user", clickedUser);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

