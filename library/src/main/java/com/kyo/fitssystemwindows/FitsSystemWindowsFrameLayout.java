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
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by jianghui on 6/20/16.
 */
public class FitsSystemWindowsFrameLayout extends FrameLayout {
	private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
	protected Object lastInsets;
	private boolean drawStatusBarBackground;
	private Drawable statusBarBackground;

	interface WindowInsetsFrameLayoutCompatImpl {
		void configureApplyInsets(FitsSystemWindowsFrameLayout fitsSystemWindowsFrameLayout);

		void dispatchChildInsets(View child, LayoutParams lp, int gravity, Object insets);

		void applyMarginInsets(LayoutParams lp, int gravity, Object insets);

		int getTopInset(Object lastInsets);

		Drawable getDefaultStatusBarBackground(Context context);
	}

	static class WindowInsetsFrameLayoutCompatImplBase implements WindowInsetsFrameLayoutCompatImpl {
		public void configureApplyInsets(FitsSystemWindowsFrameLayout fitsSystemWindowsFrameLayout) {
			// This space for rent
		}

		public void dispatchChildInsets(View child, LayoutParams lp, int gravity, Object insets) {
			// This space for rent
		}

		public void applyMarginInsets(LayoutParams lp, int gravity, Object insets) {
			// This space for rent
		}

		public int getTopInset(Object insets) {
			return 0;
		}

		@Override
		public Drawable getDefaultStatusBarBackground(Context context) {
			return null;
		}
	}

	static class WindowInsetsFrameLayoutCompatImplApi21 implements WindowInsetsFrameLayoutCompatImpl {
		public void configureApplyInsets(FitsSystemWindowsFrameLayout fitsSystemWindowsFrameLayout) {
			FitsSystemWindowsFrameLayoutCompatApi21.configureApplyInsets(fitsSystemWindowsFrameLayout);
		}

		public void dispatchChildInsets(View child, LayoutParams lp, int gravity, Object insets) {
			FitsSystemWindowsFrameLayoutCompatApi21.dispatchChildInsets(child, lp, gravity, insets);
		}

		public void applyMarginInsets(LayoutParams lp, int gravity, Object insets) {
			FitsSystemWindowsFrameLayoutCompatApi21.applyMarginInsets(lp, gravity, insets);
		}

		public int getTopInset(Object insets) {
			return FitsSystemWindowsFrameLayoutCompatApi21.getTopInset(insets);
		}

		@Override
		public Drawable getDefaultStatusBarBackground(Context context) {
			return FitsSystemWindowsFrameLayoutCompatApi21.getDefaultStatusBarBackground(context);
		}
	}

	static {
		final int version = Build.VERSION.SDK_INT;
		if (version >= 21) {
			IMPL = new WindowInsetsFrameLayoutCompatImplApi21();
		} else {
			IMPL = new WindowInsetsFrameLayoutCompatImplBase();
		}
	}

	static final WindowInsetsFrameLayoutCompatImpl IMPL;

	public FitsSystemWindowsFrameLayout(Context context) {
		super(context);
		this.init(context);
	}

	public FitsSystemWindowsFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	public FitsSystemWindowsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FitsSystemWindowsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.init(context);
	}


	private void init(Context context) {
		if (ViewCompat.getFitsSystemWindows(this)) {
			IMPL.configureApplyInsets(this);
			statusBarBackground = IMPL.getDefaultStatusBarBackground(context);
		}
	}

	public void setDrawStatusBarBackground(boolean draw) {
		this.drawStatusBarBackground = draw;
		if (drawStatusBarBackground
			&& lastInsets != null
			&& IMPL.getTopInset(lastInsets) > 0) {
			this.invalidate();
		}
	}

	/**
	 * @hide Internal use only; called to apply window insets when configured
	 * with fitsSystemWindows="true"
	 */
	public void setChildInsets(Object insets, boolean draw) {
		lastInsets = insets;
		drawStatusBarBackground = draw && drawStatusBarBackground;
		setWillNotDraw(!drawStatusBarBackground && getBackground() == null);
		requestLayout();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (drawStatusBarBackground && statusBarBackground != null) {
			final int inset = IMPL.getTopInset(lastInsets);
			if (inset > 0) {
				statusBarBackground.setBounds(0, 0, getWidth(), inset);
				statusBarBackground.draw(canvas);
			}
		}
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final boolean applyInsets = lastInsets != null && ViewCompat.getFitsSystemWindows(this);
		final int layoutDirection = ViewCompat.getLayoutDirection(this);
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);

			if (child.getVisibility() == GONE) {
				continue;
			}

			final LayoutParams lp = (LayoutParams) child.getLayoutParams();
			if (lp.gravity == -1) {
				lp.gravity = DEFAULT_CHILD_GRAVITY;
			}

			if (applyInsets) {
				int cgrav = GravityCompat.getAbsoluteGravity(lp.gravity, layoutDirection);

				if (cgrav == -1) {
					cgrav = DEFAULT_CHILD_GRAVITY;
				} else {
					if ((cgrav & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
						cgrav = cgrav | Gravity.TOP;
					}
					if ((cgrav & Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
						cgrav = cgrav | (layoutDirection == LAYOUT_DIRECTION_LTR ? Gravity.LEFT : Gravity.RIGHT);
					}
				}

				if (ViewCompat.getFitsSystemWindows(child)) {
					final int l = child.getPaddingLeft();
					final int t = child.getPaddingTop();
					final int r = child.getPaddingRight();
					final int b = child.getPaddingBottom();
					IMPL.dispatchChildInsets(child, lp, cgrav, lastInsets);
					child.setPadding(l, t, r, b);
				} else {
					IMPL.applyMarginInsets(lp, cgrav, lastInsets);
					lp.leftMargin += lp.leftMargin2;
					lp.topMargin += lp.topMargin2;
					lp.rightMargin += lp.rightMargin2;
					lp.bottomMargin += lp.bottomMargin2;
				}
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE || ViewCompat.getFitsSystemWindows(child)) {
				continue;
			}
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();
			lp.leftMargin -= lp.leftMargin2;
			lp.topMargin -= lp.topMargin2;
			lp.rightMargin -= lp.rightMargin2;
			lp.bottomMargin -= lp.bottomMargin2;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE || ViewCompat.getFitsSystemWindows(child)) {
				continue;
			}
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();
			lp.leftMargin += lp.leftMargin2;
			lp.topMargin += lp.topMargin2;
			lp.rightMargin += lp.rightMargin2;
			lp.bottomMargin += lp.bottomMargin2;
		}
		super.onLayout(changed, left, top, right, bottom);

		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE || ViewCompat.getFitsSystemWindows(child)) {
				continue;
			}
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();
			lp.leftMargin -= lp.leftMargin2;
			lp.topMargin -= lp.topMargin2;
			lp.rightMargin -= lp.rightMargin2;
			lp.bottomMargin -= lp.bottomMargin2;
		}
	}

	// ----------------------------------------------------------------------
	// The rest of the implementation is for custom per-child layout parameters.
	// If you do not need these (for example you are writing a layout manager
	// that does fixed positioning of its children), you can drop all of this.

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	public static class LayoutParams extends FrameLayout.LayoutParams {

		public int leftMargin2;
		public int topMargin2;
		public int rightMargin2;
		public int bottomMargin2;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(int width, int height, int gravity) {
			super(width, height, gravity);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		@TargetApi(Build.VERSION_CODES.KITKAT)
		public LayoutParams(FrameLayout.LayoutParams source) {
			super(source);
		}
	}
}