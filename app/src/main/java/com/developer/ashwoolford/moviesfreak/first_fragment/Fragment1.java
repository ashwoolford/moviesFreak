package com.developer.ashwoolford.moviesfreak.first_fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.ashwoolford.moviesfreak.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    private RecyclerView mRecyclerView ;
    private GridLayoutManager mGridLayoutManager;
    private UMoviesAdapter mUMoviesAdapter;
    private List<UMoviesData> mUMoviesDatas;
    public UMoviesData mUMoviesData;
    ProgressBar mProgressBar;
    int itemCount=1;

    //offline view

    private ImageView offlineView;
    private TextView offlineText;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    //for recycleview

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;



    //broadcast receiver

    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private TextView networkStatus;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1, container, false);

        offlineView = (ImageView) view.findViewById(R.id.offlineView);
        offlineView.setImageResource(R.drawable.offline);
        offlineText = (TextView) view.findViewById(R.id.offlineTextView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshView1);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorGreen));

        mUMoviesDatas = new ArrayList<>();
        //

        offlineView.setVisibility(View.INVISIBLE);



        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mUMoviesAdapter = new UMoviesAdapter(getActivity(),mUMoviesDatas);
        mRecyclerView.setAdapter(mUMoviesAdapter);

        AdView myAdView =  new AdView(getActivity());

        MobileAds.initialize(getActivity(),"ca-app-pub-7754575952527726~7551199890");
        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if(isNetworkAvailable() == true){
            load_data_from_server("");
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        //intent filter

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(receiver, filter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    load_data_from_server("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
                //Log.i("ashraf!", "Visible_item= "+visibleItemCount+"totalItemCount= "+totalItemCount+"firstVisible= "+firstVisibleItem);

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
                    try {
                        load_data_from_server("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loading = true;
                }
            }
        });



       // load_data_from_server("");


        return view;
    }

    private void load_data_from_server(final String searchKey){

        AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            String json_url;
            String Json_string;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try{
                    json_url = "https://api.themoviedb.org/3/movie/now_playing?api_key=c2226e0e5206de2cca3ab22468c07647&language=en-US&page="+itemCount;

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

                if(s!=null){
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            mUMoviesData =new UMoviesData(jsonObject1.getString("title"),
                                    "https://image.tmdb.org/t/p/w500"+jsonObject1.getString("poster_path"),
                                    jsonObject1.getString("release_date"),jsonObject1.getLong("id"),jsonObject1.getDouble("vote_average"));
                            mUMoviesDatas.add(mUMoviesData);
                        }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("ashraf","null pointer");
                }


                mUMoviesAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);


            }

        };


        task.execute(searchKey);
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }





    //network change receiver

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
                                try {
                                    load_data_from_server("");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mRecyclerView.setVisibility(View.VISIBLE);
                                offlineView.setVisibility(View.INVISIBLE);
                               // mProgressBar.setVisibility(View.VISIBLE);

                               // Toast.makeText(getActivity(),"connected",Toast.LENGTH_LONG).show();
                                isConnected = true;
                            }
                            return true;
                        }
                    }
                }
            }

            Log.v(LOG_TAG, "You are not connected to Internet!");
            mRecyclerView.setVisibility(View.INVISIBLE);
            offlineView.setVisibility(View.VISIBLE);
           // mProgressBar.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),"There is no internet connection",Toast.LENGTH_LONG).show();
            isConnected = false;
            return false;
        }
    }

}
