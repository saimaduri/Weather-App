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
import android.widget.Toast;

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
    TextView quotes;
    TextView city;
    EditText editText;
    TextView tvtime;
    TextView tvweather;
    ConstraintLayout layout;
    ImageView currentweatherimage;

    TextView tvtime2;
    ImageView imageView2;
    TextView tvhigh2;
    TextView tvlow2;

    TextView tvtime3;
    ImageView imageView3;
    TextView tvhigh3;
    TextView tvlow3;

    TextView tvtime4;
    ImageView imageView4;
    TextView tvhigh4;
    TextView tvlow4;

    TextView tvtime5;
    ImageView imageView5;
    TextView tvhigh5;
    TextView tvlow5;

    private static final String TAG = "MainActivity";

    String apikey = "a5e8f2d8bc0af9455d020b385e25fa40";
    String code = "94016";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tvtemp);
        quotes = findViewById(R.id.quote);
        editText = findViewById(R.id.editText);
        tvtime = findViewById(R.id.tvtime);
        city = findViewById(R.id.city);
        tvweather = findViewById(R.id.tvweather);
        layout = findViewById(R.id.appbackground);
        currentweatherimage = findViewById(R.id.imageView);

        tvtime2 = findViewById(R.id.tvtime2);
        imageView2 = findViewById(R.id.imageView2);
        tvhigh2 = findViewById(R.id.tvhigh2);
        tvlow2 = findViewById(R.id.tvlow2);

        tvtime3 = findViewById(R.id.tvtime3);
        imageView3 = findViewById(R.id.imageView3);
        tvhigh3 = findViewById(R.id.tvhigh3);
        tvlow3 = findViewById(R.id.tvlow3);

        tvtime4 = findViewById(R.id.tvtime4);
        imageView4 = findViewById(R.id.imageView4);
        tvhigh4 = findViewById(R.id.tvhigh4);
        tvlow4 = findViewById(R.id.tvlow4);

        tvtime5 = findViewById(R.id.tvtime5);
        imageView5 = findViewById(R.id.imageView5);
        tvhigh5 = findViewById(R.id.tvhigh5);
        tvlow5 = findViewById(R.id.tvlow5);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = s.toString();
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
                while (string == "\"cod\":\"400\",\"message\":\"invalid zip code" || string.equals("")){
                    String code = strings[0];
                    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?zip=" + code + "&appid=a5e8f2d8bc0af9455d020b385e25fa40");
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    string = br.readLine();
                }

                editText.setVisibility(View.INVISIBLE);
                editText.setClickable(false);
                editText.setEnabled(false);

            } catch (Exception e) {
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

                city.setText(jsonObject.getJSONObject("city").getString("name"));

                String weather = currentday.getJSONArray("weather").getJSONObject(0).getString("main");
                tvweather.setText(weather);
                setQuote(weather);

                String[] timelist = currentday.getString("dt_txt").split(" ");
                String time = convertTime(timelist[1]);
                switch (time) {
                    case "1 AM":
                        layout.setBackgroundResource(R.drawable.night);
                        break;
                    case "4 AM":
                        layout.setBackgroundResource(R.drawable.night);
                        break;
                    case "7 AM":
                        layout.setBackgroundResource(R.drawable.sunrise);
                        break;
                    case "10 AM":
                        layout.setBackgroundResource(R.drawable.earlymorning);
                        break;
                    case "1 PM":
                        layout.setBackgroundResource(R.drawable.morning);
                        break;
                    case "4 PM":
                        layout.setBackgroundResource(R.drawable.morning);
                        break;
                    case "7 PM":
                        layout.setBackgroundResource(R.drawable.sunset);
                        break;
                    case "10 PM":
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
                setHighLow(tvhigh2, tvlow2, time2.getJSONObject("main").get("temp_max").toString(), time2.getJSONObject("main").get("temp_min").toString());

                JSONObject time3 = fullforecast.getJSONObject(2);
                setTime(tvtime3, time3.getString("dt_txt").split(" "));
                setImage(imageView3, time3.getJSONArray("weather").getJSONObject(0).getString("main"));
                setHighLow(tvhigh3, tvlow3, time3.getJSONObject("main").get("temp_max").toString(), time3.getJSONObject("main").get("temp_min").toString());

                JSONObject time4 = fullforecast.getJSONObject(3);
                setTime(tvtime4, time4.getString("dt_txt").split(" "));
                setImage(imageView4, time4.getJSONArray("weather").getJSONObject(0).getString("main"));
                setHighLow(tvhigh4, tvlow4, time4.getJSONObject("main").get("temp_max").toString(), time4.getJSONObject("main").get("temp_min").toString());

                JSONObject time5 = fullforecast.getJSONObject(4);
                setTime(tvtime5, time5.getString("dt_txt").split(" "));
                setImage(imageView5, time5.getJSONArray("weather").getJSONObject(0).getString("main"));
                setHighLow(tvhigh5, tvlow5, time5.getJSONObject("main").get("temp_max").toString(), time5.getJSONObject("main").get("temp_min").toString());

            } catch (Exception e) {

            }
        }
    }

    public String convertTime(String string) {
        String[] timelist = string.split(":");
        String ap = "";
        int timeint = Integer.parseInt(timelist[0]);
        timeint-=5;

        if (timeint < 12 && timeint > 0) {
            ap = "AM";
        } else {
            ap = "PM";
        }
        timeint = timeint%12;
        if (timeint < 0) {
            timeint+=12;
        }
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

    public int convertTemperature(String string) {
        double temperature = Double.parseDouble(string);
        double fahrenheit = (double)(Math.round((temperature-273)*1.8+32));
        return (int)fahrenheit;
    }

    public void setHighLow(TextView tvhigh, TextView tvlow, String high, String low) {
        tvhigh.setText("H: " + convertTemperature(high));
        tvlow.setText("L: " + convertTemperature(low));
    }

    public void setQuote(String string) {
        switch (string) {
            case "Rain":
                if ((int) (Math.random() * 2) == 0) {
                    quotes.setText("\"Rain is a major component of the water cycle and is responsible for depositing most of the fresh water on the Earth!\"");
                } else {
                    quotes.setText("\"Rain is liquid water in the form of droplets!\"");
                }
                break;
            case "Thunderstorm":
                if ((int) (Math.random() * 2) == 0) {
                    quotes.setText("\"A thunderstorm is also known as an electrical storm, lightning storm, or thundershower.\"");
                } else {
                    quotes.setText("\"Thunderstorms occur in a type of cloud known as a cumulonimbus\"");
                }
            case "Drizzle":
                if ((int) (Math.random() * 2) == 0) {
                    quotes.setText("\"Drizzle is a light liquid precipitation consisting of liquid water drops.\"");
                } else {
                    quotes.setText("\"Drizzle drops are smaller than rain drops!\"");
                }
                break;
            case "Snow":
                if ((int) (Math.random() * 2) == 0) {
                    quotes.setText("\"Snow refers to forms of ice crystals that precipitate from the atmosphere.\"");
                } else {
                    quotes.setText("\"Snowstorms organize and develop by feeding on sources of atmospheric moisture and cold air!\"");
                }
                break;
            case "Clear":
                quotes.setText("\"Clear means there are no clouds in the sky!\"");
                break;
            case "Clouds":
                quotes.setText("\"Cloudy means the entire sky is covered by clouds!\"");
                break;
        }
    }
}