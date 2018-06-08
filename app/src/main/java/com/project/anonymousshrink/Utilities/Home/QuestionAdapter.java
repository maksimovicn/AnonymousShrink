package com.project.anonymousshrink.Utilities.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.anonymousshrink.Activities.AnswerQuestion;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Model.Questions;
import com.project.anonymousshrink.Utilities.Model.UsersData;

import java.util.ArrayList;

/**
 * Created by asadmehran on 02/03/2018.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

    private Context context;
    private ArrayList<Questions> questionsArrayList;

    public QuestionAdapter(Context context, ArrayList<Questions> questionsArrayList) {
        this.context = context;
        this.questionsArrayList = questionsArrayList;
    }


    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view, null);
        QuestionViewHolder questionViewHolder = new QuestionViewHolder(view);
        return questionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {

        holder.tvQuestion.setText(questionsArrayList.get(position).getQuestion());
        holder.tvAnswersPosted.setText(questionsArrayList.get(position).getAnswers());
        holder.tvDate.setText(questionsArrayList.get(position).getDate());

        holder.setQuestionClickListener(new QuestionClickListener() {
            @Override
            public void onQuestionClick(View view, int position) {
                Intent intent=new Intent(context,AnswerQuestion.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

}
