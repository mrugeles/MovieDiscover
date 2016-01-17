package mariorugeles.me.moviediscover;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

import mariorugeles.me.moviediscover.model.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {
    private final String LOG_TAG = MovieListFragment.class.getSimpleName();

    ArrayAdapter<Movie> movieListAdapter;

    public MovieListFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovieList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "MovieListFragment Ok");
        movieListAdapter = new ArrayAdapter<Movie>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                new ArrayList<String>()
        );
        return inflater.inflate(R.layout.movielistfragment, container, false);
    }

    private void updateMovieList() {
        new FetchMoviesTask().execute();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(Void... params) {
            Log.v(LOG_TAG, "FetchMoviesTask Ok");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonResponseStr = null;
            String appid = "555a0bd61e31208b9eb14cf8c1a1392e";
            try {
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String APPIKEY_PARAM = "api_key";

                Uri buildUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(APPIKEY_PARAM, appid).build();

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
            Log.v(LOG_TAG, "onPostExecute");
            if(result != null){
                movieListAdapter.clear();
                for (Movie movie:result ) {
                    Log.v(LOG_TAG, movie.getTitle());
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
