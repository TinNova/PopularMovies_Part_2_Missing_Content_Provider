package com.example.tin.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tin.popularmovies.Models.Trailer;
import com.example.tin.popularmovies.MoviePositionListener;
import com.example.tin.popularmovies.R;
import com.example.tin.popularmovies.TrailerPositionListenter;
import com.example.tin.popularmovies.retrofit.trailer.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{


    /** HOW THE THUMBNAIL IMAGE IS CONSTRUCTED
     * String thumbnailImage = "https://img.youtube.com/vi/" + trailers.getTrailerKey + "/0.jpg";
     */
    private final String YOUTUBETHUMBNAILSTART = "https://img.youtube.com/vi/";
    private final String YOUTUBETHUMBNAILEND = "/0.jpg";
    private final String YOUTUBE_TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";


    private final ArrayList<TrailerResult> trailers;
    private final Context context;
    private final TrailerPositionListenter trailerPositionListenter;

    public TrailerAdapter(ArrayList<TrailerResult> trailers, Context context, TrailerPositionListenter trailerPositionListenter) {
        this.trailers = trailers;
        this.context = context;
        this.trailerPositionListenter = trailerPositionListenter;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new View and inflate the list_item Layout into it
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailers_list_item, viewGroup, false);

        // Return the View we just created
        return new TrailerAdapter.ViewHolder(v);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param viewHolder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        TrailerResult trailer = trailers.get(position);

        Picasso.with(context).load(YOUTUBETHUMBNAILSTART + trailer.getKey() + YOUTUBETHUMBNAILEND)
                .into(viewHolder.trailerThumbNail);

        viewHolder.trailerTitle.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView trailerThumbNail;
        final TextView trailerTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            trailerThumbNail = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            trailerTitle = (TextView) itemView.findViewById(R.id.trailer_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    trailerPositionListenter.trailerItemClick(YOUTUBE_TRAILER_BASE_URL + trailers.get(getAdapterPosition()).getKey());
                }
            });
        }

    }
}
