package org.glimpseframework.android;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class AndroidSceneView extends GLSurfaceView {

	public AndroidSceneView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
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
		setEGLConfigChooser(8, 8, 8, 8, 0, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	public AndroidSceneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setScene(AndroidScene scene) {
		setRenderer(scene);
	}
}
