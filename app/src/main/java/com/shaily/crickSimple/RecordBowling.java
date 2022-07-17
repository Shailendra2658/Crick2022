package com.shaily.crickSimple;

import static com.shaily.crickSimple.util.SharedPreferencesHandler.KEY_URI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.shaily.crickSimple.databinding.ActivityRecordBowlingBinding;
import com.shaily.crickSimple.util.SharedPreferencesHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class RecordBowling extends AppCompatActivity {
    private static final String TAG = "RecordBowling";
    private static final int VIDEO_CAPTURE = 1100;
    private static final int VIDEO_PERMISSION = 1101;
    private int requestCode;
    private ActivityRecordBowlingBinding binding;
    private String[] MULTIPLE_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private ArrayList<String> permissionsList;
    private Integer mSensorOrientation;
    private int permissionsCount = 0;
    private AlertDialog alertDialog;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_bowling);
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Videos/bowl.mp4";
        SharedPreferencesHandler.setStringValues(RecordBowling.this, KEY_URI, filePath);

        Glide.with(this)
                .load(Uri.fromFile(new File(filePath)))
                .into(binding.imageViewThumb1);

        Glide.with(this)
                .load(Uri.fromFile(new File(filePath)))
                .into(binding.imageViewThumb2);

        binding.imageViewThumb1.setOnClickListener(view -> {

        });

        binding.imageViewThumb2.setOnClickListener(view -> {

        });

        binding.imageBtnRec.setOnClickListener(view -> {
            permissionsList = new ArrayList<>();
            permissionsList.addAll(Arrays.asList(MULTIPLE_PERMISSIONS));
            askForPermissions(permissionsList);
           /* if (checkForPermission(MULTIPLE_PERMISSIONS)) {
                try {
                    captureVideo();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error " + e);
                }
            }*/
        });

        binding.imageBtnNext.setOnClickListener(view -> {
            if(binding.radioGrp.getCheckedRadioButtonId()!=-1) {
                Intent intent = new Intent(RecordBowling.this, EnterName.class);
                startActivity(intent);
            }else
                Toast.makeText(this, "Please select one bowling video!", Toast.LENGTH_SHORT).show();
        });

        binding.radioGrp.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radio1:
                    binding.imageViewThumb1.setImageAlpha(200);
                    binding.imageViewThumb2.setImageAlpha(255);
                    break;
                case R.id.radio2:
                    binding.imageViewThumb2.setImageAlpha(200);
                    binding.imageViewThumb1.setImageAlpha(255);
                    break;
                default:
                    break;
            }
        });

    }



    private void captureVideo() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestCode = VIDEO_CAPTURE;
        if (intent.resolveActivity(getPackageManager()) != null)
            activityResultLauncher.launch(intent);
    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Videos");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        videoUrl = "file:" + image.getAbsolutePath();
        return image;
    }


    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
        /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
        which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }


    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are need to be allowed to use this app without any problems.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (requestCode == VIDEO_CAPTURE && result.getResultCode() == RESULT_OK) {
                        Uri videoUri = result.getData().getData();
                        //videoView.setVideoURI(videoUri);
                        File myFile = saveToLoc(videoUri);
                        videoUrl = myFile.getAbsolutePath();
                        Glide.with(RecordBowling.this)
                                .load(Uri.fromFile(myFile))
                                .into(binding.imageViewThumb2);
                        SharedPreferencesHandler.setStringValues(RecordBowling.this, KEY_URI, videoUrl);
                        Toast.makeText(RecordBowling.this, "Video saved to:\n" + videoUrl, Toast.LENGTH_LONG).show();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(RecordBowling.this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordBowling.this, "Failed to record video", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private File saveToLoc(Uri videoUri) {
        try {
            AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(videoUri, "r");
            File file = createVideoFile();
            FileOutputStream fos = new FileOutputStream(file);
            FileInputStream fis = videoAsset.createInputStream();

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
            return file;
        } catch (Exception ex) {
            Log.e(TAG, "Error " + ex);
        }
        return new File(videoUri.toString());
    }

    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(MULTIPLE_PERMISSIONS[i])) {
                                    permissionsList.add(MULTIPLE_PERMISSIONS[i]);
                                } else if (!hasPermission(RecordBowling.this, MULTIPLE_PERMISSIONS[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                //All permissions granted. Do your stuff 🤞
                                try {
                                    captureVideo();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "Error " + e);
                                }
                            }
                        }
                    });


}