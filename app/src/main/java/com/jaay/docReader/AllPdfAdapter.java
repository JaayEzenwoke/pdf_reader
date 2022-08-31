package com.jaay.docReader;


import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import android.view.View.OnClickListener;
import android.content.Context;


public class AllPdfAdapter extends RecyclerView.Adapter<AllPdfAdapter.My_ViewHolder>
{
    public ArrayList<File> data;
    public OnPdfSelectListener listener;
    public Context context;
    //final int gPosition;
    
    public AllPdfAdapter(Context context, ArrayList<File>data, OnPdfSelectListener listener){
        this.data = data;
        this.listener = listener;
        this.context = context;
    }
    
    
    @Override
    public AllPdfAdapter.My_ViewHolder onCreateViewHolder(ViewGroup p1, int p2)
    {
        LayoutInflater li = LayoutInflater.from(p1.getContext());
        View v = li.inflate(R.layout.allList,p1,false);
        
        // TODO: Implement this method
        return new My_ViewHolder(v);
    }
    
    
    @Override
    public void onBindViewHolder(AllPdfAdapter.My_ViewHolder holder, final int position)
    {
        // TODO: Implement this method
        String title = data.get(position).getName();
        //String newTitle = title.toString();
        //final int newPostion = position;
        holder.ttt.setText(title);
        holder.ttt.setSelected(true);
        holder.allList.setOnClickListener(new View.OnClickListener(){
            //newPostion 
            //final int newPostion = position;
            //positon = gPosition;
            @Override
            public void onClick(View v){
                
                listener.onPdfSelected(data.get(position));
            }
        });
        //holder.allList.setOnTouchListener(V
    }
    
    
    @Override
    public int getItemCount()
    {
        // TODO: Implement this method
        return data.size();
    }
    
    
    public class My_ViewHolder extends RecyclerView.ViewHolder{
        
        ImageView iii;
        TextView ttt;
        LinearLayout allList;
        
        public My_ViewHolder (View v){
            super(v);
            
            iii = (ImageView) v.findViewById(R.id.img);
            ttt = (TextView) v.findViewById(R.id.title);
            allList = (LinearLayout) v.findViewById(R.id.allListLayout);
            allList.setClickable(true);
            /*allList.setOnClickListener( new View.OnClickListener() {
                @Override
                public void OnClick(View v){
                    listener.onPdfSelected(data.get(position));
                }
            });*/
        }
        
    }
    
}
