package com.chrisz.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.chrisz.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.chrisz.android.geoquiz.answer_shown";
    private static final String WAS_ANSWER_SHOWN = "shown";
    private static final String TAG = "CheatActivity";

    private boolean mAnswerIsTrue;
    private boolean mWasAnswerShown;
    private Button mShowAnswerButton;
    private TextView mAnswerTextView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_ANSWER_SHOWN, mWasAnswerShown);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
            mWasAnswerShown = savedInstanceState.getBoolean(WAS_ANSWER_SHOWN);
        }

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                } else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                mWasAnswerShown = true;
                setAnswerShownResult();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    // 增加动画
                    int cx = mShowAnswerButton.getWidth() /2;
                    int cy = mShowAnswerButton.getHeight() /2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    private void setAnswerShownResult(){
        Intent data = new Intent();
        Log.d(TAG, "setAnswerShownResult is " + mWasAnswerShown);
        data.putExtra(EXTRA_ANSWER_SHOWN, mWasAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(Intent result){
        boolean flag = result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
        Log.d(TAG, "wasAnswerShown is " + flag);
        return flag;
    }
}
