package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.User;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardQueryAdapter<U> extends ParseQueryAdapter<User> {

    public LeaderboardQueryAdapter(Context context, QueryFactory<User> clazz) {
        super(context, clazz);
    }

    // View lookup cache
    static class ViewHolder {
        @InjectView(R.id.ivUserPhoto)
        ImageView ivUserPhoto;
        @InjectView(R.id.tvName)
        TextView tvName;
        @InjectView(R.id.tvPointsEarned)
        TextView tvPointsEarned;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getItemView(User user, View v, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_leaderboard, null);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
        super.getItemView(user, v, parent);

        // Do additional configuration before returning the View.
        viewHolder.tvName.setText(user.getName());
        viewHolder.tvPointsEarned.setText(String.valueOf(user.getPointsEarned()) + " " + getContext().getResources().getString(R.string.points_label));
        if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            v.setBackgroundColor(getContext().getResources().getColor(R.color.link_water));
        }

        // Rounded image transformation
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(25)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(user.getPhotoURL())
                .transform(transformation)
                .fit()
                .placeholder(R.drawable.photo_placeholder)
                .into(viewHolder.ivUserPhoto);

        return v;
    }
}
