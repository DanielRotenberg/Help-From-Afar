package com.danirg10000gmail.HelpFromAfar.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionnaireBankM {
    private UUID mId;
    private List<QuestionnaireM> mBankQuestionnaireMList;

    private Context mContext;

    public QuestionnaireBankM(Context context){
        mId = UUID.randomUUID();
        mContext = context.getApplicationContext();
        mBankQuestionnaireMList = new ArrayList<>();
    }


    public List<QuestionnaireM> getBankQuestionnaireMList() {
        return mBankQuestionnaireMList;
    }

    public UUID getId() {
        return mId;
    }

    //adding Questionnaires to the List
    public void addQuestionnaire(QuestionnaireM questionnaireM){
        mBankQuestionnaireMList.add(questionnaireM);
    }


    public QuestionnaireM getQuestionnaire(UUID id){
        for (QuestionnaireM questionnaireM : mBankQuestionnaireMList){
            if (questionnaireM.getId().equals(id)){
                return questionnaireM;
            }
        }
        return null;
    }

}
