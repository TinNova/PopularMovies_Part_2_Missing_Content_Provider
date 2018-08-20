package com.example.tin.popularmovies.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tin.popularmovies.Models.FavouriteMovie;
import com.example.tin.popularmovies.R;
import com.example.tin.popularmovies.room.FavouriteMovieRoom;

import java.util.List;


public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder>{

    private final List<FavouriteMovieRoom> mFavouriteMovies;
    private final Context context;
    //private Cursor mCursor;

    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick (int clickedItemIndex);

    }

    public FavouritesAdapter(List<FavouriteMovieRoom> favouriteMovies, Context context, ListItemClickListener mOnClickListener) {

        //this.mCursor = cursor;
        this.mFavouriteMovies = favouriteMovies;
        this.context = context;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new View and inflate the list_item Layout into it
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_list_item, viewGroup, false);

        // Return the View we just created
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        FavouriteMovieRoom favouriteMovie = mFavouriteMovies.get(position);

        //
        byte[] moviePosterSql = favouriteMovie.getMoviePoster();
        // Here we convert the Movie Poster from a byte[] into a Bitmap
        Bitmap bitmapMoviePosterSql = BitmapFactory.decodeByteArray(moviePosterSql, 0, moviePosterSql.length);
        // Here we attach the converted Movie Poster to the ImageView
        viewHolder.imageViewPosterIV.setImageBitmap(Bitmap
                .createScaledBitmap(bitmapMoviePosterSql, 342, 513, false));

    }

    @Override
    public int getItemCount() {

        return mFavouriteMovies.size();
    }

    public void addItems(List<FavouriteMovieRoom> favouriteMovies) {

        this.mFavouriteMovies.clear();
        this.mFavouriteMovies.addAll(favouriteMovies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageViewPosterIV;

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewPosterIV = (ImageView) itemView.findViewById(R.id.favourite_movie_image);
            itemView.setOnClickListener(this);

        }
    }
}