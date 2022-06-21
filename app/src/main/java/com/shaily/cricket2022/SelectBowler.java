package com.shaily.cricket2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.shaily.cricket2022.databinding.ActivitySelectBowlerBinding;


public class SelectBowler extends AppCompatActivity {
    private static final String TAG = "SelectBowler";
    private ActivitySelectBowlerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_bowler);

        selectBowler(binding.bowller, getDrawable(R.drawable.ones), getDrawable(R.drawable.one));
        selectBowler(binding.bowller1, getDrawable(R.drawable.twos), getDrawable(R.drawable.two));
        selectBowler(binding.bowller2, getDrawable(R.drawable.threes), getDrawable(R.drawable.three));
        selectBowler(binding.bowller3, getDrawable(R.drawable.fours), getDrawable(R.drawable.four));
        selectBowler(binding.bowller4, getDrawable(R.drawable.fives), getDrawable(R.drawable.five));
        selectBowler(binding.bowller5, getDrawable(R.drawable.sixes), getDrawable(R.drawable.six));

        binding.level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup paramAnonymousRadioGroup, int paramAnonymousInt)
            {
                Intent intent = new Intent(SelectBowler.this, EnterName.class);

                startActivity(intent);
            }
        });
    }

    private void selectBowler(CheckBox bowler, Drawable selected, Drawable UnSelected) {
        bowler.setOnCheckedChangeListener((compoundButton, b) -> {
            resetBowler();
           if(b)
               bowler.setBackground(selected);
           else
               bowler.setBackground(UnSelected);

       });
    }

    private void resetBowler() {
        binding.bowller.setBackground(getDrawable(R.drawable.one));
        binding.bowller1.setBackground(getDrawable(R.drawable.two));
        binding.bowller2.setBackground(getDrawable(R.drawable.three));
        binding.bowller3.setBackground(getDrawable(R.drawable.four));
        binding.bowller4.setBackground(getDrawable(R.drawable.five));
        binding.bowller5.setBackground(getDrawable(R.drawable.six));
        binding.bowller.setChecked(false);
        binding.bowller1.setChecked(false);
        binding.bowller2.setChecked(false);
        binding.bowller3.setChecked(false);
        binding.bowller4.setChecked(false);
        binding.bowller5.setChecked(false);
    }
}