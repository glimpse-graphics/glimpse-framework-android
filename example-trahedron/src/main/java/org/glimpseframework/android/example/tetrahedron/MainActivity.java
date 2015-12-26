package org.glimpseframework.android.example.tetrahedron;

import android.app.Activity;
import android.os.Bundle;
import org.glimpseframework.android.GlimpseView;

/**
 * Basic tetrahedron example with GlimpseFramework.
 * @author Slawomir Czerwinski
 */
public class MainActivity extends Activity {

	/**
	 * Initializes {@link GlimpseView} with a scene.
	 * @param savedInstanceState data saved in {@code onSaveInstanceState()} method
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = (GlimpseView) findViewById(R.id.android_scene_view);
		view.setScene(new TetrahedronScene(this));
	}

	/**
	 * Calls {@code onResume()} method of {@link GlimpseView} to resume OpenGL renderer thread.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		view.onResume();
	}

	/**
	 * Calls {@code onPause()} method of {@link GlimpseView} to pause OpenGL renderer thread.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		view.onPause();
	}

	private GlimpseView view;
}
