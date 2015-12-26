package org.glimpseframework.android.example.tetrahedron;

import android.app.Activity;
import android.os.Bundle;
import org.glimpseframework.android.GlimpseView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = (GlimpseView) findViewById(R.id.android_scene_view);
		view.setScene(new TetrahedronScene(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		view.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		view.onPause();
	}

	private GlimpseView view;
}
