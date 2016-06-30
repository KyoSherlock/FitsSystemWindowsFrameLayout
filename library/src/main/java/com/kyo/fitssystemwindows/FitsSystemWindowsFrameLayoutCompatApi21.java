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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;

import com.kyo.fitssystemwindows.FitsSystemWindowsFrameLayout.LayoutParams;


/**
 * Created by jianghui on 6/21/16.
 */
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class FitsSystemWindowsFrameLayoutCompatApi21 {

	private static final int[] THEME_ATTRS = {
		android.R.attr.colorPrimaryDark
	};

	public static void configureApplyInsets(FitsSystemWindowsFrameLayout fitsSystemWindowsFrameLayout) {
		fitsSystemWindowsFrameLayout.setOnApplyWindowInsetsListener(new InsetsListener());
		fitsSystemWindowsFrameLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	public static void dispatchChildInsets(View child, LayoutParams lp, int gravity, Object insets) {
		WindowInsets wi = (WindowInsets) insets;
		int l = 0, t = 0, r = 0, b = 0;
		if (lp.width == LayoutParams.MATCH_PARENT) {
			l = wi.getSystemWindowInsetLeft();
			r = wi.getSystemWindowInsetRight();
		} else {
			if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
				l = wi.getSystemWindowInsetLeft();
			} else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
				r = wi.getSystemWindowInsetRight();
			}
		}
		if (lp.height == LayoutParams.MATCH_PARENT) {
			t = wi.getSystemWindowInsetTop();
			b = wi.getSystemWindowInsetBottom();
		} else {
			if ((gravity & Gravity.TOP) == Gravity.TOP) {
				t = wi.getSystemWindowInsetTop();
			} else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
				b = wi.getSystemWindowInsetBottom();
			}
		}
		wi = wi.replaceSystemWindowInsets(l, t,
			r, b);
		child.dispatchApplyWindowInsets(wi);
	}

	public static void applyMarginInsets(FitsSystemWindowsFrameLayout.LayoutParams lp, int gravity, Object insets) {
		WindowInsets wi = (WindowInsets) insets;
		int l = 0, t = 0, r = 0, b = 0;
		if (lp.width == LayoutParams.MATCH_PARENT) {
			l = wi.getSystemWindowInsetLeft();
			r = wi.getSystemWindowInsetRight();
		} else {
			if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
				l = wi.getSystemWindowInsetLeft();
			} else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
				r = wi.getSystemWindowInsetRight();
			}
		}
		if (lp.height == LayoutParams.MATCH_PARENT) {
			t = wi.getSystemWindowInsetTop();
			b = wi.getSystemWindowInsetBottom();
		} else {
			if ((gravity & Gravity.TOP) == Gravity.TOP) {
				t = wi.getSystemWindowInsetTop();
			} else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
				b = wi.getSystemWindowInsetBottom();
			}
		}
		lp.leftMargin2 = l;
		lp.topMargin2 = t;
		lp.rightMargin2 = r;
		lp.bottomMargin2 = b;
	}

	public static int getTopInset(Object insets) {
		return insets != null ? ((WindowInsets) insets).getSystemWindowInsetTop() : 0;
	}

	public static Drawable getDefaultStatusBarBackground(Context context) {
		final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
		try {
			return a.getDrawable(0);
		} finally {
			a.recycle();
		}
	}

	static class InsetsListener implements View.OnApplyWindowInsetsListener {
		@Override
		public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
			if (v instanceof FitsSystemWindowsFrameLayout) {
				FitsSystemWindowsFrameLayout fitsSystemWindowsFrameLayout = (FitsSystemWindowsFrameLayout) v;
				fitsSystemWindowsFrameLayout.setChildInsets(insets, insets.getSystemWindowInsetTop() > 0);
			}
			return insets.consumeSystemWindowInsets();
		}
	}
}
