package com.danirg10000gmail.HelpFromAfar.user;

/*
* using inner classes for ViewHolder and Adapter
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danirg10000gmail.HelpFromAfar.QuestionnaireListActivity;
import com.danirg10000gmail.HelpFromAfar.R;
import com.danirg10000gmail.HelpFromAfar.UserDetailDialogFragment;
import com.danirg10000gmail.HelpFromAfar.SingleFragment;

import java.util.List;

public class UsersListFragment extends SingleFragment {
    private static final String TAG = "Therapist";
    public static final String SHOW = "UserDetailDialogFragment";
    private UserM mUser;
    private RecyclerView mRecyclerView;
    private List<UserM> mUsersList;
    private SingleUserM mSingleUser;

    public static UsersListFragment newInstance(){
        return new UsersListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSingleUser = SingleUserM.getSingleUser();
        String name = mSingleUser.getUser().getName();
        mUsersList = mSingleUser.getUser().getUsersList();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_therapist_patients_list, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.patients_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new UserAdapter(mUsersList));
        return v;
    }
    //ViewHolder inner class
    private class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mUserNameHolder;
        private ImageButton info;
        public UserHolder(View itemView) {
            super(itemView);
            info = (ImageButton)itemView.findViewById(R.id.show);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDetailDialogFragment userDetailDialogFragment = UserDetailDialogFragment.createDialogWithBundle(mUserNameHolder.getText().toString());
                    FragmentManager fm = getFragmentManager();
                    userDetailDialogFragment.show(fm,SHOW);
                }
            });

            mUserNameHolder = (TextView)itemView.findViewById(R.id.users_list_user_name);
            if (SingleUserM.getSingleUser().getUser().isTherapist()) {
                itemView.setOnClickListener(this);
            }
        }
        public void setUserM(UserM user){
            mUserNameHolder.setText(user.getName());
            UsersListFragment.this.mUser = user;
        }

        @Override
        public void onClick(View v) {
            // using static factory to create Intent
            Intent intent = QuestionnaireListActivity.newInstance(getActivity(), mUserNameHolder.getText().toString());
            startActivity(intent);
        }
    }
    // Adapter inner class
    private class UserAdapter extends RecyclerView.Adapter<UserHolder>{
        private List<UserM> mAdapterUsersList;

            public UserAdapter(List<UserM> userMList){
                mAdapterUsersList = userMList;
            }
        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.users_list,parent,false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
           UserM user = mAdapterUsersList.get(position);
           holder.setUserM(user);
        }

        @Override
        public int getItemCount() {
            return mAdapterUsersList.size();
        }
    }

}
