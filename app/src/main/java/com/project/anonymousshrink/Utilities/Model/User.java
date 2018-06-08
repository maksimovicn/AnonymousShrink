package com.project.anonymousshrink.Utilities.Model;

/**
 * Created by asadmehran on 01/03/2018.
 */

public class User {

    public String f_name;
    public String l_name;
    public String email;
    public String sex;
    public String psychologist;
    public String contact;
    public String questions;
    public String answers;
    public String dob;

    public User() {
    }

    public User(String f_name, String l_name, String email, String sex, String psychologist, String contact, String questions, String answers, String dob) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.email = email;
        this.sex = sex;
        this.psychologist = psychologist;
        this.contact = contact;
        this.questions = questions;
        this.answers = answers;
        this.dob = dob;
    }

    public String getF_name() {
        return f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    public String getPsychologist() {
        return psychologist;
    }

    public String getContact() {
        return contact;
    }

    public String getQuestions() {
        return questions;
    }

    public String getAnswers() {
        return answers;
    }

    public String getDob() {
        return dob;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPsychologist(String psychologist) {
        this.psychologist = psychologist;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
