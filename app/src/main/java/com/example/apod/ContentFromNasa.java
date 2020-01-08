package com.example.apod;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;






public class ContentFromNasa {

    private static final String API_KEY = "CYcf43kdunmOhn5KnhZ5J9iUfpY7OlG12gqqZFhx";
    private static final String URI_SCHEME = "https://";
    private static final String URI_AUTHORITIES = "api.nasa.gov";
    private static final String URI_PATH = "/planetary/apod";
    public static final String FULL_URI = URI_SCHEME + URI_AUTHORITIES + URI_PATH + "?api_key=" + API_KEY;
    public static URL rest_api_uri;
    private static final Boolean CONTENT_IN_HD = false;

    // список значений
    public final ArrayList<String> resultNasaValueSet = new ArrayList<>();




    // https://api.nasa.gov/planetary/apod?api_key=CYcf43kdunmOhn5KnhZ5J9iUfpY7OlG12gqqZFhx


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


    public ArrayList<String> getNasaContent(Boolean view_in_hd) {


        // делаем два элемента

        if(resultNasaValueSet.size()==0) {
            resultNasaValueSet.add(0,""); // url
            resultNasaValueSet.add(1,""); // title
            resultNasaValueSet.add(2,""); // explanation
            resultNasaValueSet.add(3,""); // media_type
        }




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
                    } else {
                        // Error handling code goes here
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


                    // 1)
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

                        // 2)
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

                            // 3
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

                                // 4
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

                // не забывать закрыывать ридер
                try {
                    jsonReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        thread.start();





        if(!thread.isAlive()) {

            // gdfg

        }







        return resultNasaValueSet;



    }








}
