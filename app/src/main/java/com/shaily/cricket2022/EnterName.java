package com.shaily.cricket2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.shaily.cricket2022.databinding.ActivityEnterNameBinding;
import com.shaily.cricket2022.databinding.ActivitySelectBowlerBinding;
import com.shaily.cricket2022.util.SharedPreferencesHandler;

public class EnterName extends AppCompatActivity {
    private static final String TAG = "EnterName";
    private ActivityEnterNameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_name);

        binding.next.setOnClickListener(view -> {
            SharedPreferencesHandler.setStringValues(this,"KEY_NAME", binding.editTextName.getText().toString());
            startActivity(new Intent(EnterName.this,EnterOvers.class));
        });
    }
}