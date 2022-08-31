package com.jaay.docReader;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.annotation.Nullable;
import android.support.annotation.NonNull;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.provider.Settings;
import android.net.Uri;
import android.content.DialogInterface;


public class MainActivity extends AppCompatActivity {
    
    private final int STORAGE_PERMISSION_CODE = 1;
    
    
    
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		//Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);
        
        
        navigationView = findViewById(R.id.navigation);
        
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new AllFragment()).commit();
        navigationView.setSelectedItemId(R.id.allPDFs);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                Fragment fragment = null;
                 switch (item.getItemId()){
                     case R.id.recentPDFs:
                         fragment = new RecentFragment();
                         break;
                     case R.id.allPDFs:
                         fragment = new AllFragment();
                         break;
                         
                     case R.id.favouritePDFs:
                         fragment = new FavouriteFragment();
                         break;
                     case R.id.searchPDFs:
                         fragment = new SearchFragment();
                         break;
                     case R.id.newPDFs:
                         fragment = new NewFragment();
                         break;
                         
                 }
                 getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, fragment).commit();
                 return true;
                 }
        });
    }
    
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) 
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {

                //Now further we check if used denied permanently or not
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                                                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 1. The user has temporarily denied permission.
                    Toast.makeText(MainActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();

                } else {
                    // 2. Permission has been denied.
                    // From here, you can access the setting's page.

                    new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Permission Required")
                        .setMessage("This permission was already declined by you. Please open settings, go to \"Permissions\", and allow the permission.")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                MainActivity.this.startActivity(i);
                            }
                        
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Please allow this permission!", Toast.LENGTH_SHORT).show();
                        
                        }
                        
                    })
                    .create().show();
                }

            }
        }
    }
    
}
