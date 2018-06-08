package com.project.anonymousshrink.Utilities.Home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.anonymousshrink.R;


public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView tvDate;
    public TextView tvQuestion;
    public TextView tvAnswersPosted;

    QuestionClickListener questionClickListener;


    public QuestionViewHolder(View itemView) {
        super(itemView);
        tvDate=(TextView)itemView.findViewById(R.id.tvDate);
        tvQuestion=(TextView)itemView.findViewById(R.id.tvQuestions);
        tvAnswersPosted=(TextView)itemView.findViewById(R.id.tvAnswersPosted);
        itemView.setOnClickListener(this);
    }

    public void setQuestionClickListener(QuestionClickListener questionClickListener) {
        this.questionClickListener = questionClickListener;
    }

    @Override
    public void onClick(View v) {
        questionClickListener.onQuestionClick(v,getLayoutPosition());
    }
}
