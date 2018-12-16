package com.example.saicm.weatherapp;

import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    EditText editText;
    TextView tvtime;
    TextView tvweather;
    ConstraintLayout layout;
    ImageView currentweatherimage;

    TextView tvtime2;
    ImageView imageView2;
    TextView tvtemp2;

    TextView tvtime3;
    ImageView imageView3;
    TextView tvtemp3;

    TextView tvtime4;
    ImageView imageView4;
    TextView tvtemp4;

    TextView tvtime5;
    ImageView imageView5;
    TextView tvtemp5;


    private static final String TAG = "MainActivity";

    String apikey = "a5e8f2d8bc0af9455d020b385e25fa40";
    String code = "94016";
//    TemperatureThread temperatureThread = new TemperatureThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tvtemp);
        editText = findViewById(R.id.editText);
        tvtime = findViewById(R.id.tvtime);
        tvweather = findViewById(R.id.tvweather);
        layout = findViewById(R.id.appbackground);
        currentweatherimage = findViewById(R.id.imageView);

        tvtime2 = findViewById(R.id.tvtime2);
        imageView2 = findViewById(R.id.imageView2);
        tvtemp2 = findViewById(R.id.tvtemp2);

        tvtime3 = findViewById(R.id.tvtime3);
        imageView3 = findViewById(R.id.imageView3);
        tvtemp3 = findViewById(R.id.tvtemp3);

        tvtime4 = findViewById(R.id.tvtime4);
        imageView4 = findViewById(R.id.imageView4);
        tvtemp4 = findViewById(R.id.tvtemp4);

        tvtime5 = findViewById(R.id.tvtime5);
        imageView5 = findViewById(R.id.imageView5);
        tvtemp5 = findViewById(R.id.tvtemp5);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = s.toString();
                if (s.length() == 5) {
                    editText.setVisibility(View.INVISIBLE);
                    editText.setClickable(false);
                    editText.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    new TemperatureThread().execute(code);
                } catch (Exception e) {

                }
            }
        });

    }

    public class TemperatureThread extends AsyncTask<String, Void, Void> {
        private static final String TAG = "TemperatureThread";
        String string = "";

        @Override
        protected Void doInBackground(String... strings) {

            try {
                String code = strings[0];
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?zip="+code+"&appid=a5e8f2d8bc0af9455d020b385e25fa40");
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                string = br.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                JSONObject jsonObject = new JSONObject(string);
                JSONArray fullforecast = jsonObject.getJSONArray("list");
                JSONObject currentday = fullforecast.getJSONObject(0);

                String weather = currentday.getJSONArray("weather").getJSONObject(0).getString("main");
                tvweather.setText(weather);

                String[] timelist = currentday.getString("dt_txt").split(" ");
                String time = convertTime(timelist[1]);
                switch (time) {
                    case "12 AM":
                        layout.setBackgroundResource(R.drawable.night);
                        break;
                    case "3 AM":
                        layout.setBackgroundResource(R.drawable.night);
                        break;
                    case "6 AM":
                        layout.setBackgroundResource(R.drawable.sunrise);
                        break;
                    case "9 AM":
                        layout.setBackgroundResource(R.drawable.earlymorning);
                        break;
                    case "12 PM":
                        layout.setBackgroundResource(R.drawable.morning);
                        break;
                    case "3 PM":
                        layout.setBackgroundResource(R.drawable.morning);
                        break;
                    case "6 PM":
                        layout.setBackgroundResource(R.drawable.sunset);
                        break;
                    case "9 PM":
                        layout.setBackgroundResource(R.drawable.earlynight);
                        break;
                }
                tvtime.setText(time);
                setTime(tvtime, currentday.getString("dt_txt").split(" "));

                setImage(currentweatherimage, weather);
                setTemperature(tv, currentday.getJSONObject("main").get("temp").toString());

                JSONObject time2 = fullforecast.getJSONObject(1);
                setTime(tvtime2, time2.getString("dt_txt").split(" "));
                setImage(imageView2, time2.getJSONArray("weather").getJSONObject(0).getString("main"));
                setTemperature(tvtemp2, time2.getJSONObject("main").get("temp").toString());

                JSONObject time3 = fullforecast.getJSONObject(2);
                setTime(tvtime3, time3.getString("dt_txt").split(" "));
                setImage(imageView3, time3.getJSONArray("weather").getJSONObject(0).getString("main"));
                setTemperature(tvtemp3, time3.getJSONObject("main").get("temp").toString());

                JSONObject time4 = fullforecast.getJSONObject(3);
                setTime(tvtime4, time4.getString("dt_txt").split(" "));
                setImage(imageView4, time4.getJSONArray("weather").getJSONObject(0).getString("main"));
                setTemperature(tvtemp4, time4.getJSONObject("main").get("temp").toString());

                JSONObject time5 = fullforecast.getJSONObject(4);
                setTime(tvtime5, time5.getString("dt_txt").split(" "));
                setImage(imageView5, time5.getJSONArray("weather").getJSONObject(0).getString("main"));
                setTemperature(tvtemp5, time5.getJSONObject("main").get("temp").toString());








            } catch (Exception e) {

            }
        }
    }

    public String convertTime(String string) {
        String[] timelist = string.split(":");
        String ap = "";
        if (Integer.parseInt(timelist[0]) < 12) {
            ap = "AM";
        } else {
            ap = "PM";
        }
        int timeint = (Integer.parseInt(timelist[0]) % 12);
        String time = Integer.toString(timeint);
        if (time.equals("0")) {
            time = "12";
        }
        return time + " " + ap;
    }

    public void setTime(TextView tv, String[] strings) {
        String time = convertTime(strings[1]);
        tv.setText(time);
    }

    public void setImage(ImageView image, String string) {
        switch (string) {
            case "Rain":
                image.setImageResource(R.drawable.rain);
                break;
            case "Thunderstorm":
                image.setImageResource(R.drawable.thunderstorm);
                break;
            case "Drizzle":
                image.setImageResource(R.drawable.drizzle);
                break;
            case "Snow":
                image.setImageResource(R.drawable.snow);
                break;
            case "Clear":
                image.setImageResource(R.drawable.clear);
                break;
            case "Clouds":
                image.setImageResource(R.drawable.clouds);
                break;
        }
    }

    public void setTemperature(TextView tv, String string) {
        double temperature = Double.parseDouble(string);
        double fahrenheit = (double)(Math.round((temperature-273)*1.8+32));
        tv.setText((int)fahrenheit+"°");
    }
}
