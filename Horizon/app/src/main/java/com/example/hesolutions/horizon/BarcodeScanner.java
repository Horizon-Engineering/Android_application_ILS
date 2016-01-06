package com.example.hesolutions.horizon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.BoringLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.BiMap;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.HashMap;



public class BarcodeScanner extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private Button scanButton;
    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    EditText Inputname;
    Button Saveid;
    String accountname;
    Button cancelcam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        Inputname = (EditText)findViewById(R.id.Inputname);
        Saveid = (Button)findViewById(R.id.Saveid);
        Inputname.setVisibility(View.GONE);
        Saveid.setVisibility(View.GONE);
        cancelcam = (Button)findViewById(R.id.cancelcam);
        initControls();
        cancelcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeScanned = false;
                previewing = false;
                releaseCamera();
                Intent startNewActivityIntent = new Intent(BarcodeScanner.this, AdminPage.class);
                startActivity(startNewActivityIntent);
            }
        });

    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(BarcodeScanner.this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (Button) findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();


                    RunComplete(scanResult);


                    //showAlertDialog(scanResult);

                  /*  Toast.makeText(BarcodeScanner.this, scanResult,
                            Toast.LENGTH_SHORT).show();*/

                    barcodeScanned = true;

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    public void RunComplete(final String message)
    {
        final BiMap<String,String> bimap;
        bimap = DataManager.getInstance().getdevice();
        //Toast.makeText(BarcodeScanner.this, message,Toast.LENGTH_SHORT).show();
        //==========Device key: message(device ID), value: device name
        if(bimap.get(message)== null) {
            Inputname.setVisibility(View.VISIBLE);
            Saveid.setVisibility(View.VISIBLE);
            Saveid.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    accountname = Inputname.getText().toString();

                    if (accountname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No name is given"
                                , Toast.LENGTH_LONG).show();

                    } else if (bimap.inverse().get(accountname)!= null) {
                        Inputname.setText("");
                        Toast.makeText(getApplicationContext(), "Name already exists, please change the name of the device"
                                , Toast.LENGTH_LONG).show();

                    } else {

                        bimap.put(message, accountname);
                        DataManager.getInstance().setdevice(bimap); //store back to datamanger

                        Inputname.setText("");
                        Inputname.setVisibility(View.GONE);
                        Saveid.setVisibility(View.GONE);
                    }


                }
            });

        }else {
            String Cname = bimap.get(message);
            Toast.makeText(getApplicationContext(), "Device already exists, the name of the device is " + Cname
                    , Toast.LENGTH_LONG).show();
        }
    }

/*
    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }*/

}
