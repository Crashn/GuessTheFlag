package com.example.GuessTheFlag;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GuessTheCountry extends Activity {

    private static final String TAG = "FlagQuizGame Activity";

    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Map<String ,Boolean> regionsMap;
    private String correctAnswer;
    private int totalGuesses;
    private int correctAnswers;   //count
    private int guessRows;
    private Random random;
    private Handler handler; //wait before load next flag
    private Animation shakeAnimation; //wrong answer animation
    private TextView answerTextView;
    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private TableLayout buttonTableLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fileNameList = new ArrayList<String>();
        quizCountriesList = new ArrayList<String>();
        regionsMap = new HashMap<String, Boolean>();
        guessRows = 1; //default 1 string of answer buttons
        random = new Random();
        handler = new Handler();

        shakeAnimation = AnimationUtils.loadAnimation(this,R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);

        String[] regionNames = getResources().getStringArray(R.array.regionsList);

        for(String region : regionNames){
            regionsMap.put(region, true);
        }

        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView) findViewById(R.id.flagImageView);
        buttonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        questionNumberTextView.setText(getResources().getString(R.string.question) + " 1 " +
        getResources().getString(R.string.of) + " 10");

        resetQuiz();
    }

    private void resetQuiz(){

        AssetManager assets = getAssets();

        fileNameList.clear();

        try{
            Set<String> regions = regionsMap.keySet();

            for(String region:regions){

                if(regionsMap.get(region)){
                    String[] paths = assets.list(region);

                    for(String path : paths){
                        fileNameList.add(path.replace(".png", ""));
                    }
                }
            }
        }
        catch (IOException e){
            Log.e(TAG, "Error loading images file names", e);
        }

        correctAnswers = 0;
        totalGuesses = 0;

        quizCountriesList.clear();

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();

        while(flagCounter <=10){

            int randomIndex = random.nextInt(numberOfFlags);

            String fileName = fileNameList.get(randomIndex);

            if(!quizCountriesList.contains(fileName)){
                quizCountriesList.add(fileName);
                ++flagCounter;
            }
        }
        loadNextFlag();
    }

    private void loadNextFlag(){

        String nextImageName = quizCountriesList.remove(0);
        correctAnswer = nextImageName;
        answerTextView.setText("");

        questionNumberTextView.setText(
                getResources().getString(R.string.question) + " " +
                        (correctAnswer + 1) + " " +
                getResources().getString(R.string.of) + " 10");

        //imageName format region-country
        String region = nextImageName.substring(0, nextImageName.indexOf('-'));

        AssetManager assets = getAssets();
        InputStream stream;

        try{
            stream = assets.open(region + "/" + nextImageName + ".png");

            Drawable flag = Drawable.createFromStream(stream, nextImageName);
            flagImageView.setImageDrawable(flag);
        }
        catch (IOException e){
            Log.e(TAG, "Error loading " + nextImageName, e);
        }
        //clear prev answer buttons
        for(int row = 0; row < buttonTableLayout.getChildCount(); ++row){
            ((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();
        }

        Collections.shuffle(fileNameList);//like a mixer

        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));  //magic //TODO:how it works?

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < guessRows; row++){

            TableRow currentTableRow = getTableRow(row);

            for (int column = 0; column < 3; column++){

                Button newGuessButton = (Button) inflater.inflate(R.layout.guess_button, null);

                String fileName = fileNameList.get((row * 3) + column); // TODO:some magic again
                newGuessButton.setText(getCountryName(filename));

                newGuessButton.setOnClickListener(guessButtonListener);
                currentTableRow.addView(newGuessButton);
            }
        }

        int row = random.nextInt(guessRows);
        int column = random.nextInt(3);
        TableRow randomTableRow = getTableRow(row);
        String countryName = getCountryName(correctAnswer);
        ((Button) randomTableRow.getChildAt(column)).setText(countryName);

    }

    private TableRow getTableRow(int row){
        return (TableRow) buttonTableLayout.getChildAt(row);
    }

    private String getCountryName(String name){
        return name.substring(name.indexOf('-') + 1).replace('_',' '); //pokerface smile
    }

}
