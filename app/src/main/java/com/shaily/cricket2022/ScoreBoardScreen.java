package com.shaily.cricket2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.shaily.cricket2022.databinding.ActivityEnterOversBinding;
import com.shaily.cricket2022.databinding.ActivityScoreBoardScreenBinding;

public class ScoreBoardScreen extends AppCompatActivity {
    private static final String TAG = "ScoreBoardScreen";
    private ActivityScoreBoardScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_score_board_screen);
         setValues();
    }

    private void setValues() {
        binding.textViewOver.setText("6WibmoSecurePreferences.0");
    }
}