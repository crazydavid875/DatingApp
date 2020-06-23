package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.R;

import java.io.IOException;
import java.util.List;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class PhotoAdapter extends ArrayAdapter<Cards> {
    Context mContext;
    MainActivity main;

    public PhotoAdapter(@NonNull Context context, int resource, @NonNull List<Cards> objects,MainActivity main) {
        super(context, resource, objects);
        this.mContext = context;
        this.main = main;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView auther = convertView.findViewById(R.id.auther);
        ImageView image = convertView.findViewById(R.id.image);
        ImageButton btnInfo = convertView.findViewById(R.id.checkInfoBeforeMatched);

        name.setText(card_item.getName() );
        auther.setText(card_item.getAuther());
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileCheckinMain.class);
                intent.putExtra("name", card_item.getName() );
                intent.putExtra("auther", card_item.getAuther() );
                intent.putExtra("photo", card_item.getProfileImageUrl());
                intent.putExtra("bio", card_item.getBio());
                intent.putExtra("interest", card_item.getInterest());
                intent.putExtra("distance", card_item.getDistance());
                mContext.startActivity(intent);
            }
        });

        name.setText(card_item.getName() );
        auther.setText(card_item.getAuther());
        switch (card_item.getProfileImageUrl()) {
            case "defaultFemale":
                Glide.with(getContext()).load(R.drawable.default_woman).into(image);
                break;
            case "defaultMale":
                Glide.with(getContext()).load(R.drawable.default_man).into(image);
                break;
            default:
                Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }

}
