package com.example.android.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView titleTV;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTV;
    @BindView(R.id.tv_average_vote)
    TextView averageVoteTV;
    @BindView(R.id.tv_plot)
    TextView plotTV;
    @BindView(R.id.iv_poster)
    ImageView posterIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        Movie currentMovie = bundle.getParcelable("movie");

        titleTV.setText(currentMovie.getTitle());
        releaseDateTV.setText(currentMovie.getReleaseDate());
        averageVoteTV.setText(currentMovie.getVote());
        plotTV.setText(currentMovie.getPlot());
        Glide.with(this).load(currentMovie.getPosterUrl()).into(posterIV);
    }

    /**
     * Return to the previous activity state on Up pressed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
