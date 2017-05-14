package com.developer.ashwoolford.moviesfreak.AccountPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.ashwoolford.moviesfreak.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountPage extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mImageView;
    private TextView mEmailView;
    private TextView mDisplayName;
    private TextView mWatchList;
    private TextView mFeedBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        mImageView = (ImageView) findViewById(R.id.profilepic);
        mEmailView = (TextView) findViewById(R.id.displayEmailView);
        mDisplayName = (TextView) findViewById(R.id.displayName);
        mWatchList = (TextView) findViewById(R.id.watchlistView);
        mFeedBackView = (TextView) findViewById(R.id.feedbackView);
        try {
            mFeedBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Email = new Intent(Intent.ACTION_SEND);
                    Email.setType("text/email");
                    Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "ashwoolford007@gmail.com" });
                    Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    Email.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(Email, "Send Feedback:"));
                }
            });
        } catch (NullPointerException e){

        } catch (Exception e) {
            e.printStackTrace();
        }

        mWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountPage.this,ShowList.class));
            }
        });


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userPhotoUrl = null;
        try {
            userPhotoUrl= user.getPhotoUrl().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if(userPhotoUrl==null){
            Glide.with(getApplicationContext())
                    .load(R.drawable.user)
                    .into(mImageView);
        }
        else{
            Glide.with(getApplicationContext())
                    .load(user.getPhotoUrl()
                            .toString())
                    .into(mImageView);
        }

        mEmailView.setText(user.getEmail());
        mDisplayName.setText(user.getDisplayName());





    }
}
