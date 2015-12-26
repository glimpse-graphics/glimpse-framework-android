package org.glimpseframework.android;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * GlimpseFramework scene view.
 * @author Slawomir Czerwinski
 */
public class GlimpseView extends GLSurfaceView {

	/**
	 * Creates a new GlimpseFramework scene view.
	 * @param context Android context
	 */
	public GlimpseView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		if (isInEditMode()) {
			return;
		}
		if (isGLES20Supported(context)) {
			initGLES();
		} else {
			throw new UnsupportedOperationException("OpenGL ES 2.0 not supported.");
		}
	}

	private boolean isGLES20Supported(Context context) {
		final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
	}

	private void initGLES() {
		setZOrderOnTop(true);
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	/**
	 * Creates a new GlimpseFramework scene view.
	 * @param context Android context
	 * @param attrs attributes from XML layout resource file
	 */
	public GlimpseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * Sets scene to be rendered in the view.
	 * @param scene Android scene
	 */
	public void setScene(@NonNull AndroidScene scene) {
		setRenderer(scene);
	}
}
