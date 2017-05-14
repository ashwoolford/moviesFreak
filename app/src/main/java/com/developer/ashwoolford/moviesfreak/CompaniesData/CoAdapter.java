package com.developer.ashwoolford.moviesfreak.CompaniesData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.ashwoolford.moviesfreak.R;

import java.util.List;

/**
 * Created by ashwoolford on 1/14/2017.
 */
public class CoAdapter  extends RecyclerView.Adapter<CoAdapter.ViewHolder>{
    List<CompanyData> mList;
    Context context;

    public CoAdapter(Context context,List<CompanyData> list) {
        mList = list;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.companieslistlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position).getCompaniesName());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.listRecyclerText);
        }
    }
}
