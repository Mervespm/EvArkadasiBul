package com.mervekarabulut.mezunuygulamasi.adapter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.model.Post;
import com.mervekarabulut.mezunuygulamasi.model.Singleton;
import com.mervekarabulut.mezunuygulamasi.model.UserSingleton;
import com.mervekarabulut.mezunuygulamasi.view.DetailsActivity;
import com.mervekarabulut.mezunuygulamasi.view.MyPostsActivity;
import com.mervekarabulut.mezunuygulamasi.view.UserDetailsActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    ArrayList<DataHolder> list;

    public UserAdapter(ArrayList<DataHolder> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        DataHolder dataHolder = list.get(holder.getAdapterPosition());
        holder.email.setText(dataHolder.getEmail());
        holder.nameSurname.setText(dataHolder.getName() + " " + dataHolder.getSurname());
        holder.year.setText(dataHolder.getEnteringYear() + "-" + dataHolder.getGraduateYear());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(holder.itemView.getContext(), UserDetailsActivity.class);
//                UserSingleton userSingleton = UserSingleton.getInstance();
//                userSingleton.setChosenDataHolder(list.get(holder.getAdapterPosition()));
//                holder.itemView.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView email, nameSurname, year;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.TextEmail);
            nameSurname = itemView.findViewById(R.id.TextNameSurname);
            year = itemView.findViewById(R.id.Years);

        }

    }

}
