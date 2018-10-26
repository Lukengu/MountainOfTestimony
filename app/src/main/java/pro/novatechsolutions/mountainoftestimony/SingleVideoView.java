package pro.novatechsolutions.mountainoftestimony;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import pro.novatechsolutions.mountainoftestimony.ui.utils.MyVideoView;

public class SingleVideoView extends AppCompatActivity {

    MyVideoView videoView;
    ProgressDialog progressDialog;
    RelativeLayout parent;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
        videoView = findViewById(R.id.videoView);
        parent  = findViewById(R.id.parent);
        Bundle b =  getIntent().getExtras();
        setTitle(b.getString("description"));

       /* if(b.getString("description").length()  > 0) {
            final Snackbar snackbar = Snackbar.make(parent, b.getString("description"), Snackbar.LENGTH_INDEFINITE);
            View sbView = snackbar.getView();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sbView.setBackground(getDrawable(R.drawable.snackbar_error));
            } else {
                sbView.setBackground(getResources().getDrawable(R.drawable.snackbar_error));
            }

            snackbar.show();

            Timer timer = new  Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            snackbar.dismiss();
                        }
                    });

                }
            }, 12000, 1000);


        }*/

        videoView.setVideoURI(Uri.parse(b.getString("url")));
        progressDialog=  new ProgressDialog(this);

        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);




        progressDialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;







        videoView.setDimensions(width, height);
        videoView.getHolder().setFixedSize(width, height);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();

                mp.start();
            }
        });

    }
}
