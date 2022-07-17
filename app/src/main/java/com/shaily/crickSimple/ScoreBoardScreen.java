package com.shaily.crickSimple;

import static android.view.View.VISIBLE;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_NAME;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_OVER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.shaily.crickSimple.databinding.ActivityScoreBoardScreenBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

public class ScoreBoardScreen extends AppCompatActivity {
    private static final String TAG = "ScoreBoardScreen";
    private ActivityScoreBoardScreenBinding binding;
    private float overLeft, overs;
    private int mainScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_score_board_screen);
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
            binding.textViewOver.setText(String.format("%.1f", overLeft));

            String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/bowl.mp4";
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
            overLeft = getRemainingOver();
            binding.textViewOver.setText(String.format("%.1f", overLeft));

        });

        binding.tvOne.setOnClickListener(view -> {
            setVideoOff();
            overLeft = getRemainingOver();
            binding.textViewOver.setText(String.format("%.1f", overLeft));
            setScore(1);
        });

        binding.tvFour.setOnClickListener(view -> {
            setVideoOff();
            overLeft = getRemainingOver();
            binding.textViewOver.setText(String.format("%.1f", overLeft));
            setScore(4);

        });

        binding.tvSix.setOnClickListener(view -> {
            setVideoOff();
            overLeft = getRemainingOver();
            binding.textViewOver.setText(String.format("%.1f", overLeft));
            setScore(6);
        });

        binding.tvOut.setOnClickListener(view -> {
            setVideoOff();
            overLeft = getRemainingOver();
            binding.textViewOver.setText(String.format("%.1f", overLeft));
        });

        binding.tvWide.setOnClickListener(view -> {
            setVideoOff();
            setScore(1);
        });
    }

    private void setScore(int score) {
        if(overLeft<=0 || String.format("%.1f", overLeft).equalsIgnoreCase("0.0"))
            return;
        mainScore = mainScore + score;
        binding.textViewScore.setText(mainScore+"");
    }

    private float getRemainingOver() {
       String s = String.format("%.1f", overLeft);
        if (s.endsWith(".0") || s.endsWith(".00")) {
            overLeft = overLeft - 0.50f;
        } else {
            overLeft = overLeft - 0.10f;
        }
        if (overLeft <=0 || String.format("%.1f", overLeft).equalsIgnoreCase("0.0")) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, CongratsScreen.class);
                intent.putExtra("EXTRA_SCORE", mainScore);
                startActivity(intent);
            }, 1000);
            return overLeft = 0f;
        } else
            return overLeft;
    }

    private void setVideoOff() {
        binding.videoView1.setVisibility(View.GONE);
        binding.linearScores.setVisibility(View.GONE);
        binding.tvWide.setVisibility(View.GONE);
        binding.tvOut.setVisibility(View.GONE);
        binding.tvBreak.setVisibility(VISIBLE);
    }

    private void setVideoOn() {
        binding.videoView1.setVisibility(VISIBLE);
        binding.linearScores.setVisibility(View.VISIBLE);
        binding.tvWide.setVisibility(VISIBLE);
        binding.tvOut.setVisibility(VISIBLE);
        binding.tvBreak.setVisibility(View.GONE);
    }

    private void setValues() {
        binding.textViewName.setText(SharedPreferencesHandler.getStringValues(this, KEY_NAME).trim());
        binding.textViewOver.setText(String.format("%.2f", SharedPreferencesHandler.getFloatValues(this, KEY_OVER)));
        overs = Float.parseFloat(binding.textViewOver.getText().toString());
        overLeft = overs;
    }
}