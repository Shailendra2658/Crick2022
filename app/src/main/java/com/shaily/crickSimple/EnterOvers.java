package com.shaily.crickSimple;

import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_OVER;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.shaily.crickSimple.databinding.ActivityEnterOversBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

import java.util.regex.Pattern;

public class EnterOvers extends AppCompatActivity {
    private static final String TAG = "EnterName";
    private ActivityEnterOversBinding binding;
    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_overs);

        binding.next.setOnClickListener(view -> {
            if (isValid()) {
                try {
                    SharedPreferencesHandler.setFloatValues(this, KEY_OVER, Float.parseFloat(binding.editTextOver.getText().toString()));
                }catch (Exception ex){
                    SharedPreferencesHandler.setFloatValues(this, KEY_OVER, 0f);
                    Log.e(TAG, "Error "+ex);
                }

                startActivity(new Intent(EnterOvers.this, SelectChallenge.class));
            }else
                errorChecks();
        });
        binding.editTextOver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                errorChecks();
            }
        });

    }

    private void errorChecks() {

        if (!TextUtils.isEmpty(binding.editTextOver.getText()) &&
                !TextUtils.isEmpty(binding.editTextOver.getText().toString()) &&
                pattern.matcher(binding.editTextOver.getText().toString()).matches() &&
                !binding.editTextOver.getText().toString().startsWith("0") &&
                !binding.editTextOver.getText().toString().startsWith(".")) {
            double over = Double.parseDouble(binding.editTextOver.getText().toString());
            showError(over < 0, getString(R.string.error_zero_msg_over));
        }else
            showError(true, getString(R.string.error_msg_over));
    }

    private void showError(boolean isError, String errorMsg) {
        binding.textViewError.setText(errorMsg);
        binding.textViewError.setVisibility(isError ? View.VISIBLE : View.GONE);
    }

    private boolean isValid() {
        return (!(TextUtils.isEmpty(binding.editTextOver.getText()) &&
                TextUtils.isEmpty(binding.editTextOver.getText().toString())) &&
                pattern.matcher(binding.editTextOver.getText().toString()).matches() &&
                binding.editTextOver.getText().toString().length()>0 &&
                Double.parseDouble(binding.editTextOver.getText().toString())>0  &&
                !binding.editTextOver.getText().toString().startsWith("0") &&
                !binding.editTextOver.getText().toString().startsWith("."));
    }
}