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
