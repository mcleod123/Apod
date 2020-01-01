package com.example.apod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ContentFromNasa contentFromNasa;
    ArrayList<String> resultNasaValueSet = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentFromNasa = new ContentFromNasa();
        contentFromNasa.getNasaContent(false);

        // показать сообщение, чтобы кликнули по картинке
        ShowToast(getString(R.string.click_to_new_content));


    }

    @Override
    protected void onResume() {
        super.onResume();

        // скроем вью с контентом
        WebView webView = findViewById(R.id.web_view_main);
        webView.setVisibility(View.INVISIBLE);

        // сначала покажем стартовую картинку
        ImageView startImageView = findViewById(R.id.start_image_on_screen);
        startImageView.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }






    /* Отслеживаем клики по элементам на активити */
    public void OnClick(View view) {

        switch(view.getId()){

            // при клике на экране со стартовой картинкой - покажем картинку на сегодня
            case R.id.start_image_on_screen:
                // показать контент по умолчанию на сегодня
                ShowContentOnWebView();
                break;
                default:
                    // do nothing
                    break;
        }
    }
    /* ------------------------------------------ */





    /*  Выбор пунктов выпадающего меню  */
   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // какой пункт меню нажали
        int selected_menu_item_id = item.getItemId();

        switch (selected_menu_item_id) {

            // о программе
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            // получить контент на сегодня
            case R.id.action_get_today_content:

                // показать контент по умолчанию на сегодня
                ShowContentOnWebView();

                return true;
            // по умолчанию
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /* ------------------------------- */




    public String PrepareHtmlPage(ArrayList<String> resultNasaValueSet) {

        // media_type
        String media_type = resultNasaValueSet.get(3);

        String resultHtml;
        resultHtml = null;


        // в зависимости от обрабатываемого контента, мы будем формировать нужную разметку
        switch (media_type) {


            // с картинкой все просто
            case "image":
                resultHtml = "<style>\n" +
                        "   .sign {\n" +
                        "    float: center;\n" +
                        "    \n" +
                        "    padding: 7px; /* Поля внутри блока */\n" +
                        "    margin: 10px 0; /* Отступы вокруг */\n" +
                        "    \n" +
                        "   }\n" +
                        "   .sign figcaption {\n" +
                        "    margin: 0 auto 5px; /* Отступы вокруг абзаца */\n" +
                        "   }\n" +
                        "  </style>\n" +
                        " <body>\n" +
                        "  <figure class=\"sign\">\n" +
                        "   <p><img src=\"" +
                        resultNasaValueSet.get(0) +
                        "\"></p>\n" +
                        "<h1>" +
                        resultNasaValueSet.get(1) +
                        "</h1>" +
                        "   <figcaption>" +
                        resultNasaValueSet.get(2) +
                        "</figcaption>\n" +
                        "  </figure>\n" +
                        "</body>";
                break;

            // пока не знаем других типов
            default:
                resultHtml = "<h1>" + getString(R.string.no_show_content_methods) + "<h1>";
                break;


        }

        return resultHtml;

    }





    /* показать контент по умолчанию на сегодня */
    public void ShowContentOnWebView() {

        // contentFromNasa.getNasaContent(false);

        // получили значение сегодняшнего контента
        resultNasaValueSet = contentFromNasa.getNasaContent(false);

        // ShowToast(resultNasaValueSet.get(0));
        // ShowToast(resultNasaValueSet.get(1));

        // сначала скроем стартовую картинку
        ImageView startImageView = findViewById(R.id.start_image_on_screen);
        startImageView.setVisibility(View.INVISIBLE);

        // покажем вью, на котором отобразим контент
        WebView webView = findViewById(R.id.web_view_main);
        webView.setVisibility(View.VISIBLE);

        // до того, как показать вью - настроим ее
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        // загрузим текущий контент
        // webView.loadUrl(resultNasaValueSet.get(0));

        // загрузим текущий контент в HTML
        /*
        webView.loadData(
                PrepareHtmlPage(resultNasaValueSet),
                "text/html",
                "UTF-8"

        );
        */

        webView.loadData(
                PrepareHtml.PrepareHtml(resultNasaValueSet),
                "text/html",
                "UTF-8"

        );



    }
    /* -----------------------------------------   */





    /* Показ сообщений Toast */
    public void ShowToast(String message_string) {
        Toast toast_message = Toast.makeText(
                getApplicationContext(),
                message_string,
                Toast.LENGTH_LONG
        );
        toast_message.show();
    }
    /* ---------------------- */

}
