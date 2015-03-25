package com.example.vikramjeet.challengerapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.activities.CompletedChallengeDetailActivity;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.ChallengeStatus;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by vrumale on 3/23/15.
 */
public class SimpleRecycleViewAdapter extends RecyclerView.Adapter<SimpleRecycleViewAdapter.SimpleItemViewHolder> implements View.OnClickListener{
    private List<Challenge> mChallenges;
    private Context mContext;
    private final ChallengeArrayAdapter.ChallengeArrayAdapterListener mlistener;

    private static final String YOUTUBE_VIDEO_THUMBNAIL = "http://img.youtube.com/vi/%s/hqdefault.jpg";

    @Override
    public void onClick(View v) {

    }

    public interface ChallengeArrayAdapterListener {
        void onChallengeSponsor(Challenge challenge);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SimpleRecycleViewAdapter(Context context, List<Challenge> items, ChallengeArrayAdapter.ChallengeArrayAdapterListener listener) {
        mContext = context;
        if (items == null) {
            throw new IllegalArgumentException("contacts must not be null");
        }
        mChallenges = items;
        mlistener = listener;
    }
    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvCategory;
        TextView tvGoal;
        TextView tvExpiry;
        ImageView ivProfile;
        Button btnSponsor;

        public SimpleItemViewHolder(View itemView, final Context context) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);

            tvGoal = (TextView) itemView.findViewById(R.id.tvGoal);
            tvExpiry = (TextView) itemView.findViewById(R.id.tvExpiry);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            btnSponsor = (Button) itemView.findViewById(R.id.btnAdd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an intent
                    Intent challengeDetailIntent = new Intent(context, CompletedChallengeDetailActivity.class);
                    // Get the challenge

                    // Start activity
                    context.startActivity(challengeDetailIntent);
                    String position = (String) tvTitle.getTag();
                    challengeDetailIntent.putExtra(CompletedChallengeDetailActivity.EXTRA_OPEN_CHALLENGE_ID, position);
                    context.startActivity(challengeDetailIntent);
                    ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    // Log.d("ObjectID:", challenge.getObjectId());

                }
            });

        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mChallenges.size();
    }

    // Create new items (invoked by the layout manager)
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_challenge, viewGroup, false);
        return new SimpleItemViewHolder(itemView, mContext);
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final SimpleItemViewHolder viewHolder, int position) {
        final Challenge challenge = mChallenges.get(position);
        if(challenge == null  || viewHolder.tvCategory == null) {
            Toast.makeText(mContext, "What happen", Toast.LENGTH_SHORT).show();
            return;
        }
//        viewHolder.tvCategory.setText(challenge.getCategory());
        viewHolder.tvTitle.setText(challenge.getTitle());
        viewHolder.tvGoal.setText("Goal:" + " $" + challenge.getPrize());
        if (challenge.getCreatedMediaId() != null) {
            Picasso.with(mContext).
                    load(String.format(YOUTUBE_VIDEO_THUMBNAIL, challenge.getCreatedMediaId())).
                    placeholder(R.drawable.photo_placeholder).
                    into(viewHolder.ivProfile);
        }
        viewHolder.tvExpiry.setText("Expires " + getRelativeTimeAgo(challenge.getExpiryDate().toString()));
        if ((challenge.getStatus() == ChallengeStatus.VERIFIED) || (challenge.getStatus() == ChallengeStatus.BACKED)) {
            viewHolder.btnSponsor.setEnabled(false);
            viewHolder.btnSponsor.setTextColor(R.color.primary_light);
            viewHolder.btnSponsor.setText("SPONSORED");
        }
        viewHolder.btnSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onChallengeSponsor(challenge);
                if(challenge.getStatus().equals(ChallengeStatus.BACKED)) {
                    viewHolder.btnSponsor.setTextColor(R.color.primary_light);
                    viewHolder.btnSponsor.setText("SPONSORED");
                    viewHolder.btnSponsor.setEnabled(false);
                }
            }
        });
        viewHolder.tvCategory.setText(challenge.getCategory());
        viewHolder.tvTitle.setTag(challenge.getObjectId());
    }
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
