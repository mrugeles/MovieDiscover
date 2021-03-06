package me.mariorugeles.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MovieDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_moviedetail, new MovieDetailFragment())
                    .commit();
        }
    }

}
