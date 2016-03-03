package com.example.hesolutions.horizon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.LinearLayout;



public class PaintPage extends Activity{
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, loadBtn, modeBtn;
    private DrawingView drawView;
    private LinearLayout drawingpart;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    String sectorsave = "";
    ArrayList<String> sectorlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_page);
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawView = (DrawingView)findViewById(R.id.drawing);
        drawingpart = (LinearLayout)findViewById(R.id.drawingpart);
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog brushDialog = new Dialog(PaintPage.this);
                brushDialog.setTitle("Eraser size:");
                brushDialog.setContentView(R.layout.brush_chooser);
                ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(true);
                        drawView.setBrushSize(smallBrush);
                        brushDialog.dismiss();
                    }
                });
                ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(true);
                        drawView.setBrushSize(mediumBrush);
                        brushDialog.dismiss();
                    }
                });
                ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(true);
                        drawView.setBrushSize(largeBrush);
                        brushDialog.dismiss();
                    }
                });
                brushDialog.show();
            }
        });

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        drawBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog brushDialog = new Dialog(PaintPage.this);
                brushDialog.setTitle("Brush size:");
                brushDialog.setContentView(R.layout.brush_chooser);
                ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(smallBrush);
                        drawView.setLastBrushSize(smallBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });
                ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(mediumBrush);
                        drawView.setLastBrushSize(mediumBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });

                ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(largeBrush);
                        drawView.setLastBrushSize(largeBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });
                brushDialog.show();
                drawView.setBrushSize(mediumBrush);
            }
        });

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(PaintPage.this);
                newDialog.setTitle("New drawing");
                newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
                newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        drawView.startNew();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                newDialog.show();
            }
        });

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(PaintPage.this);
                saveDialog.setTitle("Save drawing");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        drawView.setDrawingCacheEnabled(true);
                        if (!sectorsave.equals("")) {
                            writedata(drawView.getDrawingCache(), sectorsave + ".png");
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                            savedToast.show();
                            drawView.destroyDrawingCache();
                        }

                    }
                });
                saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                saveDialog.show();

            }
        });

        loadBtn = (ImageButton)findViewById(R.id.load_btn);
        loadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        modeBtn = (ImageButton)findViewById(R.id.mode_btn);
        modeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog modeDialog = new Dialog(PaintPage.this);
                modeDialog.setTitle("Brush mode:");
                modeDialog.setContentView(R.layout.modes_chooser);
                ImageButton penmode = (ImageButton)modeDialog.findViewById(R.id.pen_mode);
                penmode.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(false);
                        drawView.setMode(1);
                        modeDialog.dismiss();
                    }
                });
                ImageButton recmode = (ImageButton)modeDialog.findViewById(R.id.rec_mode);
                recmode.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(false);
                        drawView.setMode(2);
                        modeDialog.dismiss();
                    }
                });
                ImageButton cirmode = (ImageButton)modeDialog.findViewById(R.id.cir_mode);
                cirmode.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(false);
                        drawView.setMode(3);
                        modeDialog.dismiss();
                    }
                });
                modeDialog.show();
            }
        });

        LoadList();
    }
    public void paintClicked(View view){
        //use chosen color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if(view!=currPaint){
//update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                drawView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked the blueprint",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
    public static void writedata(Bitmap bitmap, String filename) {
        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Horizon/Bitmap");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, filename);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void LoadList()
    {
        HashMap<String, HashMap> sector = DataManager.getInstance().getsector();
        for (Map.Entry<String, HashMap> entry : sector.entrySet()) {
            HashMap<String, ArrayList> value = entry.getValue();
            for (Map.Entry<String, ArrayList> entrys : value.entrySet()) {
                String sectorname = entrys.getKey();
                if (!sectorlist.contains(sectorname)) {
                    sectorlist.add(sectorname);
                }
            }
        }
        ListView sectorlistlayout = (ListView)findViewById(R.id.sectorlistlayout);
        if (!sectorlist.isEmpty()) {
            MyAdapter adapter = new MyAdapter(this, sectorlist);
            sectorlistlayout.setAdapter(adapter);
            sectorlistlayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < sectorlist.size(); i++) {
                        showContent(view);
                        if (position == i) {
                            parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.buttonclicked));
                        } else {
                            parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.buttonunclick));
                        }
                    }
                }
            });
        }
    }
    public class MyAdapter extends ArrayAdapter<String> {

        private Activity context;
        private ArrayList<String> devicelist;

        public MyAdapter(Activity context, ArrayList<String> zoneList) {
            super(context, R.layout.devicelistadmin, zoneList);
            this.context = context;
            this.devicelist = zoneList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.devicelistadmin, null);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            txtTitle.setText(devicelist.get(position));
            return rowView;
        }

    }
    public void showContent(View view)
    {
        sectorsave = ((TextView) view).getText().toString();
        drawingpart.setVisibility(View.VISIBLE);
        drawView.startNew();
        drawView.setBackground(null);
    }
}
