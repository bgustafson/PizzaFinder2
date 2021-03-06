package com.example.brigus.pizzafinder2.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.brigus.pizzafinder2.R;
import com.squareup.picasso.Picasso;

public class AboutFragment extends DialogFragment {

    public static String TAG = "About Fragment";

    @BindView(R.id.dismiss_about) ImageButton mDismissButton;
    @BindView(R.id.avatar) ImageView mAvatarImage;
    @BindView(R.id.about_text) TextView mAboutTextView;
    private Unbinder mUnbinder;

    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        getDialog().setCanceledOnTouchOutside(false);

        Picasso.with(getContext())
                .load("https://avatars1.githubusercontent.com/u/4312256?v=3")
                .into(mAvatarImage);

        mAboutTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @OnClick(R.id.dismiss_about)
    public void goAway() {
        this.dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
