package com.developer.ashwoolford.moviesfreak.search_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.ashwoolford.moviesfreak.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.developer.ashwoolford.moviesfreak.R.id.swipeRefreshView3;

public class SearchResult extends AppCompatActivity {

    private SearchAdapter mSearchAdapter;
    private RecyclerView mRecyclerView ;
    private GridLayoutManager mGridLayoutManager;
    private SearchData mSearchData;
    private List<SearchData> mSearchDataList;
    private String Query_Data;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private TextView mTextView;
    private ImageView mImageView;
    private TextView offlineView;
    private RelativeLayout mRelativeLayout;
    private ImageView mNetworkImageView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    //for recycleview

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int itemCount=1;



    //network listener

    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private TextView networkStatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //intent filter

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);



       mRelativeLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        mNetworkImageView = (ImageView) findViewById(R.id.networkViewImage);
        mNetworkImageView.setImageResource(R.drawable.offline);
        mNetworkImageView.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(swipeRefreshView3);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorGreen));

        mRelativeLayout.setVisibility(View.VISIBLE);
        offlineView = (TextView) findViewById(R.id.offlineTextView);
       mImageView = (ImageView) findViewById(R.id.offlineView);
        mTextView = (TextView) findViewById(R.id.resultTextView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mRecyclerView= (RecyclerView) findViewById(R.id.newRecyclerView);
        mSearchDataList = new ArrayList<>();
        mGridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mSearchAdapter=new SearchAdapter(this,mSearchDataList);
        mRecyclerView.setAdapter(mSearchAdapter);


        Intent intent = getIntent();
        Query_Data = intent.getStringExtra("string");


        //reload again if 1st page is finished


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
                Log.i("ashraf!", "Visible_item= "+visibleItemCount+"totalItemCount= "+totalItemCount+"firstVisible= "+firstVisibleItem);

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("ashraf!", "end called");

                    // Do something
                    itemCount = itemCount+1;
                    load_data_from_server(Query_Data);

                    loading = true;
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load_data_from_server(Query_Data);
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });



    }

    private void load_data_from_server(final String searchKey){

        AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            String json_url;
            String Json_string;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try{
                    json_url = "https://api.themoviedb.org/3/search/movie?api_key=c2226e0e5206de2cca3ab22468c07647&language=en-US&query="+searchKey+"&page="+itemCount;

                }catch(Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... strings) {
               final OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(json_url)
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

                if(s!=null){
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                   // Toast.makeText(getApplicationContext(),""+ jsonObject.getInt("total_results"),Toast.LENGTH_LONG).show();
                    if(jsonObject.getInt("total_results")==0){
                        mTextView.setText("Sorry no data found");

                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("results");



                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        mSearchData = new SearchData(jsonObject1.getString("title"),
                                "https://image.tmdb.org/t/p/w500"+jsonObject1.getString("poster_path"),
                                jsonObject1.getString("release_date"),
                                jsonObject1.getLong("id"),jsonObject1.getDouble("vote_average"));


                        mSearchDataList.add(mSearchData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("ashraf","searchException");
                }
                mSearchAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

        };


        task.execute(searchKey);
    }




    //

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);

        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if(!isConnected){
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                load_data_from_server(Query_Data);
                                mRelativeLayout.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.VISIBLE);
                                mNetworkImageView.setVisibility(View.INVISIBLE);
                                isConnected = true;
                            }
                            return true;
                        }
                    }
                }
            }

            Log.v(LOG_TAG, "You are not connected to Internet!");
            showDialog();
            mRelativeLayout.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mNetworkImageView.setVisibility(View.VISIBLE);

          //  Toast.makeText(getApplicationContext(),"There is no network connection", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }


    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchResult.this);
        builder.setMessage("Sorry there is no internet connection right now")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Connection Error");
        dialog.show();
    }

}
