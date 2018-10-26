package pro.novatechsolutions.mountainoftestimony;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import pro.novatechsolutions.mountainoftestimony.ui.Events;
import pro.novatechsolutions.mountainoftestimony.ui.Home;
import pro.novatechsolutions.mountainoftestimony.ui.Videos;


public class MainActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = null;
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                  //  mTextMessage.setText(R.string.title_home);
                   // return false;
                    setTitle(getString(R.string.welcome));
                    fragment = new Home();
                    break;
                case R.id.navigation_videos:
                    fragment = new Videos();
                    setTitle(getString(R.string.title_videos));

                    //  mTextMessage.setText(R.string.title_home);
                    //return true;
                    break;
                case R.id.navigation_dashboard:
                  //  mTextMessage.setText(R.string.title_dashboard);
                    //return false;
                    break;
                case R.id.navigation_events:
                    //  mTextMessage.setText(R.string.title_dashboard);
                    //return false;
                    fragment = new Events();
                    setTitle(getString(R.string.title_events));
                    break;
                case R.id.navigation_notifications:
                  //  mTextMessage.setText(R.string.title_notifications);
                   // return false;
                    break;
            }
            if (bundle != null)
                fragment.setArguments(bundle);

            if(fragment != null ) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_home);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);*/

        /*PackageInfo packageInfo;
        String key = null;
       try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }*/


    }


}
