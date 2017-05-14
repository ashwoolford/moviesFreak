package com.developer.ashwoolford.moviesfreak.details_activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.developer.ashwoolford.moviesfreak.CompaniesData.CoAdapter;
import com.developer.ashwoolford.moviesfreak.CompaniesData.CompanyData;
import com.developer.ashwoolford.moviesfreak.FirebaseHelperPackage.SavedListData;
import com.developer.ashwoolford.moviesfreak.R;
import com.developer.ashwoolford.moviesfreak.Trailer.TrailerActivity;
import com.developer.ashwoolford.moviesfreak.details_activity.FragmentForDetailsActivity.BlankFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG ="DetailsActivity" ;
    private List<CompanyData> companaiesList;
    private CoAdapter mCoAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout backdropPath;
    private TextView title;
    private TextView overview;
    private TextView rating;
    private TextView tagline;
    private TextView language;
    private TextView originalTitle;
    private TextView releaseDate;
    private TextView status;
    private TextView budget;
    private TextView revenue;
    private TextView runtimeOfMovie;
    private LinearLayoutManager mLinearLayoutManager;
    private Button trailerButton;
    private String idOfMovie;
    private String genreList,statusS,mTitleMovie;
    private TextView genreView;
    private ImageView mImageView;
    private ImageView mSaveList;
    private String backdrop;
    private Button mRateButton;
    private ScrollView mScrollView;
    private boolean isConnected = false;
    private String sessionId;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mUserRef,mSavedListRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //get data from otheractivity

        Intent intent= getIntent();
        idOfMovie = String.valueOf(intent.getLongExtra("id",1));


        //initializing firebase instance variables

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("rateRecord");
        mSavedListRef = mFirebaseDatabase.getReference().child("users_favorite_list");
        mUserRef = mDatabaseReference.child(FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid().toString());


       mDatabaseReference.keepSynced(true);
        mSavedListRef.keepSynced(true);

        //view implementation
        mImageView = (ImageView) findViewById(R.id.backArrow);
        genreView = (TextView) findViewById(R.id.genreTextView);
        title = (TextView) findViewById(R.id.movie_title);
        backdropPath = (LinearLayout) findViewById(R.id.backdropImageBackground);
        overview = (TextView) findViewById(R.id.overview);
        rating = (TextView) findViewById(R.id.ratting);
        tagline = (TextView) findViewById(R.id.tagline);
        language = (TextView) findViewById(R.id.language);
        originalTitle = (TextView) findViewById(R.id.original_title);
        releaseDate = (TextView) findViewById(R.id.release_date);
        status = (TextView) findViewById(R.id.movie_status);
        budget = (TextView) findViewById(R.id.budget);
        revenue = (TextView) findViewById(R.id.revenue);
        runtimeOfMovie = (TextView) findViewById(R.id.runtime);
        trailerButton = (Button) findViewById(R.id.trailerButton);
        mRateButton = (Button) findViewById(R.id.rate_button);
        mSaveList = (ImageView) findViewById(R.id.makeListButton);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);


        //array and list Adapter
        load_data_from_server(idOfMovie);


        String[] ss = new String[]{"ashraf"};

        trailerButton.setVisibility(View.INVISIBLE);

        companaiesList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.companies_list_view);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mCoAdapter = new CoAdapter(getApplicationContext(),companaiesList);
        mRecyclerView.setAdapter(mCoAdapter);

        int i = getSupportFragmentManager().beginTransaction().add(R.id.MainFragmentContainer,sendDataToFragment()).commit();
        Log.d("ashraf","supportFragment_"+i);


        //
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                trailerButton.setVisibility(View.VISIBLE);
            }
        }, 6000);

        getSessionId();



        //perform the back arrow button

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //rate button

        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });


        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean aa = dataSnapshot.child(idOfMovie).exists();
                Log.d("ashraf","exist "+aa);
                if(aa){
                    mRateButton.setText("Rated");
                    mRateButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRateButton.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    mRateButton.setText("Rate this");
                    mRateButton.setBackgroundColor(getResources().getColor(R.color.white1));
                    mRateButton.setTextColor(getResources().getColor(R.color.black1));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ashraf","database error");

            }
        });

        mSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myRef = mSavedListRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
                SavedListData data = new SavedListData(mTitleMovie,idOfMovie,genreList,statusS,backdrop);
                myRef.setValue(data);

                Toast.makeText(getApplicationContext(),"successfully added",Toast.LENGTH_SHORT).show();


            }
        });

        performTrailerbutton();



    }

    private void customDialog() {
        final Dialog dialog = new Dialog(DetailsActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rattingBar);
        final TextView textView = (TextView) dialog.findViewById(R.id.ratingShow);
        Button submit = (Button) dialog.findViewById(R.id.dialogSubmitButton);
        Button cancelBtn = (Button) dialog.findViewById(R.id.dialogCancelButton);
        final float[] rate = new float[1];

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(idOfMovie).exists() == false){
                    Log.d("ashraf","ratting : no ratting found");
                }
                else {
                    String as = dataSnapshot.child(idOfMovie).child("ratting").getValue().toString();
                    ratingBar.setRating(Float.parseFloat(as)/2);
                    Log.d("ashraf","ratting : "+as);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      //  ratingBar.setRating((float) 5);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                textView.setText("Rating: "+v*2);
                rate[0] = v;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserRef.child(idOfMovie).child("ratting").setValue((float)rate[0]*2);
                makeRating(sessionId,rate[0]*2);
                Toast.makeText(getApplicationContext(),"Successfully Rated",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserRef.child(idOfMovie).removeValue();
                unRating(sessionId);
                Toast.makeText(getApplicationContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Async task

    private void load_data_from_server(final String searchKey){

        AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            String json_url;
            String Json_string;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try{
                    //json_url = "https://api.themoviedb.org/3/movie/274870/credits?api_key=c2226e0e5206de2cca3ab22468c07647";
                    json_url = "https://api.themoviedb.org/3/movie/"+searchKey+"?api_key=c2226e0e5206de2cca3ab22468c07647&language=en-US";
                }catch(Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... strings) {

                try {
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((Json_string = bufferedReader.readLine()) != null) {

                        stringBuilder.append(Json_string + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("production_companies");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("genres");
                    String [] genres = new String[jsonArray1.length()];

                    for(int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                        genres[i] = jsonObject1.getString("name");

                    }

                    if(jsonArray1.length()==1){
                        genreView.setText(genres[0]);
                        genreList = genres[0];
                    }
                    else if(jsonArray1.length()==2){
                        genreView.setText(genres[0]+", "+genres[1]);
                        genreList = genres[0]+", "+genres[1];
                    }
                    else if(jsonArray1.length()==3){
                        genreView.setText(genres[0]+", "+genres[1]+", "+genres[2]);
                        genreList = genres[0]+", "+genres[1];

                    }
                    else if(jsonArray1.length()>=4){
                        genreView.setText(genres[0]+", "+genres[1]+", "+genres[2]+", "+genres[3]);
                        genreList = genres[0]+", "+genres[1];
                    }
                    else {
                        genreView.setText("No data");
                        genreList = "N/A";
                    }

                    //genre for database

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CompanyData companyData = new CompanyData(jsonObject1.getString("name"));
                        companaiesList.add(companyData);
                    }

                    backdrop =  jsonObject.getString("backdrop_path");
                    Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500/"+backdrop).asBitmap().into(new SimpleTarget<Bitmap>(600, 300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                backdropPath.setBackground(drawable);
                            }
                        }
                    });
                    mTitleMovie = jsonObject.getString("title");
                    title.setText(mTitleMovie);
                    String overview1 =  jsonObject.getString("overview");
                    overview.setText(overview1);

                    double ratting =  jsonObject.getDouble("vote_average");
                    //DecimalFormat df = new DecimalFormat("#.#");

                    //rating.setText(""+df.format(ratting));
                    rating.setText(""+ratting);
                    String tagline1 =  jsonObject.getString("tagline");
                    tagline.setText(tagline1);
                    String languageS =  jsonObject.getString("original_language");
                    language.setText(languageS);

                    String originalTitleS =  jsonObject.getString("original_title");
                    originalTitle.setText(originalTitleS);

                    String releaseDateS =  jsonObject.getString("release_date");
                    releaseDate.setText(releaseDateS);
                    statusS =  jsonObject.getString("status");
                    status.setText(statusS);

                    long budgetL =  jsonObject.getLong("budget");
                    if(budgetL==0){
                        budget.setText("No record found");
                    }
                    else {
                        budget.setText("$"+budgetL+" (estimated)");
                    }


                    long revenueL =  jsonObject.getLong("revenue");

                    if(revenueL==0){
                        revenue.setText("No record found");
                    }
                    else {
                        revenue.setText("$"+revenueL+" (estimated)");
                    }





                    runtimeOfMovie.setText( timeConverter(jsonObject.getInt("runtime")));




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("ashraf","details page Null pointer");
                }

                mCoAdapter.notifyDataSetChanged();
            }

        };


        task.execute(searchKey);
    }

    //get a session id

    public void getSessionId(){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, "{}");
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/authentication/guest_session/new?api_key=c2226e0e5206de2cca3ab22468c07647")
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    final JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getBoolean("success")){

                      sessionId = jsonObject.getString("guest_session_id");
                        Log.d(TAG,"id = "+sessionId);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG,"Boolean"+jsonObject.getBoolean("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d(TAG,"session id exception");
                }


            }
        }.execute();
    }

    // post rating to server

    public void makeRating(final String id, final float value){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"value\":"+value+"}");
                Log.d(TAG,"ratting valu is = "+value);
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/372058/rating?api_key=c2226e0e5206de2cca3ab22468c07647&guest_session_id="+id)
                        .post(body)
                        .addHeader("content-type", "application/json;charset=utf-8")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("status_message").equals("Success.")){
                       // Toast.makeText(getApplicationContext(),"Successfully rated",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                Log.d(TAG,"rating status "+s);

            }
        }.execute();
    }

    //unrate

    public void unRating(final String id){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, "{}");
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/372058/rating?api_key=c2226e0e5206de2cca3ab22468c07647&guest_session_id="+id)
                        .delete(body)
                        .addHeader("content-type", "application/json;charset=utf-8")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                Log.d(TAG,"rating delete status "+s);

            }
        }.execute();
    }




    //time converter
    public String timeConverter(int totalMin){
        int hr = totalMin/60;
        int min = totalMin%60;
        if(hr>1 && min==1){
            return hr+" hours "+min+" minute";
        }
        if(hr==1 && min>1){
            return hr+" hour "+min+" minutes";
        }

        if(hr>1 && min>1){
            return hr+" hours "+min+" minutes";

        }
        return  hr+" hour "+min+" min";
    }

    public Fragment sendDataToFragment(){
        Bundle bundle = new Bundle();
        bundle.putString("message", idOfMovie);
        BlankFragment fragInfo = new BlankFragment();
        fragInfo.setArguments(bundle);
        return fragInfo;
    }

    public void performTrailerbutton(){
        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this,TrailerActivity.class);
                intent.putExtra("movieID",idOfMovie);
                startActivity(intent);
            }
        });
    }
}
