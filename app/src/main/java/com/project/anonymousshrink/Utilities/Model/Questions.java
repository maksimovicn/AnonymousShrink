package com.project.anonymousshrink.Utilities.Model;

/**
 * Created by asadmehran on 02/03/2018.
 */

public class Questions {

    public String date;
    public String question;
    public String answers;
    public String visibility;
    public String uid;
    public String qid;
    public String posted_by;

    public Questions() {
    }

    public Questions(String date, String question, String answers, String visibility, String uid, String posted_by, String qid) {
        this.date = date;
        this.question = question;
        this.answers = answers;
        this.visibility = visibility;
        this.uid = uid;
        this.posted_by = posted_by;
        this.qid = qid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getDate() {
        return date;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswers() {
        return answers;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getUid() {
        return uid;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }
}
