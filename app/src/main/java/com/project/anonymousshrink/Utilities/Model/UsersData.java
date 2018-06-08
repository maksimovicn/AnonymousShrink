package com.project.anonymousshrink.Utilities.Model;

import java.util.ArrayList;

/**
 * Created by asadmehran on 01/03/2018.
 */

public class UsersData {

    public static User user;
    public static ArrayList<Questions> questionsArrayList;

    public static ArrayList<Questions> getQuestionsArrayList() {
        return questionsArrayList;
    }
    public static void setQuestionsArrayList(ArrayList<Questions> questionsArrayList) {
        UsersData.questionsArrayList = questionsArrayList;
    }
    public static void setUser(User user) {
        UsersData.user = user;
    }
    public static User getUser() {
        return user;
    }
}
