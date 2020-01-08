package com.example.apod;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity {

    private int clickToTextCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /* ---- клик 10 раз на текст------- */
        TextView textAbout = (TextView)findViewById(R.id.about_text_content);
        textAbout.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 clickToTextCounter++;

                 if (clickToTextCounter == 10) {

                                                 // скроем текст
                                                 TextView textViewAbout = findViewById(R.id.about_text_content);
                                                 textViewAbout.setVisibility(View.INVISIBLE);

                                                 // скроем заголовок
                                                 TextView textViewAbouttitle = findViewById(R.id.about_text_title);
                                                 textViewAbouttitle.setVisibility(View.INVISIBLE);

                                                 // отобразим кота
                                                 ImageView catView = findViewById(R.id.ic_cat);
                                                 catView.setVisibility(View.VISIBLE);

                                                 // сброс значения
                                                 clickToTextCounter = 0;
                                             }

                                         }



                                     });
        /* ------------------------------ */

    }


    /* нажатие клавиатцрной кнопки НАЗАД */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // переходим на главную активность
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /* ----- Создание меню ------------- */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }
    /* --------------------------------- */



    /*  Выбор пунктов выпадающего меню  */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int selected_menu_item_id = item.getItemId();

        switch (selected_menu_item_id) {

            // о программе
            case R.id.action_main:
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            // по умолчанию
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /* ------------------------------- */



}
