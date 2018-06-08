package com.project.anonymousshrink.Utilities.Model;

/**
 * Created by asadmehran on 02/03/2018.
 */

public class Answers {

    public String uid;
    public String name;
    public String answer;
    public String date;

    public Answers() {
    }

    public Answers(String uid, String name, String answer, String date) {
        this.uid = uid;
        this.name = name;
        this.answer = answer;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDate() {
        return date;
    }

}

