package com.example.hesolutions.horizon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {


    Button scannerButton;
    Button AccessPermit;
    Button Logout;
    Button SetDevice;
    Button Grouping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //code to make the full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

     /*   scannerButton = (Button) findViewById(R.id.scannerButton);
        AccessPermit = (Button) findViewById(R.id.AccessPermit);
        Logout = (Button) findViewById(R.id.Logout);
        SetDevice = (Button) findViewById(R.id.SetDevice);
        Grouping = (Button) findViewById(R.id.Grouping);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdminPage.this.startActivityForResult(new Intent(AdminPage.this, CaptureActivity.class), 0);
                AdminPage.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        AccessPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(), AccessPermission.class);
                startActivity(intent1);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(), HomePage.class);
                startActivity(intent1);
            }
        });
        SetDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(), TotalDevice.class);
                startActivity(intent1);
            }
        });
        Grouping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(), GroupActivity.class);
                startActivity(intent1);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("****************" + "HHHHHHHHHHHHHHHHHHHH" + " Right place");
        if (requestCode != 0) {
            Toast.makeText(this, getResources().getString(R.string.scanerfail), Toast.LENGTH_LONG).show();
        } else if (resultCode == -1) {
            String contents = data.getExtras().getString("result");
            System.out.println("****************"+contents + " Right place");
            boolean boolresu = false;
            if (contents != null && contents.length() == 7) {
                int devtype;
                int subdevtype = 0;
                if (Integer.parseInt(contents.substring(1, 2)) == 2 && Integer.parseInt(contents.substring(0, 1)) == 5) {
                    subdevtype = 2;
                    devtype = 5;
                } else if (Integer.parseInt(contents.substring(1, 2)) != 1) {
                    devtype = Integer.parseInt(contents.substring(0, 2));
                } else {
                    devtype = Integer.parseInt(contents.substring(0, 1));
                }
                String deviceaddress = contents.substring(2, contents.length());
                if (Integer.parseInt(deviceaddress) <= 65500) {
                    Device mDevice = new Device();
                    if (subdevtype > 0) {
                        try {
                            mDevice.setSubDeviceType((short) subdevtype);
                        } catch (Exception e) {
                            Toast.makeText(this, getResources().getString(R.string.scanerfail), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    mDevice.setChannelInfo((short) 1);
                    mDevice.setDeviceType((short) devtype);
                    mDevice.setDeviceAddress((short) Integer.parseInt(deviceaddress));

                    Intent toaddintent = new Intent(this, ZxingToAddDeviceActivity.class);
                    toaddintent.putExtra("mDevice", mDevice);
                    startActivity(toaddintent);

                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    contents = null;
                    finish();
                    boolresu = true;
                }
            }
            if (contents != null && !boolresu) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("QR code error");
                alertDialog.setPositiveButton("Scan Another", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AdminPage.this.startActivityForResult(new Intent(AdminPage.this, CaptureActivity.class), 0);
                        AdminPage.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                alertDialog.show();

            }
        }
    }

        /*
        GridView gridView = (GridView)findViewById(R.id.gridView);

        ArrayList<String> numberlist;
        numberlist = DataManager.getInstance().getGrid();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, numberlist);
        gridView.setAdapter(adapter);
*/
    }

}


