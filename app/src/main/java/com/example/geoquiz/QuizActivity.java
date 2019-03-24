package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_NUMBER_ANSWERED = "number_answered";
    private static final String KEY_NUMBER_CORRECT = "number_correct";
    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private int mNumberAnswered = 0;
    private int mNumberCorrect = 0;
    private Question[] mQuestionBank = Question.getQuestionBank();
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: 被调用。");

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mNumberAnswered = savedInstanceState.getInt(KEY_NUMBER_ANSWERED, 0);
            mNumberCorrect = savedInstanceState.getInt(KEY_NUMBER_CORRECT, 0);
        }

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mQuestionTextView = findViewById(R.id.question_text_view);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        };
        mQuestionTextView.setOnClickListener(listener);
        updateQuestion();

        mPreviousButton = findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 被调用。");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 被调用。");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 被调用。");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 被调用。");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 被调用。");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: 被调用。");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_NUMBER_ANSWERED, mNumberAnswered);
        outState.putInt(KEY_NUMBER_CORRECT, mNumberCorrect);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        updateButtonsState();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = (answerIsTrue == userPressedTrue) ?
                R.string.correct_toast : R.string.incorrect_toast;
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
        mQuestionBank[mCurrentIndex].setAnswered(true);
        updateButtonsState();
        ++mNumberAnswered;
        if (answerIsTrue == userPressedTrue) {
            ++mNumberCorrect;
        }
        if (mNumberAnswered >= mQuestionBank.length) {
            String result = "总分数为（满分100）：" +
                    (double) mNumberCorrect / mQuestionBank.length * 100;
            Toast.makeText(QuizActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

    private void updateButtonsState() {
        if (mQuestionBank[mCurrentIndex].isAnswered()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }
}
