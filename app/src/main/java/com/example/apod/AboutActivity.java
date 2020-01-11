package com.example.apod;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }


    /*  Выбор пунктов выпадающего меню  */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int selected_menu_item_id = item.getItemId();

        switch (selected_menu_item_id) {

            // о программе
            case R.id.action_main:
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            // по умолчанию
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /* ------------------------------- */





}
