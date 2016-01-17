package me.mariorugeles.app.model;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mariorugeles.me.moviediscover.R;

/**
 * Created by mario on 1/17/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity context, List<Movie> Movies) {
        super(context, 0, Movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie Movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.list_item_posterimage);
        posterImageView.setImageURI(Uri.parse(Movie.getPoster())); //.setImageResource(Movie.getPoster());

        TextView titleView = (TextView) convertView.findViewById(R.id.list_item_title);
        titleView.setText(Movie.getTitle());

        TextView ratingView = (TextView) convertView.findViewById(R.id.list_item_rating);
        ratingView.setText(String.format("%d %", Movie.getRating()));
        return convertView;
    }

}
