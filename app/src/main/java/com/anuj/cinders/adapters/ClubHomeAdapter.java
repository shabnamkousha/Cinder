package com.anuj.cinders.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuj.cinders.dao.Club;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by anujacharya on 2/21/16.
 */
public class ClubHomeAdapter extends RecyclerView.Adapter<ClubHomeAdapter.ViewHolder>{

    List<Club> clubs;

    public ClubHomeAdapter() {
    }

    public ClubHomeAdapter(List<Club> clubs){
        this.clubs = clubs;
    }

    @Override
    public ClubHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);

        }
    }
}
