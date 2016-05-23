package com.danirg10000gmail.HelpFromAfar.model;

import java.util.UUID;

public class QuestionM {
    private String mQuestion;
    private String mAnswer;
    private String mExplanation;
    private String mComment;
    private boolean mIsTherapistComment;
    private UUID mId;

    //constructor
    public QuestionM(String question, String explanation) {
        mId = UUID.randomUUID();
        mQuestion = question;
        mExplanation = explanation;
    }

    //getters and setters
    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public void setIsTherapistComment(boolean isTherapistComment) {
        mIsTherapistComment = isTherapistComment;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getExplanation() {
        return mExplanation;
    }

    public String getComment() {
        return mComment;
    }

    public boolean isTherapistComment() {
        return mIsTherapistComment;
    }

    public UUID getId() {
        return mId;
    }
}
