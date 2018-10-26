package pro.novatechsolutions.mountainoftestimony.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pro.novatechsolutions.mountainoftestimony.AppConfig;
import pro.novatechsolutions.mountainoftestimony.R;
import pro.novatechsolutions.mountainoftestimony.SingleVideoView;


public class VideoListAdapter  extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>  {


    Context c;
    ArrayList<JSONObject> objects;



   // SwipeRefreshLayout swiper;



    public VideoListAdapter(Context c, ArrayList<JSONObject> objects ) {
        this.c = c;
        this.objects = objects;
      //  this.swiper = swiper;
       // this.parent = parent;

    }

    public void addVideo(JSONObject object){
        objects.add(object);
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,  int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_items,viewGroup,false);
        VideoListAdapter.VideoViewHolder holder=new VideoListAdapter.VideoViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, final int i) {

       // videoViewHolder.description.setText(objects.get(i).optString("description"));
        try {
           // Log.e("Image", objects.get(i).getString("picture"));
            Glide.with(c)
                    .load(objects.get(i).getString("picture"))
                    .into(videoViewHolder.thumbnail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        videoViewHolder.play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", objects.get(i).optString("source"));
                bundle.putString("description", objects.get(i).optString("description"));

                c.startActivity(new Intent(c, SingleVideoView.class).putExtras(bundle));
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public JSONObject getItem(int i){
        return objects.get(i);
    }
    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView thumbnail, play_button;
        //public TextView description;
      //  public TextView duration;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail =  itemView.findViewById(R.id.thumbnail);
            play_button  =  itemView.findViewById(R.id.play_button);
         //   description =  itemView.findViewById(R.id.description);
        //    duration =  itemView.findViewById(R.id.duration);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
