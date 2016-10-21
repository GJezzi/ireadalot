package com.example.android.ireadalot.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gjezzi on 11/10/16.
 */

public class Quizz implements Serializable {

    @SerializedName("question")
    private String mQuestion;

    @SerializedName("answer")
    private String mAnswer;

    public Quizz() {}

    public Quizz(String question, String answer) {
        this.mQuestion = question;
        this.mAnswer = answer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }
}
