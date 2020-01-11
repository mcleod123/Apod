package com.example.apod;



import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;







public class PrepareHtml extends AppCompatActivity {





    public static String PrepareHtml(ArrayList<String> resultNasaValueSet, Context context) {

        // media_type
        String media_type = resultNasaValueSet.get(3);


        String resultHtml;
        resultHtml = null;


        // в зависимости от обрабатываемого контента, мы будем формировать нужную разметку
        // пока что делал только для картинок
        switch (media_type) {


            // с картинкой все просто
            case "image":
                resultHtml = "<html><body>\n" +
                        "   <p><img src=\"" +
                        resultNasaValueSet.get(0) +
                        "\"></p>\n" +
                        "<h1>" +
                        resultNasaValueSet.get(1) +
                        "</h1>" +
                        "<p>" +
                        resultNasaValueSet.get(2) +
                        "</p>\n" +
                        "</body></html>";
                break;

            // пока не знаем других типов
            default:


                String messagePart = context.getString(R.string.no_show_content_methods);
                resultHtml = "<h1>" + messagePart + "<h1>";
                break;

        }


        return resultHtml;

    }

}
