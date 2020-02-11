package com.example.apod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;


public class ContentFromNasa {

    // для хранения данных
    private SharedPreferences preferences;
    public static final String APP_PREFERENCES_NAME = "apod_data";
    public static final String DATA_DELIMETER = "[-]";
    // ====================


    private static final String API_KEY = "CYcf43kdunmOhn5KnhZ5J9iUfpY7OlG12gqqZFhx";
    private static final String URI_SCHEME = "https://";
    private static final String URI_AUTHORITIES = "api.nasa.gov";
    private static final String URI_PATH = "/planetary/apod";
    public static final String FULL_URI = URI_SCHEME + URI_AUTHORITIES + URI_PATH + "?api_key=" + API_KEY;
    public static URL rest_api_uri;
    // private static final Boolean CONTENT_IN_HD = false;

    // список значений, которые будут получены из REST API
    public final ArrayList<String> resultNasaValueSet = new ArrayList<>();


    // для проверки интернет соединения
    NetworkChangeReceiver networkStatusValue = new NetworkChangeReceiver();
    public static final String IS_NO_INTERNET_CONNECTION_FLAG = "no_internet";


    // для проверочки
    // https://api.nasa.gov/planetary/apod?api_key=CYcf43kdunmOhn5KnhZ5J9iUfpY7OlG12gqqZFhx


    // конструктор
    ContentFromNasa () {
        setRestURLfromString(FULL_URI);
    }


    public static URL getRestApiURL() {
        return rest_api_uri;
    }


    public static void setRestURLfromString(String restUrlString) {

        URL restURL = null;

        // первая попытка
        try {
            restURL = new URL(restUrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        rest_api_uri = restURL;

    }


    public ArrayList<String> getNasaContent(Boolean view_in_hd, Intent intentAPP, Context contextAPP) {

        // инициализируем локальное хранилище с данными
        setMySharedPrefs(contextAPP);
        // =============================


        // делаем два элемента

        if(resultNasaValueSet.size()==0) {
            resultNasaValueSet.add(0,""); // url
            resultNasaValueSet.add(1,""); // title
            resultNasaValueSet.add(2,""); // explanation
            resultNasaValueSet.add(3,""); // media_type
            resultNasaValueSet.add(4,""); // hd_url
        }

        Log.d("test_test", "Начали проверку тырнета");

        // если интернет не подключен, то никаких потоков создавать не будем и вернем пустое значение
        String currentInternet = networkStatusValue.IsInternetActive(contextAPP, intentAPP);

        Log.d("test_test", "Вот, что получили: " + currentInternet);

        if(currentInternet.equals(NetworkChangeReceiver.NO_INTERNET)) {

            Log.d("test_test", "Тырнет у нас такой: " + currentInternet);

            // понятно, какое значение придет, чтобы его отловить
            resultNasaValueSet.set(0, IS_NO_INTERNET_CONNECTION_FLAG);
            // return resultNasaValueSet;












        // а вот если с интернетом все норм, то запускаем поток
        // и живем нормальной человеческой жизнью
        }  else {




            Log.d("test_test", "Данные из локальной хранилки на этапе выполнения программы: " + GetPrefsData(GetCurDate(), contextAPP).get(0));

            // проверим, есть ли у нас локальные данные
            if( GetPrefsData(GetCurDate(), contextAPP) != null) {

                Log.d("test_test", "грузим локальные данные, так как они есть в shared greferences");

                // если есть - грузимся из них и возвращаем значение
                return GetPrefsData(GetCurDate(), contextAPP);
            }
            // =======================================






            Log.d("test_test", "Тырнет у нас такой: " + currentInternet);

            Thread thread = new Thread(new Runnable() {
                @Override public void run() {

                    setRestURLfromString(FULL_URI);


                    // Create connection
                    HttpsURLConnection myConnection = null;
                    try {
                        myConnection = (HttpsURLConnection) getRestApiURL().openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (myConnection != null) {
                        myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                    }

                    try {
                        if (myConnection.getResponseCode() == 200) {
                            // Success
                            // result_uri_1 = "Connection OTKRYT";
                            // result_uri = "https://apod.nasa.gov/apod/image/1912/M20_volskiy1024.jpg";
                        } else {
                            // Error handling code goes here
                            // result_uri = "https://google.com/";
                            // result_uri_1 = "NARKOMAN!";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // разбор ответа от сервера
                    InputStream responseBody = null;
                    try {
                        responseBody = myConnection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    InputStreamReader responseBodyReader =
                            null;
                    try {
                        responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }



                    JsonReader jsonReader = new JsonReader(responseBodyReader);



                    // извлечь инфу
                    try {
                        jsonReader.beginObject(); // Start processing the JSON object
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    while (true) {

                        try {
                            if (!jsonReader.hasNext()) break;
                        } catch (IOException e) {  e.printStackTrace(); }

                        String key = null; // Fetch the next key

                        try {
                            key = jsonReader.nextName();
                        } catch (IOException e) {  e.printStackTrace(); }


                        // 0)
                        // result_url
                        if (key.equals("url")) {

                            String value = null;

                            try {
                                value = jsonReader.nextString();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            resultNasaValueSet.set(0, value);
                            Log.d("test_test","Set value URL: " + value);
                            continue;

                        } else {

                            // 1)
                            // result title
                            if (key.equals("title")) {

                                String value = null;

                                try {
                                    value = jsonReader.nextString();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                resultNasaValueSet.set(1, value);
                                Log.d("test_test","Set value TITLE: " + value);
                                continue;

                            } else {

                                // 2
                                // result explanation
                                if(key.equals("explanation")) {

                                    String value = null;

                                    try {
                                        value = jsonReader.nextString();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    resultNasaValueSet.set(2, value);
                                    Log.d("test_test","Set value explanation: " + value);
                                    continue;


                                } else {

                                    // 3
                                    // media_type
                                    if(key.equals("media_type")) {

                                        String value = null;

                                        try {
                                            value = jsonReader.nextString();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        resultNasaValueSet.set(3, value);
                                        Log.d("test_test","Set value media_type: " + value);
                                        continue;


                                    } else

                                        // 4
                                        // hdurl
                                        if(key.equals("hdurl")) {

                                            String value = null;

                                            try {
                                                value = jsonReader.nextString();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            resultNasaValueSet.set(4, value);
                                            Log.d("test_test","Set value hdurl: " + value);
                                            continue;


                                        } else {

                                            try {
                                                jsonReader.skipValue();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                }




                            }






                        }
                    }

                    // не забывать закрывать ридер
                    try {
                        jsonReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            thread.start();




        }



        PutPrefData(
                resultNasaValueSet.get(1),
                resultNasaValueSet.get(0),
                resultNasaValueSet.get(4),
                resultNasaValueSet.get(3),
                resultNasaValueSet.get(2)
        );


        /*
            resultNasaValueSet.add(0,""); // url
            resultNasaValueSet.add(1,""); // title
            resultNasaValueSet.add(2,""); // explanation
            resultNasaValueSet.add(3,""); // media_type
            resultNasaValueSet.add(4,""); // hd_url
        */



        return resultNasaValueSet;



    }




    // получаем наше хранилище с настройками
    public void setMySharedPrefs(Context context_app) {

        preferences = context_app.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE);

    }
    // ============================






    // пишем в наше хранилище с настройками
    public void PutPrefData(
                        String title,
                        String url,
                        String hd_url,
                        String media_type,
                        String explanation
    ) {



        String date = GetCurDate();


        String data_string = url + DATA_DELIMETER + title + DATA_DELIMETER + explanation + DATA_DELIMETER + media_type + DATA_DELIMETER + hd_url;

        // если есть что сохранять, то сохраняем
        if(!url.isEmpty()) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(date, data_string );
            editor.apply();

        }



    }
    // ====================================




    // получаем данные по дате - ключу
    public ArrayList<String> GetPrefsData(String date, Context context_app) {

        ArrayList<String> result_arr_list = null;

        if (preferences == null) {
            setMySharedPrefs(context_app);
        }

        String res_string = preferences.getString(date, null);

        Log.d("test_test", "Данные в хранилке - " + res_string);

        String[] sub_string = res_string.split(DATA_DELIMETER);

        for(int i = 0; i < sub_string.length; i++) {
            result_arr_list.add(0, sub_string[i]);

        }

        return result_arr_list;
    }
    // ====================================






    // Общая функция для получения времени в нужном нам формате
    public String GetCurDate() {



        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        String date = sdf.format(new Date());


        Log.d("test_test", "Текущая дата - " + date);

        return date;

    }
    // ==============================

}
