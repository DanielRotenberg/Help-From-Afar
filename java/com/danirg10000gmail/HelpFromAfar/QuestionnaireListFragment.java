package com.danirg10000gmail.HelpFromAfar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireM;
import com.danirg10000gmail.HelpFromAfar.user.SingleUserM;
import com.danirg10000gmail.HelpFromAfar.user.UserM;

import java.util.List;

/*
* using inner classes for ViewHolder and Adapter
 */

public class QuestionnaireListFragment extends SingleFragment {
    private static final String USER_NAME = "UserName";
    private boolean mIsTherapist;
    private RecyclerView mRecyclerView;
    private QuestionnaireAdapter mQuestionnaireAdapter;
    private UserM mUser;
    private String mUserName;

    public static Fragment newInstance(String userName){
        Bundle args = new Bundle();
        args.putString(USER_NAME,userName);
        QuestionnaireListFragment fragment = new QuestionnaireListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUserName = getArguments().getString(USER_NAME);
        mUser = SingleUserM.getSingleUser().getUser();
        mIsTherapist = mUser.isTherapist();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_questionnaire_list, container, false);
        mRecyclerView = (RecyclerView)fragmentView.findViewById(R.id.questionnaire_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mQuestionnaireAdapter.notifyDataSetChanged();
        updateUI();
    }

    private void updateUI(){
        if (mQuestionnaireAdapter == null) {
            mQuestionnaireAdapter = new QuestionnaireAdapter(verify());
            mRecyclerView.setAdapter(mQuestionnaireAdapter);
        } else {
            mQuestionnaireAdapter.notifyDataSetChanged();
        }
    }
    //ViewHolder inner class
    private class QuestionnaireViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private QuestionnaireM mQuestionnaireM;
        private Button mDateButton;
        private CheckBox mQuestionnaireCheckedCheckBox;

        //constructor
        public QuestionnaireViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDateButton = (Button)itemView.findViewById(R.id.view_holder_questionnaire_list_date);
            mDateButton.setOnClickListener(this);
            mQuestionnaireCheckedCheckBox = (CheckBox)itemView.findViewById(R.id.view_holder_questionnaire_list_therapist_checked);
        }
        public void setQuestionnaireM(QuestionnaireM questionnaireM){
            mQuestionnaireM = questionnaireM;
            mDateButton.setText(mQuestionnaireM.getFormattedDate());
            mQuestionnaireCheckedCheckBox.setChecked(mQuestionnaireM.isTherapistCheckedIt());
        }

        @Override
        public void onClick(View v) {
               String name;
            if (mUser.isTherapist()){
                name = mUserName;
            }else {
                name = mUser.getName();
            }
               Intent intent = QuestionViewPagerActivity.newInstanceExistingQuestionnaire(getActivity(), name,this.mQuestionnaireM.getId());
               startActivity(intent);
        }
    }
    // Adapter inner class
    private class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireViewHolder>{
        private List<QuestionnaireM> mAdapterQuestionnaireMList;
        private QuestionnaireM mQuestionnaireM;
        public QuestionnaireAdapter (List<QuestionnaireM>questionnaireMList){
            mAdapterQuestionnaireMList = questionnaireMList;
        }

        @Override
        public QuestionnaireViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View rootView = layoutInflater.inflate(R.layout.view_holder_questionnaire_list,parent,false);
            return new QuestionnaireViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(QuestionnaireViewHolder holder, int position) {
            mQuestionnaireM = mAdapterQuestionnaireMList.get(position);
            holder.setQuestionnaireM(mQuestionnaireM);
        }

        @Override
        public int getItemCount() {
            return mAdapterQuestionnaireMList.size();
        }
    }
    /*
    check the user if therapist -> load the list questionnaires of specific patient from his users list,
    if its patient user so load his own questionnaire list
      */
    private List<QuestionnaireM> verify(){
        if (mIsTherapist){
            UserM userM = mUser.getPatient(mUserName);
            return getUserQuestionnaires(userM);
        }
            return getUserQuestionnaires(mUser);
    }

    private List<QuestionnaireM> getUserQuestionnaires(UserM userM){
        return userM.getQuestionnaireBankM().getBankQuestionnaireMList();
    }

}
