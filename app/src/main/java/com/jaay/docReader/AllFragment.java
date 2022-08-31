package com.jaay.docReader;


import android.support.annotation.NonNull;
import android.content.Context;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.provider.MediaStore;
import android.database.Cursor;
import java.sql.Array;
import java.util.List;
import java.util.ArrayList;
import android.webkit.MimeTypeMap;
import android.os.Build;
import android.util.Log;
import android.nfc.Tag;
import java.io.File;
import android.os.Environment;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;



public class AllFragment extends Fragment implements OnPdfSelectListener {
    View view;
    boolean isLoaded;
    private static final String TAG = "MyActivity";
    ArrayList<File> pdfList;
    
    
    
    private final int STORAGE_PERMISSION_CODE = 1;
    String loadedString = "pdf";
    ArrayList<File> listOfPdfs;
    // public final ArrayList<File>
    File allPdfFiles;

    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
    
    //com.
    public void checkPermission(){
        
        if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(mActivity)
                    .setTitle("Permission needed")
                    .setMessage("Allow "+getResources().getString(R.string.app_name)+" to access your storage?")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            ActivityCompat.requestPermissions(mActivity,
                                                              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    } 
                )
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        Toast.makeText(mActivity, "Please allow this permission!", Toast.LENGTH_SHORT).show();
                    }
                    
                })
                .create().show();
            } else {
                ActivityCompat.requestPermissions(mActivity,
                                                  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }
        
    }
    
    
    //checkPermission();
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.AllFragment, container, false);
        //pdfList = getPdfList();
        //buildData();
        //LinearLayoutManager
        //pdfList = new String[] {"one", "two", "three", "fou4", "five", "six"};
        //System.out.println(getPdfList());
        //getPdfList();
        checkList();
        initRecyclerView(view);
        return view;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        checkPermission();
        //checkList();
        //isLoaded = false;
        listOfPdfs = getPdfList(Environment.getExternalStorageDirectory());
        isLoaded = true;
        checkList();
        /*if(isLoaded == false){
            listOfPdfs = getPdfList(Environment.getExternalStorageDirectory());
        }else{
        }*/
        //listOfPdfs = getPdfList(Environment.getExternalStorageDirectory());
        
        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));
    }
        
       /* public BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                listOfPdfs = (ArrayList<File>) intent.getSerializableExtra(loadedString);
                //allPdfFiles = new File(listOfPdfs);
            }
        };*/
         //pdfList.addAll(getPdfList(Environment.getExternalStorageDirectory()));
    
    
    
    //RecyclerView
    public void initRecyclerView(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allPdfsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AllPdfAdapter adapter = new AllPdfAdapter(getActivity(), listOfPdfs, this);
        recyclerView.setAdapter(adapter);
        
        
    }
    
    
   
   
    
    /*public ArrayList<String> getPdfList() {
        pdfList = new ArrayList<>();
        Uri collection;
        
        final String[] projection = new String[]{
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE,
        };

        final String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";

        final String selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?";

        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        final String[] selectionArgs = new String[]{mimeType};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else{
            collection = MediaStore.Files.getContentUri("internal");
        }


        try (Cursor cursor = getActivity().getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
            assert cursor != null;

            if (cursor.moveToFirst()) {
                int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                //ArrayList pdfList = new ArrayList<>();
                do {
                    pdfList.add((cursor.getString(columnName)));
                    System.out.println(cursor.getString(columnData));
                    Log.d(TAG, "getPdf: " + cursor.getString(columnData));
                    //you can get your pdf files
                } while (cursor.moveToNext());
            }
        }
        return pdfList;
    }*/
    
    
    public ArrayList<File> getPdfList(File file){
        
        ArrayList<File> pdfArrayList = new ArrayList<>();
        File[] files = file.listFiles();
        //isLoaded =false;
        for (File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                pdfArrayList.addAll(getPdfList(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".pdf")){
                    pdfArrayList.add(singleFile);
                    //isLoaded = true;
                }
            }
        }
       /* if (isLoaded == false){
            
        }
        isLoaded = true;*/
        return pdfArrayList;
        
    }
    
    public ArrayList<File> checkList(){
       // listOfPdfs;
        if (isLoaded == false){
            listOfPdfs = getPdfList(Environment.getExternalStorageDirectory());
            return listOfPdfs;
        }else{
            return listOfPdfs;
        }
       // return listOfPdfs;
    }

    
    
    
    @Override
    public void onPdfSelected(File file) {
        startActivity(new Intent(getActivity(), PdfActivity.class).putExtra("filePath", file.getAbsoluteFile().toString()));
    }

      
}
