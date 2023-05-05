package com.mervekarabulut.mezunuygulamasi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.model.Post;
import com.mervekarabulut.mezunuygulamasi.model.Singleton;
import com.mervekarabulut.mezunuygulamasi.view.DetailsActivity;
import com.mervekarabulut.mezunuygulamasi.view.FeedActivity;
import com.mervekarabulut.mezunuygulamasi.view.MyPostsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context context;
    ArrayList<Post> postArrayList;


    public PostAdapter(ArrayList<Post> postArrayList, FeedActivity activity) {
        this.context = activity;
        this.postArrayList = postArrayList;
    }

    public PostAdapter(ArrayList<Post> postArrayList, MyPostsActivity activity) {
        this.context = activity;
        this.postArrayList = postArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView url1;
        TextView email1;
        TextView comment1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            url1 = itemView.findViewById(R.id.imageview3);
            email1 = itemView.findViewById(R.id.Textemail);
            comment1 = itemView.findViewById(R.id.Textcomment);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post  = postArrayList.get(holder.getAdapterPosition());
        holder.email1.setText(post.getEmail());
        holder.comment1.setText(post.getComment());
        Picasso.get().load(postArrayList.get(holder.getAdapterPosition()).getUrl()).into(holder.url1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                Singleton singleton = Singleton.getInstance();
                singleton.setChosenPost(postArrayList.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }




}
