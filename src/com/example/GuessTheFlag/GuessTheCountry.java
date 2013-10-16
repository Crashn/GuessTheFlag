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
    }
}
