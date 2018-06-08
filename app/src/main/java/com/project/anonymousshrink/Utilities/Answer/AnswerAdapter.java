package com.project.anonymousshrink.Utilities.Answer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.anonymousshrink.Activities.AnswerQuestion;
import com.project.anonymousshrink.Activities.ProfileView;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Home.QuestionClickListener;
import com.project.anonymousshrink.Utilities.Home.QuestionViewHolder;
import com.project.anonymousshrink.Utilities.Model.Answers;
import com.project.anonymousshrink.Utilities.Model.Questions;

import java.util.ArrayList;

/**
 * Created by asadmehran on 02/03/2018.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder> {

    private Context context;
    private ArrayList<Answers> answersArrayList;


    public AnswerAdapter(Context context, ArrayList<Answers> answersArrayList) {
        this.context = context;
        this.answersArrayList = answersArrayList;
    }


    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_view, null);
        AnswerViewHolder answerViewHolder = new AnswerViewHolder(view);
        return answerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, final int position) {

        holder.tvName.setText(answersArrayList.get(position).getName());
        holder.tvAnswers.setText(answersArrayList.get(position).getAnswer());
        holder.tvDate.setText(answersArrayList.get(position).getDate());

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context, ProfileView.class);
               intent.putExtra("uid",answersArrayList.get(position).getUid());
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answersArrayList.size();
    }

}
