/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

public class RotateLoadingLayout extends LoadingLayout {

//	static final int ROTATION_ANIMATION_DURATION = 1200;

//	private final Animation mRotateAnimation;
//	private final Matrix mHeaderImageMatrix;
//
//	private float mRotationPivotX, mRotationPivotY;

//	private final boolean mRotateDrawableWhilePulling;

	int[]resIds = new int[]{
			R.drawable.dropdown_anim_00,
			R.drawable.dropdown_anim_01,
			R.drawable.dropdown_anim_02,
			R.drawable.dropdown_anim_03,
			R.drawable.dropdown_anim_04,
			R.drawable.dropdown_anim_05,
			R.drawable.dropdown_anim_06,
			R.drawable.dropdown_anim_07,
			R.drawable.dropdown_anim_08,
			R.drawable.dropdown_anim_09,
			R.drawable.dropdown_anim_10};

	public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

//		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

//		mHeaderImage.setScaleType(ScaleType.MATRIX);
//		mHeaderImageMatrix = new Matrix();
//		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
//
//		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
//		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
//		mRotateAnimation.setRepeatCount(Animation.INFINITE);
//		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
//		if (null != imageDrawable) {
//			mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
//			mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
//		}
	}

	protected void onPullImpl(float scaleOfLayout) {

//		float angle;
//		if (mRotateDrawableWhilePulling) {
//			angle = scaleOfLayout * 90f;
//		} else {
//			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
//		}
//
//		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
//		mHeaderImage.setImageMatrix(mHeaderImageMatrix);

		Log.i("LWX======", "onPullImpl:" + scaleOfLayout);
		int index = (int) (scaleOfLayout*10);
		int resId;
		if (index <= 10 ){
			resId = resIds[index];
			Bitmap src = BitmapFactory.decodeResource(getResources(), resId);
			int size = index / 10 + 1;
			int width = src.getWidth()*size;
			int height = src.getHeight()*size;
			Bitmap bitmap = Bitmap.createScaledBitmap(src, width, height, true);
			mHeaderImage.setImageBitmap(bitmap);
		} else {
			resId = resIds[10];
			mHeaderImage.setImageResource(resId);
		}
	}

	@Override
	protected void refreshingImpl() {
//		mHeaderImage.startAnimation(mRotateAnimation);
		mHeaderImage.setImageResource(R.drawable.refreshing_anim);
		Drawable drawable = mHeaderImage.getDrawable();
		((AnimationDrawable)drawable).start();
	}

	@Override
	protected void resetImpl() {
//		mHeaderImage.clearAnimation();
//		resetImageRotation();
	}

	private void resetImageRotation() {
//		if (null != mHeaderImageMatrix) {
//			mHeaderImageMatrix.reset();
//			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
//		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.dropdown_anim_00;
	}

}
