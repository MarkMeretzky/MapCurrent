package com.example.nyuscps.rocket;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    TextView fuelView;
    TextView buildView;
    Handler buttonHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fuelView = (TextView)findViewById(R.id.fuel);
        buildView = (TextView)findViewById(R.id.build);

        ButtonRunnable buttonRunnable = new ButtonRunnable();
        Thread thread = new Thread(buttonRunnable);
        thread.start();

        Button button = (Button)findViewById(R.id.launch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RocketView rocketView = (RocketView)findViewById(R.id.rocketView);
                rocketView.launchTheRocket();
            }
        });
    }

    private class ButtonRunnable implements Runnable {
        //This method executed by second thread.
        @Override
        public void run() {
            for (int i = 0; i <= 100; ++i) {
                //Display the current value of i in the build TextView.
                final int j = i;
                buttonHandler.post(new Runnable() {
                    //This method executed by the UI thread.
                    @Override
                    public void run() {
                        buildView.setText(Integer.toString(j));
                    }
                });

                //Sleep for 1/60 of a second.
                try {
                    Thread.sleep(100L);   //milliseconds
                } catch (InterruptedException interruptedException) {
                }
            }

            //Enable the launch button.
            buttonHandler.post(new Runnable() {
                //This method executed by the UI thread.
                @Override
                public void run() {
                    Button button = (Button)findViewById(R.id.launch);
                    button.setEnabled(true);
                    button.setBackgroundColor(Color.BLUE);
                }
            });

            for (int i = 0; i <= 100; ++i) {
                //Display the current value of i in the fuel TextView.
                final int j = i;
                buttonHandler.post(new Runnable() {
                    //This method executed by the UI thread.
                    @Override
                    public void run() {
                        fuelView.setText(Integer.toString(j));
                    }
                });

                //Sleep for 1/60 of a second.
                try {
                    Thread.sleep(100L);   //milliseconds
                } catch (InterruptedException interruptedException) {
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


