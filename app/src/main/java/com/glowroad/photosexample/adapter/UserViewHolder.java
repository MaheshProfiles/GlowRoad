package com.glowroad.photosexample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowroad.photosexample.R;
import com.glowroad.photosexample.entites.Photo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Kumar on 8/14/2018.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvTitle)
    TextView title;

    @BindView(R.id.ivProductImage)
    ImageView productImage;

    @BindView(R.id.tvEmailId)
    TextView emailId;

    @BindView(R.id.tvSecret)
    TextView secret;

    private UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTo(Photo photo) {
        title.setText(photo.getTitle());
        emailId.setText(photo.getOwner());
        secret.setText(photo.getSecret());
        Picasso.get().load(photo.getUrlQ()).placeholder(R.mipmap.ic_launcher).into(productImage);
    }

    public static UserViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

}
