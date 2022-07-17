package com.shaily.crickSimple;

import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_CHALLENGE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.shaily.crickSimple.databinding.ActivitySelectChallengeBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

import java.util.regex.Pattern;

public class SelectChallenge extends AppCompatActivity {
    private static final String TAG = "SelectChallenge";
    private ActivitySelectChallengeBinding binding;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_challenge);

        binding.next.setOnClickListener(view -> {
            if (isValid()) {
                try {
                    SharedPreferencesHandler.setIntValues(this, KEY_CHALLENGE, Integer.parseInt(binding.editTextOver.getText().toString()));
                } catch (Exception ex) {
                    SharedPreferencesHandler.setIntValues(this, KEY_CHALLENGE, 0);
                    Log.e(TAG, "Error " + ex);
                }
                Intent localIntent = new Intent(SelectChallenge.this, ChallengeScreen.class);
                startActivity(localIntent);
            } else errorChecks();
        });


    }

    private void errorChecks() {

        if (!TextUtils.isEmpty(binding.editTextOver.getText()) &&
                !TextUtils.isEmpty(binding.editTextOver.getText().toString()) &&
                pattern.matcher(binding.editTextOver.getText().toString()).matches() &&
                !binding.editTextOver.getText().toString().startsWith("0") &&
                !binding.editTextOver.getText().toString().startsWith(".")) {
            double over = Double.parseDouble(binding.editTextOver.getText().toString());
            showError(over < 0, getString(R.string.error_zero_msg_chal));
        } else
            showError(true, getString(R.string.error_msg_chal));
    }

    private void showError(boolean isError, String errorMsg) {
        binding.textViewError.setText(errorMsg);
        binding.textViewError.setVisibility(isError ? View.VISIBLE : View.GONE);
    }

    private boolean isValid() {
        return (!(TextUtils.isEmpty(binding.editTextOver.getText()) &&
                TextUtils.isEmpty(binding.editTextOver.getText().toString())) &&
                pattern.matcher(binding.editTextOver.getText().toString()).matches() &&
                binding.editTextOver.getText().toString().length() > 0 &&
                Double.parseDouble(binding.editTextOver.getText().toString()) > 0 &&
                !binding.editTextOver.getText().toString().startsWith("0") &&
                !binding.editTextOver.getText().toString().startsWith("."));
    }
}