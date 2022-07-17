package com.shaily.crickSimple;

import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shaily.crickSimple.databinding.ActivityEnterNameBinding;
import com.shaily.crickSimple.databinding.ActivitySelectBowlerBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

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
            if(isValidName()) {
                SharedPreferencesHandler.setStringValues(this,KEY_NAME,  binding.editTextName.getText().toString());
                startActivity(new Intent(EnterName.this, EnterOvers.class));
            }else{
                showError(true);
            }
        });

        binding.editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showError(!isValidName());
            }
        });
    }

    private void showError(boolean isError) {
        binding.textViewError.setText(R.string.error_msg_name);
        binding.textViewError.setVisibility(isError?View.VISIBLE:View.GONE);
    }

    private boolean isValidName() {
        return (!TextUtils.isEmpty(binding.editTextName.getText()) &&
                !TextUtils.isEmpty(binding.editTextName.getText().toString()));
    }
}