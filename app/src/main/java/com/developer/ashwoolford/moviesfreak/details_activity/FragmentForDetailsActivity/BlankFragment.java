package com.developer.ashwoolford.moviesfreak.details_activity.FragmentForDetailsActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.ashwoolford.moviesfreak.R;

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


public class BlankFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<ImagesData> mImagesDataList;
    private LinearLayoutManager mLinearLayoutManager;
    private CustomAdapter mCustomAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);


        //get Data from Activity

        Bundle bundle = getArguments();
        String myValue = bundle.getString("message");
       // Toast.makeText(getActivity(),myValue,Toast.LENGTH_LONG).show();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleer_View_forFragment);
        mImagesDataList = new ArrayList<>();
        mCustomAdapter = new CustomAdapter(getActivity(),mImagesDataList);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mCustomAdapter);

        load_data_from_server(myValue);


        return view;

    }


    //asysnc Task

    private void load_data_from_server(final String movieId){

        AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            String json_url;
            String Json_string;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try{
                    json_url = "https://api.themoviedb.org/3/movie/"+movieId+"/credits?api_key=c2226e0e5206de2cca3ab22468c07647";
                    //json_url = "https://api.themoviedb.org/3/"+searchKey+"/popular?api_key=c2226e0e5206de2cca3ab22468c07647&language=en-US";

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
                    JSONArray jsonArray = jsonObject.getJSONArray("cast");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        ImagesData mData = new ImagesData("https://image.tmdb.org/t/p/w500"+jsonObject1.getString("profile_path")
                                ,jsonObject1.getString("name"));
                        mImagesDataList.add(mData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("ashraf","details page exception");
                }
                mCustomAdapter.notifyDataSetChanged();
            }

        };


        task.execute(movieId);
    }

}
