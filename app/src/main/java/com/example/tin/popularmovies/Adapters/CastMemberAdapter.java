package com.example.tin.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tin.popularmovies.Models.CastMember;
import com.example.tin.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastMemberAdapter extends RecyclerView.Adapter<CastMemberAdapter.ViewHolder> {


    private final List<CastMember> castMembers;
    private final Context context;


    public CastMemberAdapter(List<CastMember> castMembers, Context context) {
        this.castMembers = castMembers;
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
                .inflate(R.layout.castmembers_list_item, viewGroup, false);

        // Return the View we just created
        return new CastMemberAdapter.ViewHolder(v);
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

        CastMember castMember = castMembers.get(position);

        Picasso.with(context).load(castMember.getActorImageUrl())
                .into(viewHolder.castMemberThumbnail);

        viewHolder.actorName.setText(castMember.getActorName());
        viewHolder.characterName.setText(castMember.getCharacterName());

    }

    @Override
    public int getItemCount() {
        return castMembers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView castMemberThumbnail;
        final TextView characterName;
        final TextView actorName;


        public ViewHolder(View itemView) {
            super(itemView);

            castMemberThumbnail = (ImageView) itemView.findViewById(R.id.castmember_thumbnail);
            characterName = (TextView) itemView.findViewById(R.id.character_name);
            actorName = (TextView) itemView.findViewById(R.id.actor_name);

        }

    }
}
