package com.shaily.crickSimple;

import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_FOUR_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_ONE_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_OUT_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_SIX_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_TWO_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_URI;
import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_WIDE_URI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.shaily.crickSimple.databinding.ActivitySelectBowlerBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;


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

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/bowl.mp4";
        String fileSixPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/six.mp4";
        String fileFourPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/four.mp4";
        String fileTwoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/two.mp4";
        String fileOnePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/one.mp4";
        String fileOutPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/out.mp4";
        String fileWidePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/wide.mp4";


        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_URI, filePath);
        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_SIX_URI, fileSixPath);
        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_FOUR_URI, fileFourPath);
        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_TWO_URI, fileTwoPath);
        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_ONE_URI, fileOnePath);
        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_OUT_URI, fileOutPath);
        SharedPreferencesHandler.setStringValues(SelectBowler.this, KEY_WIDE_URI, fileWidePath);


        selectBowler(binding.bowller1, getDrawable(R.drawable.ones), getDrawable(R.drawable.one));
        selectBowler(binding.bowller2, getDrawable(R.drawable.twos), getDrawable(R.drawable.two));
        selectBowler(binding.bowller3, getDrawable(R.drawable.threes), getDrawable(R.drawable.three));
        selectBowler(binding.bowller4, getDrawable(R.drawable.fours), getDrawable(R.drawable.four));
        selectBowler(binding.bowller5, getDrawable(R.drawable.fives), getDrawable(R.drawable.five));
        selectBowler(binding.bowller6, getDrawable(R.drawable.sixes), getDrawable(R.drawable.six));

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
        binding.bowller1.setBackground(getDrawable(R.drawable.one));
        binding.bowller2.setBackground(getDrawable(R.drawable.two));
        binding.bowller3.setBackground(getDrawable(R.drawable.three));
        binding.bowller4.setBackground(getDrawable(R.drawable.four));
        binding.bowller5.setBackground(getDrawable(R.drawable.five));
        binding.bowller6.setBackground(getDrawable(R.drawable.six));
        binding.bowller1.setChecked(false);
        binding.bowller2.setChecked(false);
        binding.bowller3.setChecked(false);
        binding.bowller4.setChecked(false);
        binding.bowller5.setChecked(false);
        binding.bowller6.setChecked(false);
    }
}