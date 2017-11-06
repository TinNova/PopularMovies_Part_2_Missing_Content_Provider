package com.example.tin.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tin.popularmovies.Models.Trailer;
import com.example.tin.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tin on 16/10/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{


    /** HOW THE THUMBNAIL IMAGE IS CONSTRUCTED
     * String thumbnailImage = "https://img.youtube.com/vi/" + trailers.getTrailerKey + "/0.jpg";
     */
    private String YOUTUBETHUMBNAILSTART = "https://img.youtube.com/vi/";
    private String YOUTUBETHUMBNAILEND = "/0.jpg";

    private final List<Trailer> trailers;
    private final Context context;

    private final TrailerListItemClickListener mOnClickListener;

    public interface TrailerListItemClickListener {
        void onListItemClick (int clickedItemIndex);
    }


    public TrailerAdapter(List<Trailer> trailers, Context context, TrailerListItemClickListener listener) {
        this.trailers = trailers;
        this.context = context;
        this.mOnClickListener = listener;
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

        Trailer trailer = trailers.get(position);

        Picasso.with(context).load(YOUTUBETHUMBNAILSTART + trailer.getTrailerKey() + YOUTUBETHUMBNAILEND)
                .into(viewHolder.trailerThumbNail);

        viewHolder.trailerTitle.setText(trailer.getTrailerName());

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView trailerThumbNail;
        TextView trailerTitle;

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }


        public ViewHolder(View itemView) {
            super(itemView);

            trailerThumbNail = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            trailerTitle = (TextView) itemView.findViewById(R.id.trailer_title);

            itemView.setOnClickListener(this);

        }

    }
}
