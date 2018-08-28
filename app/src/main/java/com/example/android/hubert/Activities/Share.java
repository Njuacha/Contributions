package com.example.android.hubert.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hubert on 4/28/18.
 */

class Share  {
    private final String mFileName;
    private final String mContent;
    private File mFile;
    private final Context mContext;
    public final static String FILE_EXTENSION = ".txt";

    private static final String DATE_FORMAT = "dd/MM/yyy";
    // Date formatter
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    public static final String date = dateFormat.format(new Date());
    private final Intent mShareIntent =  new Intent(Intent.ACTION_SEND);

    public Share(String mFileName, String mContent, Context mContext){
        this.mFileName = mFileName;
        this.mContent = mContent;
        this.mContext = mContext;
        createFile();
        shareFile();
    }

    private void createFile(){
        try{
            File root = new File(Environment.getExternalStorageDirectory(),"NkapJik");
            if(!root.exists()){
                root.mkdir();
            }

            mFile = new File(root, mFileName);
            FileWriter writer = new FileWriter(mFile);
            writer.append(mContent);
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void shareFile(){
        mShareIntent.setType("application/txt");
        mShareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext,
                "com.example.android.hubert.fileProvider", mFile));
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT,"Sharing mFile...");
        mShareIntent.putExtra(Intent.EXTRA_TEXT,"Share mFile");
    }

    public void show(){
        mContext.startActivity(Intent.createChooser(mShareIntent,"Share"));
    }
}

