package com.babbel.renuka.languagegame.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.babbel.renuka.languagegame.R;
import com.babbel.renuka.languagegame.utils.AssetUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.babbel.renuka.languagegame.models.Keyword;

/**
 * Created by Renuka on 24/07/16.
 */

public class GameEngineActivity extends AppCompatActivity {

    private int mNoOfPlayers = 0;
    @Bind(R.id.status_img)
    ImageView statusImg;

    @Bind(R.id.option1_txt)
    TextView option1Txt;
    @Bind(R.id.option2_txt) TextView option2Txt;
    @Bind(R.id.option3_txt) TextView option3Txt;
    @Bind(R.id.option4_txt) TextView option4Txt;


    @Bind(R.id.player_1_btn)
    Button player1Btn;
    @Bind(R.id.player_2_btn) Button player2Btn;
    @Bind(R.id.player_3_btn) Button player3Btn;
    @Bind(R.id.player_4_btn) Button player4Btn;

    @Bind(R.id.player_1_txt) TextView player1Txt;
    @Bind(R.id.player_2_txt) TextView player2Txt;
    @Bind(R.id.player_3_txt) TextView player3Txt;
    @Bind(R.id.player_4_txt) TextView player4Txt;

    Integer[] animArray = {R.anim.option_1_anim, R.anim.option_2_anim, R.anim.option_3_anim, R.anim.option_4_anim};
    private List<Button> playerButtons = new ArrayList<>();
    private List<TextView> playerTexts = new ArrayList<>();
    private List<TextView> optionTexts = new ArrayList<>();
    private int[] scores = {0, 0, 0, 0};
    private List<Keyword> questions = new ArrayList<>();
    private List<Keyword> answers = new ArrayList<>();
    private int currentQuestionPosition = -1;
    private int currentAnswerPosition = 0;
    private boolean stopCurrentQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_engine);
        ButterKnife.bind(this);
        mNoOfPlayers = getIntent().getExtras().getInt("players", 1);
        initComponents();
        enablePlayers();
        fetchKeywords();
        if (!questions.isEmpty()) {
            answers = new ArrayList<>(questions);
            loadNextQuestion();
        }
    }

    private void fetchKeywords() {
        String jsonKeywords = AssetUtils.ReadFromfile("words.json", this);
        if (!TextUtils.isEmpty(jsonKeywords)) {
            Type listType = new TypeToken<List<Keyword>>() {
            }.getType();
            questions = new Gson().fromJson(jsonKeywords, listType);
        }
    }

    private void initComponents() {
        if (mNoOfPlayers >= 1) {
            playerButtons.add(player1Btn);
            playerTexts.add(player1Txt);
            optionTexts.add(option1Txt);
        }
        if (mNoOfPlayers >= 2) {
            playerButtons.add(player2Btn);
            playerTexts.add(player2Txt);
            optionTexts.add(option2Txt);
        }
        if (mNoOfPlayers >= 3) {
            playerButtons.add(player3Btn);
            playerTexts.add(player3Txt);
            optionTexts.add(option3Txt);
        }
        if (mNoOfPlayers == 4) {
            playerButtons.add(player4Btn);
            playerTexts.add(player4Txt);
            optionTexts.add(option4Txt);
        }
    }

    private void loadNextQuestion() {
        if (!stopCurrentQuestion) {
            currentQuestionPosition++;
            if (currentQuestionPosition >= 0 && currentQuestionPosition < questions.size()) {
                setQuestionText(questions.get(currentQuestionPosition).question);
                fetchOptions();
                fetchAnswers(0);
            }
        }
    }

    private void fetchOptions() {
        Collections.shuffle(answers);
        int correctAnswerPosition = new Random().nextInt(4);
        int currentAnswerPosition = answers.indexOf(questions.get(currentQuestionPosition));
        answers.set(currentAnswerPosition, answers.get(correctAnswerPosition));
        answers.set(correctAnswerPosition, questions.get(currentQuestionPosition));
    }

    private void fetchAnswers(int answerPosition) {
        if (answerPosition >= 4 || answerPosition >= answers.size()) {
            answerPosition = 0;
        }
        currentAnswerPosition = answerPosition;
        if (!stopCurrentQuestion) {
            setOptionText(answers.get(answerPosition).answer);
            for (int i = 0; i < mNoOfPlayers; i++) {
                startAnimation(optionTexts.get(i), animArray[i], answerPosition);
            }
        }
    }

    private void startAnimation(View view, int animId, final int answerPosition) {
        Animation anim = AnimationUtils.loadAnimation(this, animId);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setOptionTextVisibility(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setOptionTextVisibility(false);
                fetchAnswers(answerPosition + 1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    private void enablePlayers() {
        for (int i = 0; i < mNoOfPlayers; i++) {
            playerButtons.get(i).setVisibility(View.VISIBLE);
            playerTexts.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void setQuestionText(String text) {
        for (TextView textView : playerTexts) {
            textView.setText(text);
        }
    }

    private void setOptionText(String text) {
        for (TextView textView : optionTexts) {
            textView.setText(text);
        }
    }

    private void setOptionTextVisibility(boolean show) {
        for (TextView textView : optionTexts) {
            textView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void setPlayersEnabled(boolean enable) {
        for (Button button : playerButtons) {
            button.setEnabled(enable);
        }
    }

    private boolean isAnswerCorrect() {
        try {
            if (questions.get(currentQuestionPosition).answer.equalsIgnoreCase(answers.get(currentAnswerPosition).answer)) {
                return true;
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        return false;
    }

    @OnClick({R.id.player_1_btn, R.id.player_2_btn, R.id.player_3_btn, R.id.player_4_btn})
    public void onBuzzerClicked(final View view) {
        view.setActivated(true);
        stopCurrentQuestion = true;
        setOptionTextVisibility(false);
        setPlayersEnabled(false);

        if (isAnswerCorrect()) {
            updateScores(view, true);
            statusImg.setImageDrawable(getResources().getDrawable(R.drawable.success));
            setQuestionText("Player " + view.getTag().toString() + " is right");
        } else {
            updateScores(view, false);
            statusImg.setImageDrawable(getResources().getDrawable(R.drawable.failure));
            setQuestionText("Player " + view.getTag().toString() + " is wrong");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopCurrentQuestion = false;
                view.setActivated(false);
                setPlayersEnabled(true);
                statusImg.setImageDrawable(getResources().getDrawable(R.drawable.neutral));
                loadNextQuestion();
            }
        }, 5000);
    }

    private void updateScores(View v, boolean success) {
        for (int i = 0; i < mNoOfPlayers; i++) {
            if (playerButtons.get(i).getId() == v.getId()) {
                if (success)
                    scores[i]++;
                else
                    scores[i]--;
            }
            playerButtons.get(i).setText((i + 1) + "\n(" + scores[i] + ")");
        }
    }
}
