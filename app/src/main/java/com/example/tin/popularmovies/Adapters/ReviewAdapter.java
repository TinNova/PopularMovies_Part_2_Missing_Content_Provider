package com.example.tin.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tin.popularmovies.Models.Review;
import com.example.tin.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {


    private final List<Review> reviews;
    private final Context context;


    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new View and inflate the list_item Layout into it
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reviews_list_item, viewGroup, false);

        // Return the View we just created
        return new ReviewAdapter.ViewHolder(v);
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

        Review review = reviews.get(position);

        viewHolder.userNameTV.setText(review.getUserName());
        viewHolder.userReviewTV.setText(review.getUserComment());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView userNameTV;
        final TextView userReviewTV;


        public ViewHolder(View itemView) {
            super(itemView);

            userNameTV = (TextView) itemView.findViewById(R.id.user_name);
            userReviewTV = (TextView) itemView.findViewById(R.id.user_review);

        }

    }
}
