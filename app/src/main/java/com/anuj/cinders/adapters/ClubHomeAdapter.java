package com.anuj.cinders.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuj.cinders.R;
import com.anuj.cinders.dao.Club;
import com.anuj.cinders.models.Venue;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by anujacharya on 2/21/16.
 */
public class ClubHomeAdapter extends RecyclerView.Adapter<ClubHomeAdapter.ViewHolder>{

    List<Venue> venues;
    Context context;

    public ClubHomeAdapter() {
    }

    public ClubHomeAdapter(List<Venue> venues){
        this.venues = venues;
    }

    @Override
    public ClubHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.layout_club_home, parent, false);
        ClubHomeAdapter.ViewHolder viewHolder = new ViewHolder(v1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Venue venue = venues.get(position);

        //set name
        holder.tcClubName.setText(venue.getName());
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvClubName)
        TextView tcClubName;

        public ViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
