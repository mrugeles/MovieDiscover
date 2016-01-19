package me.mariorugeles.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import me.mariorugeles.app.model.Movie;
import me.mariorugeles.app.model.MovieAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {
    private final String LOG_TAG = MovieListFragment.class.getSimpleName();
    final String ORDER_POPULAR = "popularity.desc";
    final String ORDER_RATING = "vote_average.desc";
    MovieAdapter movieListAdapter;

    public MovieListFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovieList(ORDER_POPULAR);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movielistfragment, container, false);
        movieListAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        GridView movieGridView = (GridView)rootView.findViewById(R.id.gridview_movies);
        movieGridView.setAdapter(movieListAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieListAdapter.getItem(position);
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movie", movie);
                startActivity(movieDetailIntent);
            }
        });
        return rootView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();

        if(id == R.id.action_order_popular){
            updateMovieList(ORDER_POPULAR);
            return true;
        }
        if(id == R.id.action_order_rating){
            updateMovieList(ORDER_RATING);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void updateMovieList(String order) {
        new FetchMoviesTask().execute(order);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String orderby = params[0];
            String jsonResponseStr = null;
            String appid = "555a0bd61e31208b9eb14cf8c1a1392e";
            try {
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORTBY_PARAM = "sort_by";
                final String APPIKEY_PARAM = "api_key";

                Uri buildUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(APPIKEY_PARAM, appid)
                        .appendQueryParameter(SORTBY_PARAM, orderby).build();
                URL url = new URL(buildUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    jsonResponseStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    jsonResponseStr = null;
                }
                jsonResponseStr = buffer.toString();
                Log.v(LOG_TAG, jsonResponseStr);
            }catch (IOException ex){
                Log.e(LOG_TAG, "FetchMovies Err", ex);
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MovieListFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieListFromJson(jsonResponseStr);
            } catch (JSONException e) {
               Log.e(LOG_TAG, "JSON Error", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result){
            if(result != null){
                movieListAdapter.clear();
                for (Movie movie:result ) {
                    movieListAdapter.add(movie);
                }
            }
        }

        private Movie[] getMovieListFromJson(String jsonResponseStr) throws JSONException {
            JSONObject jsonResponse = new JSONObject(jsonResponseStr);
            JSONArray movieListJSONArray = jsonResponse.getJSONArray("results");
            ArrayList<Movie> movieList = new ArrayList<Movie>();
            for(int i = 0; i < movieListJSONArray.length(); i++) {
                JSONObject movieJSON = movieListJSONArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setTitle(movieJSON.getString("original_title"));
                movie.setPoster(movieJSON.getString("poster_path"));
                movie.setSynopsis(movieJSON.getString("overview"));
                movie.setRating(new Double(movieJSON.getDouble("vote_average")).floatValue());
                movie.setReleaseDate(movieJSON.getString("release_date"));
                movieList.add(movie);
            }
            return movieList.toArray(new Movie[movieList.size()]);
        }
    }
}
