package com.developer.ashwoolford.moviesfreak.first_fragment;

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
 * Created by ashwoolford on 11/25/2016.
 */
public class UMoviesAdapter extends RecyclerView.Adapter<UMoviesAdapter.ViewHolder> {
    Context context;
    List<UMoviesData> mUMoviesDatas;

    public UMoviesAdapter(Context context,List<UMoviesData> mUMoviesDatas){
          this.context = context;
          this.mUMoviesDatas = mUMoviesDatas;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.titleView.setText(mUMoviesDatas.get(position).getTitle());
        holder.dateView.setText(mUMoviesDatas.get(position).getReleaseYear());
        DecimalFormat df = new DecimalFormat("#.#");
        holder.rattingView.setText(""+df.format(mUMoviesDatas.get(position).getVote_average()));
        Picasso.with(context)
                .load(mUMoviesDatas.get(position)
                .getPoster())
                .resize(200,400)
                .centerCrop()
                .into(holder.posterView);


            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Long id = mUMoviesDatas.get(position).getFId();
                    intent.putExtra("id",id);
                    context.startActivity(intent);
                }
            });

            holder.posterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Long id = mUMoviesDatas.get(position).getFId();
                    intent.putExtra("id",id);
                    context.startActivity(intent);

                }
            });




    }

    @Override
    public int getItemCount() {
        return mUMoviesDatas.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView,dateView,rattingView;
        ImageView posterView;
        Button mButton;


        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.NtitleView);
            dateView = (TextView) itemView.findViewById(R.id.releseDateView);
            posterView = (ImageView) itemView.findViewById(R.id.poster);
            mButton = (Button) itemView.findViewById(R.id.more_button);
            rattingView = (TextView) itemView.findViewById(R.id.firstRatting);
        }
    }


}

