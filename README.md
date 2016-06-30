# FitsSystemWindowsFrameLayout

This FitsSystemWindowsFrameLayout is a layout to use android:fitsSystemWindows="true" easily.

![](https://github.com/KyoSherlock/FitsSystemWindowsFrameLayout/raw/master/screenshots/screenshot1.png)

# Usage

Below is an example of a FitsSystemWindowsFrameLayout. You need to set android:fitsSystemWindows="true" to FitsSystemWindowsFrameLayout and its children.

```xml
<com.kyo.fitssystemwindow.FitsSystemWindowsFrameLayout
	android:id="@+id/windowInsetsFrameLayout"
	xmlns:android="http://schemas.android.com/apk/res/android"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:fitsSystemWindows="true">

	<ImageView
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:fitsSystemWindows="true"
		android:scaleType="centerCrop"
		android:src="@drawable/background"/>

	<View
		android:layout_width="100dp"
		android:layout_height="100dp"
		android:background="#F06292"/>

</com.kyo.fitssystemwindow.FitsSystemWindowsFrameLayout>
```

And set a transparent theme for the activity.

```xml
	<style name="AppTheme.TransparentStatusBar">
		<item name="android:windowDrawsSystemBarBackgrounds">true</item>
		<item name="android:statusBarColor">@android:color/transparent</item>
	</style>
```

If you are interesting on listening the open state of the keyboard, you can use the FitsSystemWindowsFrameLayout2 and set android:windowSoftInputMode="adjustResize to the activity.

```java
windowInsetsFrameLayout.setOnKeyboardVisibleChangedListener(new OnKeyboardVisibleChangedListener() {
	@Override
	public void onKeyboardVisibleChanged(boolean visible, int height) {
		// do someting
	}
});
```

# Changelog

### Version: 1.0
  * Initial Build
  
# License

    Copyright 2016, KyoSherlock
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.