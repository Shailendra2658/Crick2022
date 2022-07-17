package com.shaily.crickSimple;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_CHALLENGE;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_FOUR_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_NAME;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_ONE_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_OUT_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_OVER;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_SIX_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_WIDE_URI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shaily.crickSimple.databinding.ActivityChallengeScreenBinding;
import com.shaily.crickSimple.databinding.ActivitySelectChallengeBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

public class ChallengeScreen extends AppCompatActivity {
    private static final String TAG = "ChallengeScreen";
    private ActivityChallengeScreenBinding binding;
    private int ballsLeft;
    private int mainScore, scoreLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_screen);
        setValues();
        binding.restart.setOnClickListener(view -> {
            Intent intent = new Intent(this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.tvBreak.setOnClickListener(view -> {
            Intent intent = new Intent(this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.tvBowl.setOnClickListener(view -> {
           // String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/bowl.mp4";
            String str = SharedPreferencesHandler.getStringValues(this, KEY_URI);
            setVideoOn();
            binding.videoView1.setVideoPath(str);
            binding.videoView1.requestFocus();
            binding.videoView1.start();
        });

        binding.videoView1.setOnCompletionListener(mediaPlayer -> {
            setVideoOff();
        });

        binding.tvZero.setOnClickListener(view -> {
            setVideoOff();
            binding.textViewBalls.setText(getRemainingBalls()+"");

        });

        binding.tvOne.setOnClickListener(view -> {
            setVideoOff();
            binding.textViewBalls.setText(getRemainingBalls()+"");
            setScore(1);
            String str = SharedPreferencesHandler.getStringValues(this, KEY_ONE_URI);
            setOtherVideoOn();
            binding.videoView1.setVideoPath(str);
            binding.videoView1.requestFocus();
            binding.videoView1.start();
        });

        binding.tvFour.setOnClickListener(view -> {
            setVideoOff();
            binding.textViewBalls.setText(getRemainingBalls()+"");
            setScore(4);
            String str = SharedPreferencesHandler.getStringValues(this, KEY_FOUR_URI);
            setOtherVideoOn();
            binding.videoView1.setVideoPath(str);
            binding.videoView1.requestFocus();
            binding.videoView1.start();

        });

        binding.tvSix.setOnClickListener(view -> {
            setVideoOff();
            binding.textViewBalls.setText(getRemainingBalls()+"");
            setScore(6);
            String str = SharedPreferencesHandler.getStringValues(this, KEY_SIX_URI);
            setOtherVideoOn();
            binding.videoView1.setVideoPath(str);
            binding.videoView1.requestFocus();
            binding.videoView1.start();
        });

        binding.tvOut.setOnClickListener(view -> {
            setVideoOff();
            setScore(-5);
            binding.textViewBalls.setText(getRemainingBalls()+"");
            String str = SharedPreferencesHandler.getStringValues(this, KEY_OUT_URI);
            setOtherVideoOn();
            binding.videoView1.setVideoPath(str);
            binding.videoView1.requestFocus();
            binding.videoView1.start();
        });

        binding.tvWide.setOnClickListener(view -> {
            setVideoOff();
            setScore(1);
            String str = SharedPreferencesHandler.getStringValues(this, KEY_WIDE_URI);
            setOtherVideoOn();
            binding.videoView1.setVideoPath(str);
            binding.videoView1.requestFocus();
            binding.videoView1.start();
        });

    }

    private void setScore(int score) {
        if(ballsLeft<=0)
            return;
        mainScore = mainScore + score;
        binding.textViewUrScr.setText(mainScore+"");
        scoreLeft = (score<scoreLeft)?scoreLeft - score : 0;
        if(scoreLeft > 0) {
            //scoreLeft = scoreLeft - mainScore;
            binding.textViewScore.setText(scoreLeft+"");
        } else {
            scoreLeft = 0;
            binding.textViewNeed.setText("Your Score");
            binding.textViewScore.setText(mainScore+"");
            //binding.textViewBalls.setText();
            binding.textViewUrScr.setVisibility(GONE);
            binding.textViewYou.setText("Target Achieved");
            binding.imageViewTarget.setVisibility(VISIBLE);
            binding.textViewIn.setVisibility(GONE);
            binding.textViewLeft.setVisibility(VISIBLE);
        }
    }

    private int getRemainingBalls() {
        ballsLeft = ballsLeft-1;
        if (ballsLeft <=0) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, CongratsScreen.class);
                intent.putExtra("EXTRA_SCORE", mainScore);
                startActivity(intent);
            }, 1000);
            return ballsLeft = 0;
        } else
            return ballsLeft;
    }

    private void setVideoOff() {
        binding.videoView1.setVisibility(GONE);
        binding.linearScores.setVisibility(GONE);
        binding.tvWide.setVisibility(GONE);
        binding.tvOut.setVisibility(GONE);
        binding.tvBreak.setVisibility(VISIBLE);
    }

    private void setVideoOn() {
        binding.videoView1.setVisibility(VISIBLE);
        binding.linearScores.setVisibility(View.VISIBLE);
        binding.tvWide.setVisibility(VISIBLE);
        binding.tvOut.setVisibility(VISIBLE);
        binding.tvBreak.setVisibility(GONE);
    }

    private void setOtherVideoOn() {
        binding.videoView1.setVisibility(VISIBLE);
        binding.linearScores.setVisibility(View.VISIBLE);
        binding.tvWide.setVisibility(GONE);
        binding.tvOut.setVisibility(GONE);
        binding.tvBreak.setVisibility(GONE);
    }

    private void setValues() {
        binding.textViewName.setText(SharedPreferencesHandler.getStringValues(this, KEY_NAME).trim());
        binding.textViewScore.setText(SharedPreferencesHandler.getIntValues(this, KEY_CHALLENGE)+"");
        scoreLeft = SharedPreferencesHandler.getIntValues(this, KEY_CHALLENGE);
        ballsLeft = (int) (SharedPreferencesHandler.getFloatValues(this, KEY_OVER) * 6);
        binding.textViewBalls.setText(ballsLeft+"");
        binding.textViewUrScr.setText("0");
    }
}