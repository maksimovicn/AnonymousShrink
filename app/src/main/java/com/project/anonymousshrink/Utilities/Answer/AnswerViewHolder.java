package com.project.anonymousshrink.Utilities.Answer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Home.QuestionClickListener;


public class AnswerViewHolder extends RecyclerView.ViewHolder{


    public TextView tvDate;
    public TextView tvName;
    public TextView tvAnswers;




    public AnswerViewHolder(View itemView) {
        super(itemView);
        tvDate=(TextView)itemView.findViewById(R.id.tvDateAnswer);
        tvName=(TextView)itemView.findViewById(R.id.tvNameAnswer);
        tvAnswers=(TextView)itemView.findViewById(R.id.tvAnswers);
    }

}
