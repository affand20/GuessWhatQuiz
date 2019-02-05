package com.trydev.games.guesswhat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.trydev.games.guesswhat.Quest.Quest;
import com.trydev.games.guesswhat.Service.MediaService;

import java.util.ArrayList;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;

    Button skip;
    ImageButton speakbutton;
    ImageView questimage;
    TextView result, chanceleft;
    int kesempatan, random, totalquestion;
    Quest quest;

    public static ArrayList<Quest> myQuest;
    public int totalanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        skip = (Button) findViewById(R.id.skip);
        speakbutton = (ImageButton) findViewById(R.id.answer_button);
        result = (TextView) findViewById(R.id.static_question);
        questimage = (ImageView) findViewById(R.id.quest_image);
        chanceleft = (TextView) findViewById(R.id.chance_left);
        kesempatan = 5;
        random = 0;
        totalanswer = 0;
        totalquestion = myQuest.size();

        setSoal(random());

        speakbutton.setOnTouchListener(this);
        skip.setOnClickListener(this);

        chanceleft.setText(String.valueOf(kesempatan));

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                result.setText(R.string.try_again);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> speechresult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION.toLowerCase());

                for (int i = 0; i < speechresult.size(); i++) {
                    speechresult.set(i, speechresult.get(i).toLowerCase());
                    Log.d("SPEECH RESULT", speechresult.get(i));
                }

                boolean isTrue = false;

                if (speechresult!=null){
                    for (int i = 0; i < speechresult.size(); i++) {

                        //if the answer is true
                        if (speechresult.get(i).toLowerCase().equals(quest.getTrueanswer())){
                            isTrue = true;
                            totalanswer++;
                            result.setText(speechresult.get(i));
                            myQuest.remove(quest);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (kesempatan>0) {
                                        setSoal(random());
                                    } else{
                                        String result = String.format(getResources().getString(R.string.final_result), totalanswer, totalquestion);
                                        GameActivity.this.finish();
                                        Intent intent = new Intent(GameActivity.this, FinalActivity.class);
                                        intent.putExtra("RESULT", result);
//                                        MainActivity.it.setAction(MediaService.ACTION_PLAY);
//                                        MainActivity.it.setPackage(MediaService.ACTION_PACKAGE);
//                                        startService(MainActivity.it);
                                        startActivity(intent);
                                    }
                                }
                            }, 1500);
                            break;
                        }
                    }
                }

                if (!isTrue){
                    kesempatan--;
                    chanceleft.setText(String.valueOf(kesempatan));
                    result.setText(R.string.wrong);
                    if (kesempatan<=0){
                        String result = String.format(getResources().getString(R.string.final_result), totalanswer, totalquestion);
                        GameActivity.this.finish();
                        Intent intent = new Intent(GameActivity.this, FinalActivity.class);
                        intent.putExtra("RESULT", result);
//                        MainActivity.it.setAction(MediaService.ACTION_PLAY);
//                        MainActivity.it.setPackage(MediaService.ACTION_PACKAGE);
//                        startService(MainActivity.it);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId()==R.id.answer_button){
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    mSpeechRecognizer.stopListening();
//                    result.setText("Wait a minute..");
                    break;
                case MotionEvent.ACTION_DOWN:
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    result.setText(R.string.listening);
                    break;
            }
        }
        return false;
    }

    private void setSoal(int number){
//        for (int i = 0; i < myQuest.size(); i++) {
//            Log.d("LIST OF QUESTION", "QUESTION: "+myQuest.get(i).getTrueanswer());
//        }
        if (myQuest.size()>0){
            result.setText(R.string.static_question);
            random = number;
            quest = myQuest.get(number);
            questimage.setImageResource(quest.getUrl());
        } else if (myQuest.size()==1){
            skip.setText("");
        }
        else{
            String result = String.format(getResources().getString(R.string.final_result), totalanswer, myQuest.size());
            GameActivity.this.finish();
            Intent intent = new Intent(GameActivity.this, FinalActivity.class);
            intent.putExtra("RESULT", result);
//            MainActivity.it.setAction(MediaService.ACTION_PLAY);
//            MainActivity.it.setPackage(MediaService.ACTION_PACKAGE);
//            startService(MainActivity.it);
            startActivity(intent);
        }
    }

    private int random(){
        return ((int) (Math.random() * (((myQuest.size()-1) - 0) + 1)));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.skip){
            int random2 = random();

            while (random2==random){
                random2 = random();
            }
            if (kesempatan>0){
                setSoal(random2);
            }
//            else{
//                Toast.makeText(this, "Game Completed", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.surrender)
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GameActivity.this, CategoryActivity.class);
                        GameActivity.this.finish();
//                        MainActivity.it.setAction(MediaService.ACTION_PLAY);
//                        MainActivity.it.setPackage(MediaService.ACTION_PACKAGE);
//                        startService(MainActivity.it);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
