package com.trydev.games.guesswhat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.trydev.games.guesswhat.Quest.Quest;
import com.trydev.games.guesswhat.Service.MediaService;

import java.util.ArrayList;

import static com.trydev.games.guesswhat.MainActivity.seek;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton fruit, animal;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        fruit = (ImageButton) findViewById(R.id.fruit);
        animal = (ImageButton) findViewById(R.id.animal);

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        start();

        fruit.setOnClickListener(this);
        animal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fruit:
                createFruitQuest();
                break;
            case R.id.animal:
                createAnimalQuest();
                break;
        }
        Intent intent;
        intent = new Intent(getApplicationContext(), GameActivity.class);
        mediaPlayer.pause();
        seek = mediaPlayer.getCurrentPosition();
        CategoryActivity.this.finish();
        startActivity(intent);
    }

    private void createAnimalQuest() {
        ArrayList<Quest> animalQuest = new ArrayList<>();
        animalQuest.add(new Quest(R.drawable.bear, "bear"));
        animalQuest.add(new Quest(R.drawable.cow, "cow"));
        animalQuest.add(new Quest(R.drawable.crocodile, "crocodile"));
        animalQuest.add(new Quest(R.drawable.deer, "deer"));
        animalQuest.add(new Quest(R.drawable.donkey, "donkey"));
        animalQuest.add(new Quest(R.drawable.koala, "koala"));
        animalQuest.add(new Quest(R.drawable.panda, "panda"));
        GameActivity.myQuest = animalQuest;
    }

    private void createFruitQuest() {
        ArrayList<Quest> fruitQuest = new ArrayList<>();
        fruitQuest.add(new Quest(R.drawable.apple_correct, "apple"));
        fruitQuest.add(new Quest(R.drawable.banana_correct, "banana"));
        fruitQuest.add(new Quest(R.drawable.cherry_correct, "cherry"));
        fruitQuest.add(new Quest(R.drawable.eggplant_correct, "eggplant"));
        fruitQuest.add(new Quest(R.drawable.grape_correct, "grape"));
        fruitQuest.add(new Quest(R.drawable.orange_correct, "orange"));
        fruitQuest.add(new Quest(R.drawable.pear_correct, "pear"));
        fruitQuest.add(new Quest(R.drawable.strawberry_correct, "strawberry"));
        fruitQuest.add(new Quest(R.drawable.tomato_correct, "tomato"));
        GameActivity.myQuest = fruitQuest;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onStop() {
        mediaPlayer.pause();
        seek = mediaPlayer.getCurrentPosition();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        start();
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.pause();
        seek = mediaPlayer.getCurrentPosition();
        super.onBackPressed();
    }

    private void start(){
        if (seek>0){
            mediaPlayer.seekTo(seek);
        }
        mediaPlayer.start();
    }
}
