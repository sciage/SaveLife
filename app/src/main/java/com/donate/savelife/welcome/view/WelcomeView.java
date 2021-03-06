package com.donate.savelife.welcome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donate.savelife.R;
import com.donate.savelife.component.BubblyDrawable;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.welcome.displayer.WelcomeDisplayer;
import com.novoda.notils.caster.Views;
import com.squareup.picasso.Picasso;

public class WelcomeView extends LinearLayout implements WelcomeDisplayer {

    private TextView welcomeMessage;
    private ImageView userAvatar;
    private View proceedButton;

    public WelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.merge_welcome_view, this);
        welcomeMessage = Views.findById(this, R.id.welcome_message_view);
        userAvatar = Views.findById(this, R.id.user_avatar);
        proceedButton = Views.findById(this, R.id.proceed_button);
        View senderFrame = Views.findById(this, R.id.welcome_sender_layout);
        senderFrame.setBackground(new BubblyDrawable(getResources()));
    }

    @Override
    public void attach(final InteractionListener interactionListener) {
        proceedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionListener.onGetStartedClicked();
            }
        });
    }

    @Override
    public void detach(InteractionListener interactionListener) {
        proceedButton.setOnClickListener(null);
    }

    @Override
    public void display(User sender) {
        Context context = getContext();
        Picasso.with(context).load(sender.getPhotoUrl()).into(userAvatar);
        welcomeMessage.setText(sender.getName() + "\ninvited you to Bonfire");
    }
}
