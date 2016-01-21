package com.homa.hls.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private final Context context;
    private boolean createDatabase;
    private String mDataBaseName;
    public final File mFile;
    private String mSrcDataBaseName;

    public DataBaseHelper(Context context, String dataBaseName, String srcDataBaseName) {
        super(context, dataBaseName, null, DATABASE_VERSION);
        this.createDatabase = false;
        this.mDataBaseName = dataBaseName;
        this.mSrcDataBaseName = srcDataBaseName;
        this.context = context;
        System.out.println("**** mDataBaseName is " + mDataBaseName);
        this.mFile = context.getDatabasePath(this.mDataBaseName);
        System.out.println("**** mfile absolute path " + this.mFile.getAbsolutePath());
        System.out.println("**** mfile exists " + this.mFile.exists());
        if (!this.mFile.exists()) {
            this.createDatabase = true;
        }
        System.out.println("**** createDAtabase is " + createDatabase);
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase writableDatabase;
        SQLiteDatabase database = super.getWritableDatabase();
        this.createDatabase = true;
        if (this.createDatabase) {
            System.out.println("**** about to copy database ");
            this.createDatabase = false;
            try {
                database = copyDatabase(database);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file = this.context.getFileStreamPath("temp.txt");
            if (file.exists()) {
                try {
                    InputStream input = new FileInputStream(file);
                    OutputStream output = new FileOutputStream(this.mFile);
                    copy(input, output);
                    input.close();
                    output.close();
                    file.delete();
                    writableDatabase = super.getWritableDatabase();
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        writableDatabase = database;
        return writableDatabase;
    }

    private SQLiteDatabase copyDatabase(SQLiteDatabase database) throws IOException {
        System.out.println("**** mSrcDataBaseName is " + mSrcDataBaseName.toString());
        System.out.println("*** context1111111111111111111 = " + this.context.getAssets().toString());
        InputStream input = this.context.getAssets().open(this.mSrcDataBaseName);
        System.out.println("*** context2222222222222222222 = " + this.context.toString());
        System.out.println("**** this.context.getAssets().open is called ");
        OutputStream output = new FileOutputStream(this.mFile);
        System.out.println("**** FileOutputStream is called ");
        copy(input, output);
        System.out.println("**** copy is called ");
        input.close();
        System.out.println("**** input.close() is called ");
        output.close();
        System.out.println("**** output.close() is called ");
        return super.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
        int count = 0;
        while (true) {
            int n = input.read(buffer);
            if (-1 == n) {
                return count;
            }
            output.write(buffer, 0, n);
            count += n;
        }
    }
}
