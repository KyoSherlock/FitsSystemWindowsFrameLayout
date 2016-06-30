/**
 * Copyright 2016, KyoSherlock
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kyo.fitssystemwindows;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by jianghui on 6/20/16.
 */
public class FitsSystemWindowsFrameLayout2 extends FitsSystemWindowsFrameLayout {

	private int effectiveHeight;
	private int lastLimitHeight;
	private int lastBottomInset;
	private OnKeyboardVisibleChangedListener onKeyboardVisibleChangedListener;

	public FitsSystemWindowsFrameLayout2(Context context) {
		super(context);
		this.init(context);
	}

	public FitsSystemWindowsFrameLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	public FitsSystemWindowsFrameLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FitsSystemWindowsFrameLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.init(context);
	}

	public void setOnKeyboardVisibleChangedListener(OnKeyboardVisibleChangedListener onKeyboardVisibleChangedListener) {
		this.onKeyboardVisibleChangedListener = onKeyboardVisibleChangedListener;
	}

	private void init(Context context) {
		effectiveHeight = dp2px(context, 200);
	}

	public static int dp2px(Context context, float dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		final int px = (int) (metrics.density * dp + 0.5f);
		if (px != 0) return px;
		if (dp == 0) return 0;
		if (dp > 0) return 1;
		return -1;
	}

	public static int getActionBarHeight(Context context) {
		int result = 0;
		TypedValue tv = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
		if (tv.resourceId > 0) {

			result = context.getResources().getDimensionPixelSize(tv.resourceId);
		}
		return result;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int mode = MeasureSpec.getMode(heightMeasureSpec);
		if (mode == MeasureSpec.UNSPECIFIED) {
			throw new IllegalStateException("WindowInsetsFrameLayout2 is not supported in this case!");
		}

		if (Build.VERSION.SDK_INT >= 21) {
			final boolean applyInsets = lastInsets != null && ViewCompat.getFitsSystemWindows(this);
			if (applyInsets) {
				int bottomInset = FitsSystemWindowsFrameLayout2CompatApi21.getBottomInset(lastInsets);
				Log.i("kyo", "lastBottomInset:" + lastBottomInset);
				if (bottomInset > lastBottomInset) {
					int distance = bottomInset - lastBottomInset;
					if (distance > effectiveHeight) { // This judgment is superfluous
						dispatchKeyboardVisibleChanged(true, distance);
					}
				} else if (bottomInset < lastBottomInset) {
					int distance = lastBottomInset - bottomInset;
					if (distance > effectiveHeight) {
						dispatchKeyboardVisibleChanged(false, distance);
					}
				}
				lastBottomInset = bottomInset;
			}
		} else {
			int height = MeasureSpec.getSize(heightMeasureSpec);
			if (lastLimitHeight == 0) {
				lastLimitHeight = height;
			} else {
				if (height > lastLimitHeight) {
					int distance = height - lastLimitHeight;
					if (distance > effectiveHeight) {
						dispatchKeyboardVisibleChanged(false, distance);
					}
				} else if (height < lastLimitHeight) {
					int distance = lastLimitHeight - height;
					if (distance > effectiveHeight) {
						dispatchKeyboardVisibleChanged(true, distance);
					}
				}
				lastLimitHeight = height;
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void dispatchKeyboardVisibleChanged(boolean visible, int height) {
		if (onKeyboardVisibleChangedListener != null) {
			onKeyboardVisibleChangedListener.onKeyboardVisibleChanged(visible, height);
		}
	}
}