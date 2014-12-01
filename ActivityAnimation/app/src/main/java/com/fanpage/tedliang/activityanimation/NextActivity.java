package com.fanpage.tedliang.activityanimation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import java.util.logging.Handler;

/**
 * Created by tedliang on 14/12/1.
 */
public class NextActivity extends Activity{
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    View mView;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextview);
        mView = findViewById(R.id.root);
        Bundle bundle = getIntent().getExtras();
        final int thumbnailTop = bundle.getInt(MyActivity.PACKAGE_NAME + ".top");
        final int thumbnailLeft = bundle.getInt(MyActivity.PACKAGE_NAME + ".left");
        final int thumbnailWidth = bundle.getInt(MyActivity.PACKAGE_NAME + ".width");
        final int thumbnailHeight = bundle.getInt(MyActivity.PACKAGE_NAME + ".height");
        if (savedInstanceState == null) {
            ViewTreeObserver observer = mView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    mView.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mView.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / mView.getWidth();
                    mHeightScale = (float) thumbnailHeight / mView.getHeight();
                    Log.d("Ted","scale "+mWidthScale +" "+mHeightScale);
                    runEnterAnimation();

                    return true;
                }
            });
        }

    }

    public void runEnterAnimation() {
        final long duration = (long) (250 * 1);

        mView.setPivotX(0);
        mView.setPivotY(0);
        mView.setScaleX(mWidthScale);
        mView.setScaleY(mHeightScale);
        mView.setTranslationX(mLeftDelta);
        mView.setTranslationY(mTopDelta);

        mView.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator);


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        runExitAnimation(new Runnable() {
            public void run() {
                // *Now* go ahead and exit the activity
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        backpress();
                    }
                });
            }
        });
    }

    android.os.Handler mHandler = new android.os.Handler();
    private void backpress(){
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    public void runExitAnimation(final Runnable endAction) {
        final long duration = (long) (250 * 1);
        mView.animate().setDuration(duration).
                scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                endAction.run();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ObjectAnimator bgAnim = ObjectAnimator.ofFloat(mView, "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();



    }

}
