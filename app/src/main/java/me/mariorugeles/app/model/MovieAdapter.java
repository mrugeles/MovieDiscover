package me.mariorugeles.app.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mariorugeles.me.moviediscover.R;

/**
 * Created by mario on 1/17/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w500/";

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
        Picasso.with(getContext()).load(String.format("%s%s", IMAGE_BASE_PATH, Movie.getPoster())).into(posterImageView);
        return convertView;
    }

}
