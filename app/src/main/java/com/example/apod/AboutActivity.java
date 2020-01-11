package com.example.apod;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity {

    // счетчик кликов на текст О програме
    public int clickCounter = 0;

    // Анимации
    private Animation animOne, animTwo, animThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Подгружаем все анимации
        animOne = AnimationUtils.loadAnimation(this, R.anim.animation_one);
        animTwo = AnimationUtils.loadAnimation(this, R.anim.animation_two);
        animThree = AnimationUtils.loadAnimation(this, R.anim.animation_three);
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
                finish();
                return true;
            // по умолчанию
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /* ------------------------------- */


    /*  отслеживаем клики по контенту на странице "О программе"  */
    public void OnClick(View view) {

        switch(view.getId()){

            // при клике на текст "О программе" будем изменять счетчик кликов и на третий клик покажем кота
            case R.id.about_text_content:

                clickCounter++;

                // считаем и показываем котика
                if(clickCounter == 3) {
                    ShowCat();
                }
                break;

            // при клике на картинку кота, скроем его и вернем как было
            case R.id.about_cat_image:
                ImageView catView = findViewById(R.id.about_cat_image);
                AnimateCat2(catView);
                break;

            default:
                // do nothing
                break;


        }
    }
    /* ------------------------------- */



    /* показываем котика */
    public void ShowCat() {

        TextView aboutTextTitle = findViewById(R.id.about_text_title);
        TextView aboutTextContent = findViewById(R.id.about_text_content);
        ImageView aboutSatImage = findViewById(R.id.about_sattelite_image);
        ImageView aboutCatImage = findViewById(R.id.about_cat_image);

        // скроем все лишнее и покажем главное - кота
        aboutTextTitle.setVisibility(View.INVISIBLE);
        aboutTextContent.setVisibility(View.INVISIBLE);
        aboutSatImage.setVisibility(View.INVISIBLE);
        aboutCatImage.setVisibility(View.VISIBLE);

        AnimateCat(aboutCatImage);
        // сброс значения
        clickCounter=0;
    }
    /* ------------------------------- */


    /* скрываем котика */
    public void HideCat() {

        TextView aboutTextTitle = findViewById(R.id.about_text_title);
        TextView aboutTextContent = findViewById(R.id.about_text_content);
        ImageView aboutSatImage = findViewById(R.id.about_sattelite_image);
        ImageView aboutCatImage = findViewById(R.id.about_cat_image);

        // скроем кота и покажем все лишнее
        aboutTextTitle.setVisibility(View.VISIBLE);
        aboutTextContent.setVisibility(View.VISIBLE);
        aboutSatImage.setVisibility(View.VISIBLE);
        aboutCatImage.setVisibility(View.INVISIBLE);

    }
    /* ------------------------------- */


    /* котоанимация */
    public void AnimateCat(ImageView imageView) {
        imageView.startAnimation(animThree);
    }
    /* ------------------------------ */

    /* котоанимация 2 */
    public void AnimateCat2(ImageView imageView) {
        imageView.startAnimation(animTwo);
        HideCat();
    }
    /* ------------------------------ */


}
