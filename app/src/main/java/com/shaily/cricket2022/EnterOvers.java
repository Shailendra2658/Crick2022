package com.shaily.cricket2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.shaily.cricket2022.databinding.ActivityEnterNameBinding;
import com.shaily.cricket2022.databinding.ActivityEnterOversBinding;

public class EnterOvers extends AppCompatActivity {
    private static final String TAG = "EnterName";
    private ActivityEnterOversBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_overs);

        binding.next.setOnClickListener(view -> {
            startActivity(new Intent(EnterOvers.this,ScoreBoardScreen.class));

        });


    }
}