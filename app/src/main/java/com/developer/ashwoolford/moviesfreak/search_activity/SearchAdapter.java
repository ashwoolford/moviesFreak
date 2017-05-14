package com.developer.ashwoolford.moviesfreak.search_activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.ashwoolford.moviesfreak.R;
import com.developer.ashwoolford.moviesfreak.details_activity.DetailsActivity;

import java.util.List;

/**
 * Created by ashwoolford on 11/25/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    Context context;
    List<SearchData> mSearchDataList;
    public SearchAdapter(Context context,List<SearchData> mSearchDataList){
        this.context = context;
        this.mSearchDataList = mSearchDataList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(mSearchDataList.get(position).getPoster()).into(holder.mImageView);
        holder.titleView.setText(mSearchDataList.get(position).getNtitle());
        holder.releaseDate.setText(mSearchDataList.get(position).getReleseDate());
        holder.rattingView.setText(""+mSearchDataList.get(position).getRatting());

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Long id = mSearchDataList.get(position).getNid();
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Long id = mSearchDataList.get(position).getNid();
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSearchDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleView,releaseDate,rattingView;
        ImageView mImageView;
        Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.NtitleView);
            releaseDate = (TextView) itemView.findViewById(R.id.releseDateView);
            mImageView = (ImageView) itemView.findViewById(R.id.poster);
            rattingView = (TextView) itemView.findViewById(R.id.firstRatting);
            mButton = (Button) itemView.findViewById(R.id.more_button);

        }
    }
}
