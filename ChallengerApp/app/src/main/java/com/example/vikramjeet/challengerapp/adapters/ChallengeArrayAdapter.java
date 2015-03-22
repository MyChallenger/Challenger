package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.ChallengeStatus;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vinutha on 3/7/2015.
 */
public class ChallengeArrayAdapter extends ArrayAdapter<Challenge> {

    private final ChallengeArrayAdapterListener listener;

    public interface ChallengeArrayAdapterListener {
        void onChallengeSponsor(Challenge challenge);
    }

    @InjectView(R.id.tvTitle)
    TextView tvTitle;
    @InjectView(R.id.tvCategory)
    TextView tvCategory;
    @InjectView(R.id.tvGoal)
    TextView tvGoal;
    @InjectView(R.id.tvExpiry)
    TextView tvExpiry;
    @InjectView(R.id.ivProfile)
    ImageView ivProfile;
    @InjectView(R.id.btnAdd)
    Button btnSponsor;

    public ChallengeArrayAdapter(Context context, List<Challenge> challengeList, ChallengeArrayAdapterListener listener) {
        super(context, android.R.layout.simple_list_item_1, challengeList);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweet
        final Challenge challenge = getItem(position);
        //2. Find or inflate the template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_challenge, parent, false);
        }
        ButterKnife.inject(this, convertView);
        tvCategory.setText(challenge.getCategory());
        tvTitle.setText(challenge.getTitle());
        tvGoal.setText("Goal:" + " $" + challenge.getPrize());
        if (challenge.getCreatedMedia() != null)
            Picasso.with(getContext()).load(challenge.getCreatedMedia().getUrl()).into(ivProfile);
        tvExpiry.setText("expires " + getRelativeTimeAgo(challenge.getExpiryDate().toString()));
        if ((challenge.getStatus() == ChallengeStatus.VERIFIED) || (challenge.getStatus() == ChallengeStatus.BACKED))
            btnSponsor.setEnabled(false);
        btnSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChallengeSponsor(challenge);
                btnSponsor.setEnabled(false);
            }
        });

        return convertView;
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
