package com.developer.ashwoolford.moviesfreak.second_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.ashwoolford.moviesfreak.R;
import com.developer.ashwoolford.moviesfreak.details_activity.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ashwoolford on 11/16/2016.
 */
public class SuggestMovieAdapter extends RecyclerView.Adapter<SuggestMovieAdapter.ViewHolder> {
    Context context;
    List<SuggestMovieData> suggestMovieDataList;

    public SuggestMovieAdapter(Context context,List<SuggestMovieData> suggestMovieDataList){
        this.context=context;
        this.suggestMovieDataList = suggestMovieDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Picasso.with(context).load(suggestMovieDataList.get(position).getPoster())
                .fit()
                .into(holder.cardImageView);
        DecimalFormat df = new DecimalFormat("#.#");
        holder.cardTextView.setText(""+df.format(suggestMovieDataList.get(position).getnTitle()));
        holder.cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Long id = suggestMovieDataList.get(position).getMovieId();
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Long id = suggestMovieDataList.get(position).getMovieId();
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return suggestMovieDataList.size();
    }

    //viewHolderClass

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView cardTextView;
        public ImageView cardImageView;
        public Button mButton;


        public ViewHolder(View itemView) {
            super(itemView);
            cardTextView = (TextView) itemView.findViewById(R.id.tvTitle);
            cardImageView = (ImageView) itemView.findViewById(R.id.itemImage);
            mButton = (Button) itemView.findViewById(R.id.SuggestedButton);
        }
    }

}
