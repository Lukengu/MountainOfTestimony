package pro.novatechsolutions.mountainoftestimony.ui;

import android.icu.util.Calendar;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pro.novatechsolutions.mountainoftestimony.AppConfig;
import pro.novatechsolutions.mountainoftestimony.R;
import pro.novatechsolutions.mountainoftestimony.ui.adapters.EventSummaryAdapter;
import pro.novatechsolutions.mountainoftestimony.ui.utils.MyVideoView;
import pro.novatechsolutions.mountainoftestimony.ui.utils.SquareImageView;

public class Home  extends Fragment  {

    TextView welcome_note;
    JSONObject  weekly_message;
    //JSONObject  next_event_object;
    JSONObject  latest_video_object;

    ProgressBar progress_bar;
    ImageView play_button,replay_button,placeholder;
    VideoView weekly_plan;
    TextView week_plan_created;
    AccessToken accessToken;
    SquareImageView next_event, latest_video;

    EventSummaryAdapter eventSummaryAdapter;
    ArrayList<JSONObject> events = new ArrayList<>();
    RecyclerView next_events_list;
    int start = 0;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.home, container, false);

        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());

        welcome_note = view.findViewById(R.id.welcome_note);
        progress_bar  = view.findViewById(R.id.progress_bar);
        replay_button  = view.findViewById(R.id.replay_button);
        play_button  = view.findViewById(R.id.play_button);
        placeholder  = view.findViewById(R.id.placeholder);
        weekly_plan  = view.findViewById(R.id.weekly_plan);
        week_plan_created = view.findViewById(R.id.week_plan_created);
      //  next_event = view.findViewById(R.id.next_event);
        //latest_video = view.findViewById(R.id.latest_video);
        next_events_list =  view.findViewById(R.id.next_events_list);

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), weekly_message.optString("source"), Toast.LENGTH_LONG).show();
                weekly_plan.setVideoURI(Uri.parse(weekly_message.optString("source")));
                weekly_plan.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        play_button.setVisibility(View.GONE);
                        placeholder.setVisibility(View.GONE);
                        weekly_plan.setVisibility(View.VISIBLE);
                        mp.start();
                    }
                });
            }
        });

        replay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekly_plan.setVideoURI(Uri.parse(weekly_message.optString("source")));
                weekly_plan.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        replay_button.setVisibility(View.GONE);
                        placeholder.setVisibility(View.GONE);

                        mp.start();
                    }
                });
            }
        });

        weekly_plan.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                placeholder.setVisibility(View.VISIBLE);
                replay_button.setVisibility(View.VISIBLE);
            }
        });

        progress_bar.setVisibility(View.VISIBLE);

        accessToken  = new AccessToken( AppConfig.ACCESS_TOKEN,AppConfig.APP_ID,AppConfig.PAGEID, null, null, null, null, null);

        //  Log.e("ACCESS_TOKEN", accessToken.getToken());
        progress_bar  = view.findViewById(R.id.progress_bar);

        eventSummaryAdapter = new EventSummaryAdapter(getActivity(), events);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //next_events_list.setHasFixedSize(false);
        mGridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        next_events_list.setAdapter(eventSummaryAdapter);
        next_events_list.setLayoutManager(mGridLayoutManager);


        next_events_list.scrollToPosition(0);

        weekly_message_fetch();
        next_event_fetch();
      //  latest_video_fetch();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            welcome_note.setText(Html.fromHtml(getActivity().getString(R.string.welcome_message), Html.FROM_HTML_MODE_COMPACT));
        } else {
            welcome_note.setText(Html.fromHtml(getActivity().getString(R.string.welcome_message)));
        }


        return  view;
    }

    private void latest_video_fetch(){
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

                            try {
                                JSONArray data = response.getJSONObject().getJSONArray("data");
                                // Log.e("DATA", data.toString());

                                for(int i =0; i < data.length(); i++){
                                    if(!"151123722498786".equals(data.getJSONObject(i).getString("id")) &&
                                            "VOD".equals(data.getJSONObject(i).optString("live_status"))) {

                                        latest_video_object  = data.getJSONObject(i);
                                        Log.e("Vido", latest_video_object.toString());
                                        Glide.with(getActivity())
                                                .load(latest_video_object.optString("picture"))
                                                .into(latest_video);

                                        break;

                                    }
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Log.e("Videos",  .toString());


                        }
                    }
            ).executeAsync();

    }

    private void next_event_fetch() {
        new GraphRequest(
                accessToken ,
                "/"+ AppConfig.PAGEID+"/events?fields=cover,start_time,end_time,id&limit=4",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        //  progressDialog.dismiss();
                        try {
                            JSONArray data = response.getJSONObject().getJSONArray("data");

                          //  Log.e("data", data.toString());

                            Map<Date, JSONObject> ev = new HashMap<>();

                            for(int i =0; i < data.length(); i++){
                                // Log.e("TITLE", data.getJSONObject(i).optString("title"));

                                String start_time =  data.getJSONObject(i).optString("start_time");
                                String[] dates = start_time.split("T");


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date event_date = simpleDateFormat.parse(dates[0]);



                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new  Date());
                               // calendar.add(Calendar.MONTH, -2);


                                if(calendar.getTime().before(event_date)) {
                                    ev.put(event_date, data.getJSONObject(i));
                                }


                            }



                           // Date d = Collections.min(events.keySet());
                          //  next_event_object = events.get(d);

                          //  Glide.with(getActivity())
                                //   .load(next_event_object.getJSONObject("cover").optString("source"))
                             //       .into(next_event);
                            ArrayList<Date> keys = new ArrayList<Date>(ev.keySet());
                            Collections.reverse(keys);
                            Iterator<Date> iterator = ev.keySet().iterator();
                            for( Date d : keys){
                                eventSummaryAdapter.addEvent( ev.get(d));
                            }


                            scroll_animate();






                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Log.e("Videos",  .toString());


                    }
                }
        ).executeAsync();
    }

    private void scroll_animate() {

        final int max =  eventSummaryAdapter.getItemCount();
        final Handler handler = new Handler();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(++start > max){
                    start = 0;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //next_events_list.scrollToPosition(start);
                    }
                });


            }
        }, 1000,3000);

    }

    private void  weekly_message_fetch(){
        new GraphRequest(
                accessToken ,
                "/"+ AppConfig.PAGEID+"/videos?fields=icon,live_status,description,published,title,source,id,created_time,picture&limit=100000",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        //  progressDialog.dismiss();
                        try {
                            JSONArray data = response.getJSONObject().getJSONArray("data");
                            // Log.e("DATA", data.toString());

                            for(int i =0; i < data.length(); i++){
                                // Log.e("TITLE", data.getJSONObject(i).optString("title"));
                                if( data.getJSONObject(i).optString("title").contains("week plan") &&
                                        "".equals(data.getJSONObject(i).optString("live_status"))) {
                                    // videoListAdapter.addVideo(data.getJSONObject(i));
                                    weekly_message = data.getJSONObject(i);

                                    progress_bar.setVisibility(View.GONE);
                                    Glide.with(getActivity())
                                            .load(weekly_message.optString("picture"))
                                            .into(placeholder);
                                    play_button.setVisibility(View.VISIBLE);

                                    String date_time = weekly_message.optString("created_time");
                                    String[] dates = date_time.split("T");

                                    week_plan_created.setText("Posted on "+dates[0]);
                                    break;
                                }
                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Log.e("Videos",  .toString());


                    }
                }
        ).executeAsync();

    }
}
