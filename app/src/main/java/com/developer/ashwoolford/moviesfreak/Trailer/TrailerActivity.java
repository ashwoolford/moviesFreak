package com.developer.ashwoolford.moviesfreak.Trailer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.developer.ashwoolford.moviesfreak.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

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

public class TrailerActivity extends YouTubeBaseActivity {

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private List<String> mList;
    private ImageButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        Intent intent = getIntent();

        mList = new ArrayList<>();

        mButton = (ImageButton) findViewById(R.id.trailerButton);
        load_data_from_server(intent.getStringExtra("movieID"));
        Log.d("ashraf","movie id:"+intent.getStringExtra("movieID"));

        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.view);

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                try {
                    youTubePlayer.loadVideo(mList.get(0));
                } catch (NullPointerException e){
                    Log.d("ashraf","youtube null pointer");
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };








    }


    private void load_data_from_server(final String searchKey){

        AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            String json_url;
            String Json_string;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try{
                    //json_url = "https://api.themoviedb.org/3/movie/274870/credits?api_key=c2226e0e5206de2cca3ab22468c07647";
                    json_url = "https://api.themoviedb.org/3/movie/"+searchKey+"/videos?api_key=c2226e0e5206de2cca3ab22468c07647&language=en-US";
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    if(jsonArray.length()==0){
                        Log.d("ashraf","not found");
                        showDialog();
                        //dialog
                    }
                    else {
                        mButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mYouTubePlayerView.initialize("AIzaSyDpabguTY3R-nyCuWehLk-b2zkdAYuR-y8",mOnInitializedListener);
                                mButton.setVisibility(View.INVISIBLE);
                            }
                        });
                        Log.d("ashraf","found");
                    }

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mList.add(jsonObject1.getString("key"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("ashraf","traileroageException");
                }

            }

        };


        task.execute(searchKey);
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TrailerActivity.this);
        builder.setMessage("Sorry This movie trailer is not found")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Not found");
        dialog.show();
    }
}
