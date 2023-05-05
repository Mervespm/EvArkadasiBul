package com.mervekarabulut.mezunuygulamasi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Binder;
import android.os.Bundle;
import android.view.View;

import com.mervekarabulut.mezunuygulamasi.R;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityDetailsBinding;
import com.mervekarabulut.mezunuygulamasi.databinding.ActivityUserDetailsBinding;
import com.mervekarabulut.mezunuygulamasi.model.DataHolder;
import com.mervekarabulut.mezunuygulamasi.model.Post;
import com.mervekarabulut.mezunuygulamasi.model.Singleton;
import com.mervekarabulut.mezunuygulamasi.model.UserSingleton;
import com.squareup.picasso.Picasso;

public class UserDetailsActivity extends AppCompatActivity {

    private ActivityUserDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        UserSingleton userSingleton = UserSingleton.getInstance();
        DataHolder dataHolder = userSingleton.getSelectedDataHolder();

        binding.NameSurname.setText(dataHolder.getName() + " "+ dataHolder.getSurname());
        binding.city.setText(dataHolder.getCity());
        binding.educationType.setText(dataHolder.getEducation());
        binding.phone.setText(dataHolder.getPhone());
        binding.Years.setText(dataHolder.getEnteringYear() + "-" + dataHolder.getGraduateYear());

//        Picasso.get().load(selectedPost.getUrl()).into(binding.imageView);


    }
}