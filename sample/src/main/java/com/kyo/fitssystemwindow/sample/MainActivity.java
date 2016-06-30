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
package com.kyo.fitssystemwindow.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.kyo.fitssystemwindow.OnKeyboardVisibleChangedListener;
import com.kyo.fitssystemwindow.FitsSystemWindowsFrameLayout2;

public class MainActivity extends AppCompatActivity {

	private FitsSystemWindowsFrameLayout2 windowInsetsFrameLayout;
	private TextView keyboardState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		keyboardState = (TextView) findViewById(R.id.keyboardState);
		keyboardState.setText("closed");
		windowInsetsFrameLayout = (FitsSystemWindowsFrameLayout2) findViewById(R.id.windowInsetsFrameLayout);
		windowInsetsFrameLayout.setOnKeyboardVisibleChangedListener(new OnKeyboardVisibleChangedListener() {
			@Override
			public void onKeyboardVisibleChanged(boolean visible, int height) {
				String state = (visible == true ? "open" : "closed");
				keyboardState.setText(state);
			}
		});
	}
}
