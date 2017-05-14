package com.developer.ashwoolford.moviesfreak.details_activity.FragmentForDetailsActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.ashwoolford.moviesfreak.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ashwoolford on 1/14/2017.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    List<ImagesData> mImagesDataList;
    Context context;

    public CustomAdapter(Context context , List<ImagesData> imagesDataList) {
        mImagesDataList = imagesDataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipeee,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Glide.with(context).load(mImagesDataList.get(position).getPath()).into(holder.mImageView);
        Picasso.with(context).load(mImagesDataList.get(position).getPath())
                .fit()
                .into(holder.mImageView);

        holder.mTextView.setText(mImagesDataList.get(position).getCastNames());

    }

    @Override
    public int getItemCount() {
        return mImagesDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.itemImage_forFragment);
            mTextView = (TextView) itemView.findViewById(R.id.castNames);
        }
    }
}

