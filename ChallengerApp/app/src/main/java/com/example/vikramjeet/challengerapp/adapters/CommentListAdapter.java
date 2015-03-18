package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Comment;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vikramjeet on 3/9/15.
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {

    public static class ViewHolder {
        @InjectView(R.id.ivCommentUserPhoto)
        ImageView ivUserPhoto;
        @InjectView(R.id.tvCommentUserName)
        TextView tvUserName;
        @InjectView(R.id.tvCommentTimestamp)
        TextView tvTimestamp;
        @InjectView(R.id.tvComment)
        TextView tvCommentText;

        public ViewHolder(android.view.View view) {
            ButterKnife.inject(this, view);
        }
    }

    public CommentListAdapter(Context context, ArrayList<Comment> comments) {

        super(context, android.R.layout.simple_list_item_1, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Comment
        Comment comment = getItem(position);

        // View look up cache stored in tag
        ViewHolder viewHolder = null;
        if (convertView == null) {
            // Inflate convertView
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            // Create View cache
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate views
        viewHolder.tvUserName.setText(comment.getAuthor().getName());
        viewHolder.tvCommentText.setText(comment.getText());

        String timestamp = DateUtils.getRelativeTimeSpanString(comment.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        viewHolder.tvTimestamp.setText(timestamp);

        // Transformation for rounded views
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext()).
                load(comment.getAuthor().getPhotoURL()).
                fit().
                transform(transformation).
                placeholder(R.drawable.photo_placeholder).
                into(viewHolder.ivUserPhoto);

        return convertView;
    }
}


