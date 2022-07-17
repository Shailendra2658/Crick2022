package com.shaily.crickSimple;

import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_OVER;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.shaily.crickSimple.databinding.ActivitySelectGameTypeBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

public class SelectGameType extends AppCompatActivity {
    private static final String TAG = "SelectGameType";
    private ActivitySelectGameTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_game_type);

        SharedPreferencesHandler.setFloatValues(this, KEY_OVER, 0f);

        binding.radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup paramAnonymousRadioGroup, int paramAnonymousInt) {
                switch (paramAnonymousInt) {
                    case R.id.radioButton1:
                        Toast.makeText(SelectGameType.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton2:
                        Toast.makeText(SelectGameType.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton3:
                        startActivity(new Intent(SelectGameType.this, EnterName.class));
                        break;
                    default:
                        Toast.makeText(SelectGameType.this, "Invalid Mode!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}