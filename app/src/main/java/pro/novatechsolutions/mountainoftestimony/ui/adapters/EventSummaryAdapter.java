package pro.novatechsolutions.mountainoftestimony.ui.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pro.novatechsolutions.mountainoftestimony.R;
import pro.novatechsolutions.mountainoftestimony.ui.utils.TimerUtils;

public class EventSummaryAdapter  extends RecyclerView.Adapter<EventSummaryAdapter.EnventViewHolder>  {

    Context c;
    ArrayList<JSONObject> objects;

    public EventSummaryAdapter(Context c, ArrayList<JSONObject> objects ) {
        this.c = c;
        this.objects = objects;
        //  this.swiper = swiper;
        // this.parent = parent;

    }

    public void addEvent(JSONObject object){
        objects.add(object);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EnventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_row,viewGroup,false);
        EventSummaryAdapter.EnventViewHolder holder=new EventSummaryAdapter.EnventViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EnventViewHolder enventViewHolder, int i) {
        try {
            // Log.e("Image", objects.get(i).getString("picture"));
            Glide.with(c)
                    .load(objects.get(i).getJSONObject("cover").getString("source"))
                    .into(enventViewHolder.next_event_image);

            String start_date = objects.get(i).getString("start_time");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");
            Date event_start = simpleDateFormat.parse(start_date);

           // Log.e("DATE" ,event_start.toString());



            new CountDownTimer( event_start.getTime() - Calendar.getInstance().getTimeInMillis(), 1000) {

                public void onTick(long millisUntilFinished) {
                    enventViewHolder.countdown.setText("In " + TimerUtils.millisecondsToDays(millisUntilFinished));
                }

                public void onFinish() {
                    enventViewHolder.countdown.setText("");
                }
            }.start();


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class EnventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView next_event_image;
        public TextView countdown;
        //public TextView description;
        //  public TextView duration;

        public EnventViewHolder(@NonNull View itemView) {
            super(itemView);
            next_event_image =  itemView.findViewById(R.id.next_event_image);
            countdown  =  itemView.findViewById(R.id.count_down);
            //   description =  itemView.findViewById(R.id.description);
            //    duration =  itemView.findViewById(R.id.duration);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
