package sg.edu.rp.c347.knowyourfacts;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> al;
    MyFragmentPagerAdapter adapter;
    ViewPager vPager;
    Button btnLater;
    int reqCode = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLater = (Button)findViewById(R. id. btnLater);
        vPager = (ViewPager)findViewById(R. id. viewpager1);

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefEdit = prefs.edit();
                int previousPage = vPager.getCurrentItem();
                prefEdit.putString("pageNo", String.valueOf(previousPage));
                Log.d("pageNo",String.valueOf(previousPage));
                prefEdit.commit();
            }
        });

        FragmentManager fm = getSupportFragmentManager();

        al = new ArrayList<Fragment>();
        al.add(new frag01());
        al.add(new frag02());
        al.add(new frag03());

        adapter = new MyFragmentPagerAdapter(fm, al);

        vPager.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);

        Intent intent = new Intent(MainActivity.this,
                MyReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, reqCode,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager)
                getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                pendingIntent);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pageNo = prefs.getString("pageNo","0");
        vPager.setCurrentItem(Integer.parseInt(pageNo), true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_previous) {
            if (vPager.getCurrentItem() > 0) {
                int previousPage = vPager.getCurrentItem() - 1;
                vPager.setCurrentItem(previousPage, true);
            }
        } else if(id == R.id.action_next){
            int max = vPager.getChildCount();
            if (vPager.getCurrentItem() < max - 1) {
                int nextPage = vPager.getCurrentItem() + 1;
                vPager.setCurrentItem(nextPage, true);
            } else{
                Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.action_random) {
            Random r = new Random();
            int random = r.nextInt(3 - 1) + 1;
//            int random = (int) Math.floor(Math.random());
            int nextPage = vPager.getCurrentItem() + 1;
            int previousPage = vPager.getCurrentItem() - 1;
            if (random % 2 == 1) {
                vPager.setCurrentItem(nextPage, true);
            } else if (random % 2 == 0){
                vPager.setCurrentItem(previousPage, true);
            }
        }
        return false;
    }


}
