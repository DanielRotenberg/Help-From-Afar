package com.danirg10000gmail.HelpFromAfar.dataBase;

import android.content.Context;
import android.widget.Toast;

import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.AnswersClass;
import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.AnswersClass.AnswerClassCols;
import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.UserClass;
import com.danirg10000gmail.HelpFromAfar.model.QuestionM;
import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireBankM;
import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireM;
import com.danirg10000gmail.HelpFromAfar.user.UserM;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ParseMethods {
    private static final String TAG = "ParseMethods";
    private Context mContext;

    public ParseMethods(Context context){
        mContext = context;
    }

    public List<QuestionnaireM> loadUserQuestionnaires(String name, QuestionnaireBankM questionnaireBankM){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AnswersClass.NAME);
        query.whereEqualTo(AnswerClassCols.USER_NAME, name);
        query.addAscendingOrder(AnswerClassCols.ANSWERS_CREATED_AT_COLUMN);
        try {
            final List<ParseObject> userQuestions = query.find();
            for (ParseObject object : userQuestions) {
                final List<String> mAnswersList = new ArrayList<String>();
                final List<String> mCommentsList = new ArrayList<String>();
                UUID id = UUID.fromString(object.getString(AnswerClassCols.UUID_COLUMN));
                JSONArray j = object.getJSONArray(AnswerClassCols.ANSWERS_ARRAY_COLUMN);
                JSONArray comments = object.getJSONArray(AnswerClassCols.COMMENTS_COLUMN);
                Date createdDate = object.getCreatedAt();
                try {
                    for (int i = 0; i < j.length(); i++) {
                        mAnswersList.add(j.getString(i));
                    }
                    if (comments != null){
                        for (int i = 0; i < comments.length(); i++) {
                            mCommentsList.add(comments.getString(i));
                        }
                    }
                } catch (Exception ex) {
                   showException(ex);
                }
        QuestionnaireM userQuestionnaireFromParse = new QuestionnaireM(mContext, id.toString(), createdDate, mAnswersList,mCommentsList);
                questionnaireBankM.addQuestionnaire(userQuestionnaireFromParse);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return questionnaireBankM.getBankQuestionnaireMList();
    }

    public List<UserM> loadUsers(List<UserM> usersList, boolean isCallerTherapist){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserClass.NAME);
        query.whereEqualTo(UserClass.UserClassCols.IS_THERAPIST, !isCallerTherapist);
        query.addAscendingOrder(UserClass.UserClassCols.USER_NAME);
        try {
            final List<ParseObject> parseUsersList = query.find();
            for (ParseObject object : parseUsersList) {
                String userName = object.getString(UserClass.UserClassCols.USER_NAME);
                UserM userM = new UserM(mContext, !isCallerTherapist, userName);
                usersList.add(userM);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } return usersList;
    }

    public void uploadQuestionnaire(QuestionnaireM questionnaireM, int index){
        List<QuestionM> questionMList = questionnaireM.getQuestionsList();
        String[] answers = new String[index + 1];
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < answers.length; ++i) {
            QuestionM questionM = questionMList.get(i);
            answers[i] = questionM.getAnswer();
            jsonArray.put(questionM.getAnswer());
        }
        ParseObject parseObject = new ParseObject(AnswersClass.NAME);
        parseObject.put(AnswerClassCols.USER_NAME, ParseUser.getCurrentUser().getUsername());
        parseObject.put(AnswerClassCols.UUID_COLUMN,questionnaireM.getId().toString());
        parseObject.put(AnswerClassCols.ANSWERS_ARRAY_COLUMN, jsonArray);
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.increment(UserClass.UserClassCols.TOTAL_QUESTIONNAIRES);

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    showException(e);
                }
            }
        });
    }

    public void uploadComments(QuestionnaireM questionnaireM, int index ){
        List<QuestionM> questionMList = questionnaireM.getQuestionsList();
        String[] comments = new String[index + 1];
        final JSONArray jsonComments = new JSONArray();
        for (int i = 0; i < comments.length; ++i) {
            QuestionM questionM = questionMList.get(i);
            jsonComments.put(questionM.getComment());
        }
        ParseQuery <ParseObject> query = ParseQuery.getQuery(AnswersClass.NAME);
        query.whereEqualTo(AnswerClassCols.UUID_COLUMN,questionnaireM.getId().toString());
        try {
            ParseObject parseObject =  query.getFirst();
            parseObject.put(AnswerClassCols.COMMENTS_COLUMN, jsonComments);
            parseObject.saveEventually();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showException (Exception e){
        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

}
