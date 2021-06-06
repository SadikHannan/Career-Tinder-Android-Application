package com.softwaregiants.careertinder.customViews;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.callback.ACTION_PERFORMED;
import com.softwaregiants.careertinder.callback.BaseListener;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.squareup.picasso.Picasso;

/**
 * Created by janisharali on 19/08/16.
 */
@Layout(R.layout.tinder_card_view)
public class TinderJobCard {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @View(R.id.profileImageView)
    ImageView profileImageView;

    @View(R.id.TVName)
    TextView TVName;

    @View(R.id.TVJobTitle)
    TextView TVJobTitle;

    @View(R.id.TVCity)
    TextView TVCity;

    @View(R.id.TVQualification)
    TextView TVQualification;

    @View(R.id.TVSkill1)
    TextView TVSkill1;

    @View(R.id.TVSkill2)
    TextView TVSkill2;

    @View(R.id.TVSkill3)
    TextView TVSkill3;

    @View(R.id.TVWorkEx)
    TextView TVWorkEx;

    @View(R.id.TVJobDesc)
    TextView TVJobDesc;

    @SwipeView
    android.view.View view;

    JobOpeningModel jobOpeningModel;
    int pos;
    BaseListener mBaseListener;

    private TinderJobCard() {}

    public TinderJobCard(JobOpeningModel jobOpeningModel, BaseListener mBaseListener, int pos) {
        this.jobOpeningModel = jobOpeningModel;
        this.pos = pos;
        this.mBaseListener = mBaseListener;
    }

    @Resolve
    public void onResolve() {
        TVName.setText("Company: " + jobOpeningModel.getCompanyName());
        TVJobTitle.setText("Job Title: " + jobOpeningModel.getJobTitle());
        TVQualification.setText("Qualification: " + jobOpeningModel.getDesiredQualification());
        TVCity.setText("City: " + jobOpeningModel.getPlaceOfWork());
        TVSkill1.setText("Skill 1: " + jobOpeningModel.getSkill1());
        TVSkill2.setText("Skill 2: " + jobOpeningModel.getSkill2());
        TVSkill3.setText("Skill 3: " + jobOpeningModel.getSkill3());
        TVWorkEx.setText("Required Work Exp: " + jobOpeningModel.getDesiredWorkExperience() + " months");
        TVJobDesc.setText("Job description:\n"+jobOpeningModel.getJobDescription());
        if ( null != jobOpeningModel.getImageUrl() && !jobOpeningModel.getImageUrl().isEmpty()) {
            StorageReference storageRef = storage.getReference(jobOpeningModel.getImageUrl());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder).into(profileImageView);
                }
            });
        } else {
            profileImageView.setImageResource(R.drawable.image_placeholder);
        }


    }

    @SwipeOut
    public void onSwipedOut() {
        //Swipe Left | REJECT
        Log.d("DEBUG", "onSwipedOut");
        mBaseListener.callback(ACTION_PERFORMED.SWIPE_LEFT_REJECT, pos);
    }

    @SwipeCancelState
    public void onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn() {
        //Swipe Right | ACCEPT
        Log.d("DEBUG", "onSwipedIn");
        mBaseListener.callback(ACTION_PERFORMED.SWIPE_RIGHT_ACCEPT, pos);
    }

    @SwipeInState
    public void onSwipeInState() {
        Log.d("DEBUG", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.d("DEBUG", "onSwipeOutState");
    }

    @SwipeHead
    public void onSwipeHead() {
        profileImageView.setBackgroundColor(Color.BLUE);

        Log.d("DEBUG", "onSwipeHead");
    }

    @Click(R.id.cardview)
    public void onClick() {
        mBaseListener.callback(ACTION_PERFORMED.JOB_CLICK, pos);
    }

    @Click(R.id.profileImageView)
    public void onClickImage() {
        mBaseListener.callback(ACTION_PERFORMED.JOB_CLICK, pos);
    }

    @LongClick(R.id.profileImageView)
    public void onProfileImageViewLongClick() {
        Log.d("DEBUG", "onProfileImageViewLongClick");
    }
}
