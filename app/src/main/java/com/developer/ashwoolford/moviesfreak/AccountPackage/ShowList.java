package com.developer.ashwoolford.moviesfreak.AccountPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.ashwoolford.moviesfreak.R;
import com.developer.ashwoolford.moviesfreak.details_activity.DetailsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowList extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private Boolean hasChild = false;
    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarL);
        mResultView = (TextView) findViewById(R.id.resultTextViewL);
        mProgressBar.setVisibility(View.VISIBLE);


        mRecyclerView = (RecyclerView) findViewById(R.id.newRecyclerViewList);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager  = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);




        //initializing firebase instance variables

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users_favorite_list");
        mDatabaseReference.keepSynced(true);
        final DatabaseReference myRef = mDatabaseReference.child(FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid()
                .toString());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseRecyclerAdapter<ListData,ListViewHolder> fireBaseRA =
                new FirebaseRecyclerAdapter<ListData, ListViewHolder>(
                        ListData.class,
                        R.layout.item_view,
                        ListViewHolder.class,
                        myRef
                ) {
                    @Override
                    protected void populateViewHolder(ListViewHolder viewHolder, final ListData model, final int position) {
                        viewHolder.setMovieName(model.getMovieName());
                        viewHolder.setGenre(model.getMovieGenre());
                        viewHolder.setStatusView(model.getStatus());
                        viewHolder.setPoster(model.getPosterUri());

                        if(model != null){
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ShowList.this, DetailsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Long id = Long.valueOf(model.getMovieId());
                                intent.putExtra("id",id);
                                startActivity(intent);

                            }
                        });

                        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(ShowList.this);
                                dialog.setTitle("Delete From Watch List");
                                dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.d("",getRef(position).getKey().toString());
                                        getRef(position).removeValue();


                                    }
                                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialog.create().show();
                                return true;
                            }
                        });



                    }
                };

        mRecyclerView.setAdapter(fireBaseRA);


       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               Log.d("ashraf",""+dataSnapshot.exists());
               if(dataSnapshot.exists()){

               }else {
                   mProgressBar.setVisibility(View.INVISIBLE);
                   mResultView.setText("No Data Found");

               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });






    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView movieNameView;
        TextView genreView;
        TextView statusView;
        ImageView imageView;



        public ListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setMovieName(String movieName){
            movieNameView = (TextView) mView.findViewById(R.id.watch_list_movietitle);
            movieNameView.setText(movieName);
        }

        public void setGenre(String genre){
            genreView = (TextView) mView.findViewById(R.id.watch_list_genre);
            genreView.setText(genre);
        }

        public void setStatusView(String status){
            statusView = (TextView) mView.findViewById(R.id.watch_list_status);
            statusView.setText(status);

        }
        public void setPoster(String poster){
            imageView = (ImageView) mView.findViewById(R.id.watch_list_poster);
            Glide.with(mView.getContext()).load("https://image.tmdb.org/t/p/w500"+poster).into(imageView);
        }


    }

}
