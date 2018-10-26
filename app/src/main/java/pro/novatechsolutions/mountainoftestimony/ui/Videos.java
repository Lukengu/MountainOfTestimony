package pro.novatechsolutions.mountainoftestimony.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pro.novatechsolutions.mountainoftestimony.AppConfig;
import pro.novatechsolutions.mountainoftestimony.R;
import pro.novatechsolutions.mountainoftestimony.ui.adapters.VideoListAdapter;

public class Videos extends Fragment {

    private AccessToken accessToken;
    private RecyclerView mRecycler;
    private VideoListAdapter videoListAdapter;
    private ArrayList<JSONObject> objects = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView video_count;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.videos_list, container, false);
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
        mRecycler  = view.findViewById(R.id.mRecycler);
        video_count   = view.findViewById(R.id.video_count);

        videoListAdapter =  new VideoListAdapter(getActivity(),objects);
        mRecycler.setAdapter(videoListAdapter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mRecycler.setLayoutManager(mGridLayoutManager);
        mRecycler.setHasFixedSize(false);

        progressDialog=  new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);


        progressDialog.show();
        populateFacebookVideos();
        return  view;
    }

    private void populateFacebookVideos() {
        //AccessToken.getCurrentAccessToken().getToken();
        accessToken  = new AccessToken( AppConfig.ACCESS_TOKEN,AppConfig.APP_ID,AppConfig.PAGEID, null, null, null, null, null);

        //  Log.e("ACCESS_TOKEN", accessToken.getToken());

        new GraphRequest(
                accessToken ,
                "/"+ AppConfig.PAGEID+"/videos?fields=icon,live_status,description,published,title,source,id,created_time,picture&limit=100000",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        progressDialog.dismiss();
                        try {
                            JSONArray data = response.getJSONObject().getJSONArray("data");
                           // Log.e("DATA", data.toString());

                            for(int i =0; i < data.length(); i++){
                                if(!"151123722498786".equals(data.getJSONObject(i).getString("id")) &&
                                        "VOD".equals(data.getJSONObject(i).optString("live_status"))) {
                                    videoListAdapter.addVideo(data.getJSONObject(i));
                                }
                            }

                            //Toast.makeText(getActivity(), String.format(getActivity().getString(R.string.nbr_videos),videoListAdapter.getItemCount()), Toast.LENGTH_LONG).show();
                            video_count.setText(String.format(getActivity().getString(R.string.nbr_videos),videoListAdapter.getItemCount()));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Log.e("Videos",  .toString());


                    }
                }
        ).executeAsync();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
