package com.developer.ashwoolford.moviesfreak.second_fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.developer.ashwoolford.moviesfreak.R;
import com.developer.ashwoolford.moviesfreak.search_activity.SearchResult;
import com.developer.ashwoolford.moviesfreak.tagResult.TagResultActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fragment2 extends Fragment {

    RecyclerView mRecyclerView;
    SuggestMovieAdapter mSuggestMovieAdapter;
    LinearLayoutManager mLinearLayoutManager;
    List<SuggestMovieData> mSuggestMovieDataList;
    MaterialSearchBar mSearchView;
    LinearLayout mLinearLayout;
    String url;

    //tag button

    Button horrorButton;
    Button adventureButton;
    Button scfiButton;
    Button romanceButton;
    Button animationButton;
    Button hackingButton;
    Button comedyButton;
    Button familyButton;
    Button dramaButton;
    Button actionButton;




    //for invisivility

    ImageView imageView;

    //network listener
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment2, container, false);



        //intent filter

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(receiver, filter);



        //for tag

        horrorButton = (Button) view.findViewById(R.id.horror);
        adventureButton = (Button) view.findViewById(R.id.adventure);
        scfiButton = (Button) view.findViewById(R.id.sc_fi);
        romanceButton = (Button) view.findViewById(R.id.romance);
        animationButton = (Button) view.findViewById(R.id.animation);
        hackingButton = (Button) view.findViewById(R.id.hacking);
        comedyButton = (Button) view.findViewById(R.id.comedy);

        familyButton = (Button) view.findViewById(R.id.family);

        dramaButton = (Button) view.findViewById(R.id.drama);
        actionButton = (Button) view.findViewById(R.id.action);


        horrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","15077");
                startActivity(intent);
            }
        });
        adventureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","4414");
                startActivity(intent);
            }
        });
        scfiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","10568");
                startActivity(intent);
            }
        });
        romanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","9840");
                startActivity(intent);
            }
        });
        animationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","10336");
                startActivity(intent);
            }
        });
        hackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","12361");
                startActivity(intent);
            }
        });
        comedyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","10267");
                startActivity(intent);
            }
        });
        familyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","18035");
                startActivity(intent);
            }
        });
        dramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","8029");
                startActivity(intent);
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagResultActivity.class);
                intent.putExtra("tag","210313");
                startActivity(intent);
            }
        });




        imageView = (ImageView) view.findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.offline);
        imageView.setVisibility(View.INVISIBLE);

        mSearchView = (MaterialSearchBar) view.findViewById(R.id.searchView);
        mSearchView.setHint("Search On Movie Freak");

        mSuggestMovieDataList = new ArrayList<>();



         mLinearLayout = (LinearLayout) view.findViewById(R.id.linerLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle);

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuggestMovieAdapter = new SuggestMovieAdapter(getActivity(),mSuggestMovieDataList);
        mRecyclerView.setAdapter(mSuggestMovieAdapter);


        mSearchView.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean b) {

            }

            @Override
            public void onSearchConfirmed(CharSequence charSequence) {
                Intent intent = new Intent(getActivity(),SearchResult.class);
//                String st = charSequence.toString().;
                intent.putExtra("string",charSequence.toString().replaceAll("\\s+","%20"));
                getActivity().startActivity(intent);
            }

            @Override
            public void onButtonClicked(int i) {

            }
        });


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
                    json_url = "https://api.themoviedb.org/3/movie/popular?api_key=c2226e0e5206de2cca3ab22468c07647&language=en-US";


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
                    Response response = null;
                    if(response==null){
                        response = client.newCall(request).execute();
                        return response.body().string();
                    }
                    else {

                    }

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

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                       SuggestMovieData suggestMovieData = new SuggestMovieData(jsonObject1.getDouble("vote_average")
                               ,"https://image.tmdb.org/t/p/w500"+jsonObject1.getString("poster_path"),jsonObject1.getLong("id"));
                        mSuggestMovieDataList.add(suggestMovieData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("ashraf","fragment 2 exception");
                }
                mSuggestMovieAdapter.notifyDataSetChanged();
            }

        };


        task.execute(searchKey);
    }

    //broadcast network listener

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
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
                                load_data_from_server("");
                                mLinearLayout.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.INVISIBLE);
                                isConnected = true;
                            }
                            return true;
                        }
                    }
                }
            }

            mLinearLayout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);

           // Toast.makeText(getActivity(),"There is no internet connection",Toast.LENGTH_LONG).show();
            isConnected = false;
            return false;
        }
    }

}
