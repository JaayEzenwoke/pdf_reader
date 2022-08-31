package com.jaay.docReader;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.os.Bundle;
import android.content.Intent;
import java.util.ArrayList;
import java.io.File;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;



public class SplashActivity extends AppCompatActivity{
    
    Handler handler;
    ArrayList arrayList;
    String loadedString = "pdf";
    
    
    
    public ArrayList<File> getPdfList(File file){
        
        ArrayList<File> pdfArrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                pdfArrayList.addAll(getPdfList(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".pdf")){
                    pdfArrayList.add(singleFile);
                }
            }
        }
        
        return pdfArrayList;
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    /*Intent intentForPdfData = new Intent("INTENT_NAME").putExtra(loadedString, getPdfList(Environment.getExternalStorageDirectory()));
                    LocalBroadcastManager.getInstance(SplashActivity.this).sendBroadcast(intentForPdfData);*/
                    
                    //arrayList = getPdfList(Environment.getExternalStorageDirectory());
                    finish();
                }
            },3000);
        }
        
    
}
