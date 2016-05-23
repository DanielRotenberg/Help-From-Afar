package com.danirg10000gmail.HelpFromAfar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.danirg10000gmail.HelpFromAfar.dataBase.CentralInternalData;
import com.danirg10000gmail.HelpFromAfar.dataBase.ParseMethods;
import com.danirg10000gmail.HelpFromAfar.model.QuestionM;
import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireM;
import com.danirg10000gmail.HelpFromAfar.user.SingleUserM;
import com.danirg10000gmail.HelpFromAfar.user.UserM;
import com.danirg10000gmail.HelpFromAfar.userData.ProfileActivity;

import java.util.UUID;

public class QuestionFragment extends SingleFragment {
    public static final String QUESTION_POSITION = "questionPosition";
    public static final String IS_ALREADY_ANSWERED = "isAlreadyAnswered";
    public static final String QUESTION_EXPLANATION = "QuestionExplanation";
    public static final String ANSWERED_QUESTIONNAIRE_ID = "answeredQuestionnaireId";
    public static final String IDENTIFIER = "Identifier";
    private boolean mIsAlreadyAnswered;
    private int index = 0;
    private UserM mUserM;
    public QuestionnaireM questionnaireM;
    private QuestionM mQuestionM;
    private TextView mQuestionText;
    private EditText mQuestionAnswer;
    private ImageButton mQuestionExplanation;
    private EditText mTherapistTextComment;
    private TextView mQuestionNumber;
    private Button mSend;
    private String mCheckQuestionnaireNewOrNot;
    private String mCurrentlyUsing;

    public static QuestionFragment newInstance(int position, String checkWhoUsing, boolean isAlreadyAnswered, UUID id) {
        Bundle args = new Bundle();
        args.putInt(QUESTION_POSITION, position);
        args.putString(IDENTIFIER, checkWhoUsing);
        args.putBoolean(IS_ALREADY_ANSWERED, isAlreadyAnswered);
        args.putSerializable(ANSWERED_QUESTIONNAIRE_ID, id);
        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIsAlreadyAnswered = getArguments().getBoolean(IS_ALREADY_ANSWERED);
        UUID id = (UUID) getArguments().getSerializable(ANSWERED_QUESTIONNAIRE_ID);
        mCheckQuestionnaireNewOrNot = getArguments().getString(IDENTIFIER);
        SingleUserM singleUserM = SingleUserM.getSingleUser();
        mUserM = singleUserM.getUserType(singleUserM, mCheckQuestionnaireNewOrNot);
        switch (mCheckQuestionnaireNewOrNot) {
            case CentralInternalData.USER_NEW_QUESTIONNAIRE:
                mCurrentlyUsing = "patientNew";
                questionnaireM = mUserM.getQuestionnaireBankM().getQuestionnaire(id);
                break;
            default:
                questionnaireM = mUserM.getQuestionnaireBankM().getQuestionnaire(id);
                if (!SingleUserM.getSingleUser().getUser().isTherapist()) {
                    mCurrentlyUsing = "patientExisting";
                } else {
                    mCurrentlyUsing = "Therapist";
                }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_single_question, container, false);
        index = getArguments().getInt(QUESTION_POSITION);
        mQuestionM = questionnaireM.getQuestion(index);
        mQuestionNumber = (TextView) fragmentView.findViewById(R.id.question_number);
        mQuestionText = (TextView) fragmentView.findViewById(R.id.question_text);
        mQuestionExplanation = (ImageButton) fragmentView.findViewById(R.id.question_explanation);
        mQuestionAnswer = (EditText) fragmentView.findViewById(R.id.question_answer);
        mTherapistTextComment = (EditText) fragmentView.findViewById(R.id.therapist_comment);
        mQuestionAnswer.setMovementMethod(new ScrollingMovementMethod());
        mTherapistTextComment.setMovementMethod(new ScrollingMovementMethod());
        mSend = (Button) fragmentView.findViewById(R.id.button_send);
        mQuestionNumber.setText(String.valueOf(index + 1) + "/" + 10);
        mQuestionText.setText(mQuestionM.getQuestion());
        if (mIsAlreadyAnswered || questionnaireM.isAnswered()) {
            mQuestionAnswer.setText(mQuestionM.getAnswer());
            mQuestionAnswer.setEnabled(false);
            if (!SingleUserM.getSingleUser().getUser().isTherapist()) {
                mSend.setEnabled(false);
            }
        } else {
            mQuestionAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().isEmpty()){
                        s="No answer";
                    }
                    mQuestionM.setAnswer(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        mQuestionExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionExplanationDialogFragment questionExplanationDialogFragment = QuestionExplanationDialogFragment.createDialogWithBundle(index);
                FragmentManager fm = getFragmentManager();
                questionExplanationDialogFragment.show(fm, QUESTION_EXPLANATION);
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentlyUsing.equals("patientExisting")) {
                    mSend.setEnabled(false);
                } else {
                    ParseMethods parseMethods = new ParseMethods(getActivity());

                    if (!SingleUserM.getSingleUser().getUser().isTherapist()) {
                        parseMethods.uploadQuestionnaire(questionnaireM, index);
                        Toast.makeText(getActivity(), "questionnaire sent", Toast.LENGTH_SHORT).show();
                    } else {
                        parseMethods.uploadComments(questionnaireM, index);
                        Toast.makeText(getActivity(), "comments uploaded", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = ProfileActivity.newIntent(getActivity());
                    startActivity(intent);
                    getActivity().finish();

                }
            }
        });

        if (SingleUserM.getSingleUser().getUser().isTherapist()) {
            if (mQuestionM.getComment() != null) {
                mTherapistTextComment.setText(mQuestionM.getComment());
                mTherapistTextComment.setEnabled(false);
                mSend.setEnabled(false);
            } else {
                mTherapistTextComment.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().isEmpty()) {
                            mQuestionM.setComment("No Comments");
                            return;
                        }
                        mQuestionM.setComment(s.toString());

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else {
            if (mQuestionM.getComment() == null) {
                mTherapistTextComment.setVisibility(View.INVISIBLE);
            } else {
                mTherapistTextComment.setText(mQuestionM.getComment());
                mTherapistTextComment.setEnabled(false);
            }
        }
        return fragmentView;
    }
}
