package com.mudez.mbf.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mudez.mbf.R;
import com.mudez.mbf.models.AgendaItemObject;
import com.mudez.mbf.ui.pages.CommentsPage;

import java.util.List;

public class AgendaViewAdapter extends RecyclerView.Adapter<AgendaViewAdapter.ViewHolder> {

   private List<AgendaItemObject> agendaItemObjectList;
   String agendaId;

    private Context context;
   public AgendaViewAdapter(Context context, List<AgendaItemObject> agendaItemObjectList1,String agendaId){
        this.agendaItemObjectList = agendaItemObjectList1;
        this.agendaId = agendaId;
        this.context = context;
    }

    public List<AgendaItemObject> getAgendaItemObjectList() {
        return agendaItemObjectList;
    }

    public void setAgendaItemObjectList(List<AgendaItemObject> agendaItemObjectList) {
        this.agendaItemObjectList = agendaItemObjectList;
    }

    @NonNull
    @Override
    public AgendaViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.agenda_view_item,parent,false);
        return new AgendaViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull AgendaViewAdapter.ViewHolder holder, final int position) {

        holder.getTitleView().setText(agendaItemObjectList.get(position).getTitle());
        if(agendaItemObjectList.get(position).getId() == null){
            holder.getComment().setVisibility(View.GONE);
        }
        holder.getTimeView().setText(agendaItemObjectList.get(position).getFrom().concat("-").concat(agendaItemObjectList.get(position).getTo()));
       if(agendaItemObjectList.get(position).getSubTitle() != null && !agendaItemObjectList.get(position).getSubTitle().isEmpty() ){

           holder.getSubTitle().setText(agendaItemObjectList.get(position).getSubTitle());

       }else{

           holder.getSubTitle().setVisibility(View.GONE);
       }
        holder.getMaterialCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getHiddenView().getVisibility() == View.GONE){

                    TransitionManager.beginDelayedTransition(holder.getMaterialCardView(),new AutoTransition());
                    holder.getArrow().setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    holder.getHiddenView().setVisibility(View.VISIBLE);
                }else{
                    TransitionManager.beginDelayedTransition(holder.getMaterialCardView(),new AutoTransition());
                    holder.getArrow().setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    holder.getHiddenView().setVisibility(View.GONE);
                }
            }
        });

       holder.getComment().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context.getApplicationContext(), CommentsPage.class);
               Bundle bundle = new Bundle();
               bundle.putString("doc_id",agendaItemObjectList.get(position).getId());
               bundle.putString("agenda_id",agendaId);
               intent.putExtra("bundle",bundle);
               context.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return agendaItemObjectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }

        public ImageView getArrow() {
            return arrow;
        }

        public TextView getTimeView() {
            return timeView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getSubTitle() {
            return subTitle;
        }

        public RelativeLayout getHiddenView() {
            return hiddenView;
        }

        public TextView getComment() {
            return comment;
        }

        MaterialCardView materialCardView;
       ImageView arrow;
       TextView timeView;
       TextView titleView;
       TextView subTitle;
       RelativeLayout hiddenView;
       TextView comment;



         ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.card);
            arrow = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.title);
            subTitle = itemView.findViewById(R.id.subTitle);
            hiddenView = itemView.findViewById(R.id.hiddenView);
            timeView = itemView.findViewById(R.id.time);
            comment = itemView.findViewById(R.id.comments);

        }
    }
}
