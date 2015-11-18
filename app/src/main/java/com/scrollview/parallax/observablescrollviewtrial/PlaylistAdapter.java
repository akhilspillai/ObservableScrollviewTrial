package com.scrollview.parallax.observablescrollviewtrial;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<String> mPlaylists;

    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.txt_playlist_name)
        TextView mTxtPlaylistName;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


    }

    public PlaylistAdapter(List<String> playLists) {
        this.mPlaylists = playLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTxtPlaylistName.setText(mPlaylists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlaylists.size();
    }


}