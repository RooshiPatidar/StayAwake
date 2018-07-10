package me.rooshi.stayawake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MathActivity extends AppCompatActivity {

    TextView questionTV;
    TextView scoreTV;
    TextView choice1TV;
    TextView choice2TV;
    TextView choice3TV;
    TextView choice4TV;

    boolean running = false;
    Random rand = new Random();
    ArrayList<TextView> choices = new ArrayList<>();
    int right;
    int total;
    int currAns;
    int needCorrect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        Intent thisIntent = getIntent();
        needCorrect = thisIntent.getIntExtra("numQ", 2);

        questionTV = findViewById(R.id.problemTextView);
        scoreTV = findViewById(R.id.scoreTextView);
        choice1TV = findViewById(R.id.topLeftTextView);
        choice2TV = findViewById(R.id.topRightTextView);
        choice3TV = findViewById(R.id.botLeftTextView);
        choice4TV = findViewById(R.id.botRightTextView);

        choices.add(choice1TV);
        choices.add(choice2TV);
        choices.add(choice3TV);
        choices.add(choice4TV);

        right = 0;
        total = 0;

        startGame();

    }

    public void startGame() {

        running = true;

        newQuestion();
    }

    /**
     * This method creates a new question with 2 addends between 0 and 20
     * @return and int which is the answer to the question
     */
    private void newQuestion() {
        if (right >= needCorrect) {
            setResult(66);
            finish();

        } else {

            int addend1 = rand.nextInt(20);
            int addend2 = rand.nextInt(20);
            int answer = addend1 + addend2;
            currAns = answer;
            String questionString = addend1 + " + " + addend2 + " = ?";
            questionTV.setText(questionString);
            createAnswerChoices();
        }
    }

    @SuppressLint("SetTextI18n")
    private void createAnswerChoices() {
        Collections.shuffle(choices);
        choices.get(0).setText(Integer.toString(currAns));
        for (int i = 1; i <= 3; i++) {
            choices.get(i).setText(Integer.toString(rand.nextInt(40)));
        }
    }

    public void choiceSelected(View view) {
        TextView textView = (TextView)view;
        if (Integer.parseInt(((TextView) view).getText().toString()) == currAns) {
            right++;
        }
        total++;
        String score = right + "/" + total;
        scoreTV.setText(score);

        newQuestion();
    }

}
