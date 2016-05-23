package com.danirg10000gmail.HelpFromAfar.model;



import android.content.Context;
import android.content.res.Resources;

import com.danirg10000gmail.HelpFromAfar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class QuestionnaireM {
    private List<QuestionM> mQuestionsList;
    private UUID mId;
    private Date mDate;
    private boolean isTherapistCheckedIt;
    private Context mContext;
    private String mFormattedDate;
    private boolean mIsAnswered;

    //constructor -> creates list of Question Objects and populates them inside the List
    public QuestionnaireM(Context context){
        mId = UUID.randomUUID();
        mDate = Calendar.getInstance().getTime();
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        mFormattedDate = dateFormat.format(mDate);
        mContext = context;
        mIsAnswered = false;
        mQuestionsList = new ArrayList<>();
        Resources resources = mContext.getResources();
        String [] questions = resources.getStringArray(R.array.questions_array);
        String [] questionExplanations = resources.getStringArray(R.array.questions_explanations_array);
        for (int i=0; i<questions.length; i++){
            QuestionM question = new QuestionM(questions[i],questionExplanations[i]);
            mQuestionsList.add(question);
        }
    }


    //constructor used when retrieving questionnaire from Parse.com
    public QuestionnaireM(Context context,String uuid,Date createDate,List<String> answers, List<String> comments){
        mDate = createDate;
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        mFormattedDate = dateFormat.format(mDate);
        mContext = context;
        mId = UUID.fromString(uuid);
        mIsAnswered = true;
        mQuestionsList = new ArrayList<>();
        Resources resources = mContext.getResources();
        String [] questions = resources.getStringArray(R.array.questions_array);
        String [] questionExplanations = resources.getStringArray(R.array.questions_explanations_array);
        for (int i=0; i<questions.length; i++){
            QuestionM question = new QuestionM(questions[i],questionExplanations[i]);
            mQuestionsList.add(question);
        }
        for (int i=0; i<answers.size(); i++){
            mQuestionsList.get(i).setAnswer(answers.get(i));
        }
        if(comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                mQuestionsList.get(i).setComment(comments.get(i));
                setIsTherapistCheckedIt(true);
            }
        }
    }


    public String getFormattedDate() {
        return mFormattedDate;
    }

    public void setIsTherapistCheckedIt(boolean isTherapistCheckedIt) {
        this.isTherapistCheckedIt = isTherapistCheckedIt;
    }

    public boolean isTherapistCheckedIt() {
        return isTherapistCheckedIt;
    }

    public Date getDate() {
        return mDate;
    }

    public UUID getId() {
        return mId;
    }

    public List<QuestionM> getQuestionsList() {
        return mQuestionsList;
    }

    public boolean isAnswered(){
        return mIsAnswered;
    }

    public QuestionM getQuestion(int position){
        return mQuestionsList.get(position);
    }

    public void setIsAnswered(boolean isAnswered) {
        mIsAnswered = isAnswered;
    }

}
