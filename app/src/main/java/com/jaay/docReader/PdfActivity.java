package com.jaay.docReader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
import android.net.Uri;
import java.io.IOException;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.content.Context;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.MotionEvent;
import android.widget.LinearLayout;




public class PdfActivity extends AppCompatActivity implements View.OnClickListener{
    
    String filePath = "";
    ImageView pdfView;
    TextView pageCountTV;
    Button nextButton, prevButton;
    int defaultPage = 0;
    int currentPage;
    int pageCount;
    File file;
    Bitmap bitMap;
    float currentZoomLevel= 12;
    OnSwipeTouchListener onSwipeTouchListener;
    LinearLayout linearLayout;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = (ImageView) findViewById(R.id.pdfview);
        //pageCountTV = (TextView) findViewById(R.id.pdfcountTV);
        prevButton = (Button) findViewById(R.id.prevPageButton);
        nextButton = (Button) findViewById(R.id.nextPageButton);
        linearLayout = (LinearLayout) findViewById(R.id.pdfLinearLayout);
        
        filePath =  getIntent().getStringExtra("filePath");
        
        file = new File(filePath);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        pdfView.setOnClickListener(this);
        //getPd
        try{
            openPDF(file,defaultPage);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        
        
        onSwipeTouchListener = new OnSwipeTouchListener(PdfActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(PdfActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                try{
                    if (defaultPage >= pageCount-1){
                        Toast.makeText(PdfActivity.this,
                                       "This is the last page. ",
                                       Toast.LENGTH_SHORT).show();
                        openPDF(file, pageCount-1);
                    }else{
                        openPDF(file, defaultPage++);
                    }
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
                Toast.makeText(PdfActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                try{
                    
                    if(defaultPage == 0){
                        Toast.makeText(PdfActivity.this,
                                       "This is the first page",
                                       Toast.LENGTH_SHORT).show();
                        openPDF(file, 0);

                    }else{
                        openPDF(file, defaultPage--);
                    }
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
                Toast.makeText(PdfActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(PdfActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
            
            /*public void onClick(){
                
            }*/
        };
       
        
    }
    
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev); 
        return super.dispatchTouchEvent(ev);   
    }
    
    
    
    /*public int getCurrentPage(){
        return currentPage;
    }*/
    
    
    public void openPDF(File file, int pageIndex) throws IOException {

        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = ParcelFileDescriptor.open(
            file, ParcelFileDescriptor.MODE_READ_ONLY);
            
        //min. API Level 21
        PdfRenderer pdfRenderer = null;
        pdfRenderer = new PdfRenderer(fileDescriptor);

        pageCount = pdfRenderer.getPageCount();
        //Toast.makeText(this,
                       //"pageCount = " + pageCount,
                       //Toast.LENGTH_LONG).show();

        //Display page 0
        
        //currentPage = defaultPage;
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(defaultPage);
        
        currentPage = rendererPage.getIndex();
        int rendererPageWidth = (int) getResources().getDisplayMetrics().densityDpi* rendererPage.getWidth() / 72* (int)currentZoomLevel/40;
        int rendererPageHeight =(int) getResources().getDisplayMetrics().densityDpi*rendererPage.getHeight()/72 *(int) currentZoomLevel/64;
        //int rendererPageWidth = rendererPage.getWidth();
        //int rendererPageHeight = rendererPage.getHeight();
        bitMap = Bitmap.createBitmap(
            rendererPageWidth,
            rendererPageHeight,
            Bitmap.Config.ARGB_8888);
        rendererPage.render(bitMap, null, null,
                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        pdfView.setImageBitmap(bitMap);
        rendererPage.close();

        pdfRenderer.close();
        fileDescriptor.close();
    }
    
    
    
    
    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
    
    //PdfRenderer.
    //handling the next and prev buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.prevPageButton: {
                
                try{
                    if(defaultPage == 0){
                        Toast.makeText(this,
                        "This is the first page",
                        Toast.LENGTH_SHORT).show();
                        openPDF(file, 0);
                        
                    }else{
                        openPDF(file, defaultPage--);
                    }
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
                    break;
                }

            case R.id.nextPageButton: {
                try{
                    if (defaultPage >= pageCount-1){
                        Toast.makeText(this,
                        "This is the last page. ",
                        Toast.LENGTH_SHORT).show();
                        openPDF(file, pageCount-1);
                    }else{
                        openPDF(file, defaultPage++);
                    }
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
               
                    break;
                }
            case R.id.pdfview: {
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    // Its visible
                    linearLayout.setVisibility(View.GONE);
                } else {
                    // Either gone or invisible
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    
}
