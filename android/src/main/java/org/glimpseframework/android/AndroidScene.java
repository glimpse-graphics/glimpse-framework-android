package org.glimpseframework.android;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.glimpseframework.android.shader.parameters.converters.AndroidShaderParameterAdapter;
import org.glimpseframework.api.Scene;
import org.glimpseframework.api.shader.ShaderProgram;
import org.glimpseframework.api.shader.parameters.converters.ShaderParameterAdapter;

/**
 * Android implementation of OpenGL scene.
 * @author Slawomir Czerwinski
 */
public class AndroidScene extends Scene implements GLSurfaceView.Renderer {

	@Override
	protected ShaderParameterAdapter getAdapter(@NonNull ShaderProgram shaderProgram) {
		adapter.setShaderProgram(shaderProgram);
		return adapter;
	}

	@Override
	@CallSuper
	protected void onCreate() {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
	}

	@Override
	@CallSuper
	protected void onResize(int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	@CallSuper
	protected void onPreRender() {
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public final void onSurfaceCreated(GL10 gl, EGLConfig config) {
		onCreate();
	}

	@Override
	public final void onSurfaceChanged(GL10 gl, int width, int height) {
		onResize(width, height);
	}

	@Override
	public final void onDrawFrame(GL10 gl) {
		render();
	}

	private AndroidShaderParameterAdapter adapter = new AndroidShaderParameterAdapter();
}
