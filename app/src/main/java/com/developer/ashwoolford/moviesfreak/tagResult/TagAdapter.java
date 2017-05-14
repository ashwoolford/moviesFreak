package com.developer.ashwoolford.moviesfreak.tagResult;

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
 * Created by ashwoolford on 1/18/2017.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    Context context;
    List<TagData> mTagDataList;

    public TagAdapter(Context context, List<TagData> tagDataList) {
        this.context = context;
        mTagDataList = tagDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(context).load(mTagDataList.get(position).getPoster_path()).into(holder.mImageView);
        holder.title.setText(mTagDataList.get(position).getTitleT());
        holder.releaseDate.setText(mTagDataList.get(position).getReleaseDate());
        holder.rattingView.setText(""+mTagDataList.get(position).getRatting());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Long id = mTagDataList.get(position).getIdT();
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });

        holder.mButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Long id = mTagDataList.get(position).getIdT();
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mTagDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView title;
        TextView releaseDate;
        TextView rattingView;
        Button mButon;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.NtitleView);
            releaseDate = (TextView) itemView.findViewById(R.id.releseDateView);
            rattingView = (TextView) itemView.findViewById(R.id.firstRatting);
            mButon = (Button) itemView.findViewById(R.id.more_button);
        }
    }
}
