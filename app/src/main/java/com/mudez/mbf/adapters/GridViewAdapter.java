package com.mudez.mbf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.mudez.mbf.R;
import com.mudez.mbf.models.GridObject;

import java.util.ArrayList;

public class GridViewAdapter  extends BaseAdapter {
   private Context context;
   private ArrayList<GridObject> gridObjects;

    public GridViewAdapter(Context context,ArrayList<GridObject> gridObjectArrayList){
        this.context = context;
        this.gridObjects = gridObjectArrayList;

    }

   public ArrayList<GridObject> getGridObjects(){
        return gridObjects;
    }


    @Override
    public int getCount() {
        return gridObjects.size();
    }

    @Override
    public GridObject getItem(int position) {
        return gridObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_view_item,parent,false);
        }

        MaterialCardView materialCardView = convertView.findViewById(R.id.card);
        TextView textView = convertView.findViewById(R.id.title);
        if(gridObjects.get(position).isTapped()){
            materialCardView.setStrokeColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            materialCardView.setStrokeColor(context.getResources().getColor(R.color.white_text_color));
        }
        textView.setText(gridObjects.get(position).getTitle());

        return convertView;
    }
}
