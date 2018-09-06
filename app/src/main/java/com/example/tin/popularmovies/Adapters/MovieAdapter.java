package com.example.tin.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tin.popularmovies.MoviePositionListener;
import com.example.tin.popularmovies.R;
import com.example.tin.popularmovies.retrofit.movie.MovieResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final ArrayList<MovieResult> movies;
    private final Context context;
    private final MoviePositionListener moviePositionListener;

    public MovieAdapter(ArrayList<MovieResult> movies, Context context, MoviePositionListener moviePositionListener) {
        this.movies = movies;
        this.context = context;
        this.moviePositionListener = moviePositionListener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Create a new View and inflate the list_item Layout into it
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movies_list_item, viewGroup, false);

        // Return the View we just created
        return new ViewHolder(v);

    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                   contents of the item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MovieResult movie = movies.get(position);

        Picasso.with(context).load(movie.getPosterPath())
                .into(viewHolder.imageViewPoster);

    }

    // Returns the number of items in the listItems List
    @Override
    public int getItemCount() {
        return movies.size();
    }


    // This is the ViewHolder Class, it represents the rows in the RecyclerView (i.e every row is a ViewHolder)
    // In this example each row is made up of an ImageView
    // The ViewHolder also "implements an OnClickListener"
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageViewPoster;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewPoster = (ImageView) itemView.findViewById(R.id.main_movie_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    moviePositionListener.MovieItemClick(movies.get(getAdapterPosition()));
                }
            });

        }
    }
}
