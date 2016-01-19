package me.mariorugeles.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.mariorugeles.app.model.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra("movie")) {
            Movie movie  = intent.getParcelableExtra("movie");
            TextView titleTextView = (TextView) rootView.findViewById(R.id.detail_title);
            titleTextView.setText(movie.getTitle());

            TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.detail_releasedate);
            Locale currentLocale = Locale.getDefault();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy", currentLocale);
            try {
                Date date = formatter.parse(movie.getReleaseDate());
                releaseDateTextView.setText(formatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TextView votesTextView = (TextView) rootView.findViewById(R.id.detail_voteaverage);
            votesTextView.setText(movie.getRating()+"/10");

            TextView plotTextView = (TextView) rootView.findViewById(R.id.detail_plot);
            plotTextView.setText(movie.getSynopsis());

            ImageView posterImageView = (ImageView) rootView.findViewById(R.id.detail_posterimage);
            Picasso.with(getActivity()).load(movie.getPoster()).into(posterImageView);
        }
        return rootView;
    }
}
