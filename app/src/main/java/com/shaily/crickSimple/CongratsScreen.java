package com.shaily.crickSimple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.shaily.crickSimple.databinding.ActivityCongratsScreenBinding;

public class CongratsScreen extends AppCompatActivity {
    private static final String TAG = "CongratsScreen";
    private ActivityCongratsScreenBinding binding;
    private int mainScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_congrats_screen);
        mainScore = getIntent().getIntExtra("EXTRA_SCORE", 0);
        binding.tvFinalScore.setText(mainScore+"");

        binding.restart.setOnClickListener(view -> {
            Intent intent = new Intent(this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        while (true)
        {
            //binding.tvName.setText(code.name + " your final score is :");
            binding.flipper.startFlipping();
            binding.flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));
            binding.flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));

            /*this.fours.setText(Integer.toString(code.fours));
            this.sixes.setText(Integer.toString(code.sixes));
            this.balls.setText(Integer.toString(i));*/

            return;
        }
    }
}