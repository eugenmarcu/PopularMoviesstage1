package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<List<Movie>> {

    public final String REQUEST_URL = "http://api.themoviedb.org/3/movie/";

    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String mSection = "popular";
    private LoaderManager mLoaderManager;
    private static final int MOVIE_LOADER_ID = 1;
    private List<Movie> mMovieList;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int numberOfColumns = 2;
        mMovieList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new RecyclerViewAdapter(this, mMovieList);
        adapter.setClickListener(MainActivity.this);
        recyclerView.setAdapter(adapter);
        // Get a reference to the LoaderManager, in order to interact with loaders.
        mLoaderManager = getLoaderManager();

        if (!isInternetConnection()) {
            adapter.mMovieList.clear();
            emptyView.setText(R.string.no_internet);
            progressBar.setVisibility(View.GONE);
        } else {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            mLoaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        Movie currentMovie = adapter.getItem(position);
        intent.putExtra("movie", currentMovie);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_order_by_most_popular:
                menu.getItem(0).getSubMenu().getItem(1).setIcon(null);
                item.setIcon(R.drawable.ic_check_black_24dp);
                mSection = "popular";
                mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                return true;

            case R.id.action_order_by_higher_rated:
                mSection = "top_rated";
                item.setIcon(R.drawable.ic_check_black_24dp);
                menu.getItem(0).getSubMenu().getItem(0).setIcon(null);
                mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL + mSection);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api_key", BuildConfig.ApiKey);

        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        progressBar.setVisibility(View.GONE);
        if (movies != null && movies.size() > 0) {
            emptyView.setVisibility(View.GONE);
            adapter.mMovieList.clear();
            adapter.addAll(movies);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.mMovieList.clear();
            recyclerView.setAdapter(adapter);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_movies_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.mMovieList.clear();
    }

    private boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}