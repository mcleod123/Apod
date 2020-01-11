package com.example.apod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import javax.crypto.ShortBufferException;


public class MainActivity extends AppCompatActivity {

    ContentFromNasa contentFromNasa;
    ArrayList<String> resultNasaValueSet = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentFromNasa = new ContentFromNasa();
        // получили значение сегодняшнего контента
        Boolean show_in_hd = false;
        resultNasaValueSet = contentFromNasa.getNasaContent(
                show_in_hd,
                getIntent(),
                this);


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


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        openQuitDialog();
    }


    public void onDestroy() {
        moveTaskToBack(true);

        super.onDestroy();

        System.runFinalizersOnExit(true);
        System.exit(0);
    }






    /* Отслеживаем клики по элементам на активити */
    public void OnClick(View view) {

        switch(view.getId()){

            // при клике на экране со стартовой картинкой - покажем картинку на сегодня
            case R.id.start_image_on_screen:
                // показать контент по умолчанию на сегодня
                ShowContentOnWebView(false);
                break;

            // при клике на картинку ошибки
            case R.id.start_error_image_on_screen:
                // показать контент по умолчанию на сегодня
                ShowContentOnWebView(false);
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
                ShowContentOnWebView(false);
                return true;

            // получить контент на сегодня HD
            case R.id.action_get_today_content_hd:

                // показать контент по умолчанию на сегодня HD
                ShowContentOnWebView(true);
                return true;


            // выход из приложения
            case R.id.action_exit_app:
                openQuitDialog();
                return true;


            // по умолчанию
            default:
                // return super.onOptionsItemSelected(item);
                return false;
        }

    }
    /* ------------------------------- */



    /* Соберем HTML код, который потом вставим в веб вью */
    public String PrepareHtmlPage(ArrayList<String> resultNasaValueSet, boolean show_in_hd) {

        // media_type
        String media_type = resultNasaValueSet.get(3);

        String image_url = null;
        if(show_in_hd == false) {

            // обычное изображение
            image_url = resultNasaValueSet.get(0);
        } else {
            // качественное изобравжение в HD
            image_url = resultNasaValueSet.get(4);
        }

        ShowToast(image_url);

        // в зависимости от обрабатываемого контента, мы будем формировать нужную разметку
        String resultHtml = null;
        switch (media_type) {




            // с картинкой все просто
            case "image":

//                resultHtml = "<style>\n" +
//                        " .sign { float: center; margin: 10px 0; }\n" +
//                        " .sign figcaption { margin: 0; color:#fff; padding:10px 20px; text-align: justify; }\n" +
//                        " body, html { background:#000; padding: 0; margin: 0; color: #fff;} " +
//                        " h1 {color:#ff0; padding:10px; text-align:center;}  " +
//                        "  img {width: 100%;} "+
//                        "  </style>\n" +
//                        " <body>\n" +
//                        "  <figure class=\"sign\">\n" +
//                        "<h1>" + resultNasaValueSet.get(1) + "</h1>" +
//                        "<p><img src=\"" + image_url + "\"></p>\n" +
//                        "<figcaption>" + resultNasaValueSet.get(2) + "</figcaption>\n" +
//                        "  </figure>\n" +
//                        "</body>";

                resultHtml = "<!doctype html><html><head></head><title></title><body><h1><font color='white'>" + resultNasaValueSet.get(1) + "</font></h1>";
                resultHtml += "<img src=\"" + image_url + "\" />";
                resultHtml += "<p><font color='white'>" + resultNasaValueSet.get(2) + "</font></p></body></html>";


                break;

            // пока не знаем других типов
            default:
                // resultHtml = "<h1>" + getString(R.string.no_show_content_methods) + "<h1>";
                // resultHtml = "";

                // ShowErrorImage1();
                resultHtml = null;
                break;


        }

        return resultHtml;

    }
    /* ------------------------------------------------- */



    /* показать контент по умолчанию на сегодня */
    @SuppressLint("ResourceAsColor")
    public void ShowContentOnWebView(boolean show_in_hd) {

        // получили значение сегодняшнего контента
        resultNasaValueSet = contentFromNasa.getNasaContent(
                                                            show_in_hd,
                                                            getIntent(),
                                                            this);



        // проверочка, а точно ли у нас есть Интернет
       if(resultNasaValueSet.get(0).equals(ContentFromNasa.IS_NO_INTERNET_CONNECTION_FLAG)) {
            ShowErrorImage1();

       // а вот если с интернетом все норм
       // то живем нормальной человеческой жизнью
       } else {

           // сначала скроем стартовую картинку
           ImageView startImageView = findViewById(R.id.start_image_on_screen);
           startImageView.setVisibility(View.INVISIBLE);


           View mainElement = findViewById(R.id.main_element);
           // mainElement.setBackgroundColor(0xFFFCFFFC);

           // покажем вью, на котором отобразим контент
           WebView webView = findViewById(R.id.web_view_main);


           // до того, как показать вью - настроим ее
           webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
           webView.getSettings().setBuiltInZoomControls(true);
           webView.getSettings().setUseWideViewPort(true);
           webView.getSettings().setLoadWithOverviewMode(true);
           webView.getSettings().setMinimumFontSize(32);
           webView.getSettings().setAllowContentAccess(true);
           // webView.setBackgroundColor(R.color.absoluteWhite);
           webView.setBackgroundColor(R.color.absoluteDark);

           // загрузим текущий контент в HTML
           webView.loadData(
                   PrepareHtmlPage(resultNasaValueSet, show_in_hd),
                   "text/html",
                   "UTF-8"
           );

           webView.setVisibility(View.VISIBLE);

       }

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



    /* Если нет интернета то паказываем картинку ошибки */
    public void ShowErrorImage1() {

        // View mainElement = findViewById(R.id.main_element);
        // mainElement.setBackgroundColor(0xFFFFFFFF);


        // скроем стартовую картинку, если она отображается
        ImageView startImageView1 = findViewById(R.id.start_image_on_screen);
        if (startImageView1.getVisibility() == View.VISIBLE) {
            startImageView1.setVisibility(View.INVISIBLE);
        }

        // скроем активную веб вью, если она сейчас отображена и на ней уже был контент
        WebView webViewMain = findViewById(R.id.web_view_main);
        if (webViewMain.getVisibility() == View.VISIBLE) {
            webViewMain.setVisibility(View.INVISIBLE);
        }

        // покажем вью ошибки
        ImageView ImageError1 = findViewById(R.id.start_error_image_on_screen);
        ImageError1.setVisibility(View.VISIBLE);

        // на экране покажем тост о том, что соединения возможно нет и хватит тыкать
        ShowToast(getString(R.string.show_error_message));

    }
    /* ------------------------------------------------ */


    /* диалог выхода из приложения  */
    private void openQuitDialog() {


        // диалог выхода
        final AlertDialog.Builder exitDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        exitDialogBuilder.setMessage(R.string.dialog_are_you_exit_program)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_exit_program_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moveTaskToBack(true);
                        MainActivity.super.onDestroy();
                        System.runFinalizersOnExit(true);
                        System.exit(0);

                       MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.dialog_exit_program_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = exitDialogBuilder.create();
        alert.setTitle(R.string.dialog_are_you_exit_program);
        alert.show();

    }
    /* ------------------------------------------------ */

}
