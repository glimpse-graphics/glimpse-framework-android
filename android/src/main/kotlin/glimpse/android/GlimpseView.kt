package glimpse.android

import android.app.ActivityManager
import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.view.ViewManager
import glimpse.gles.GLES
import glimpse.gles.Viewport
import org.jetbrains.anko.custom.ankoView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Glimpse Framework [GLSurfaceView] implementation.
 */
class GlimpseView(context: Context) : GLSurfaceView(context) {

	private val gles: GLES = glimpse.android.gles.GLES

	private var init: GLES.() -> Unit = {}
	private var reshape: GLES.(viewport: Viewport) -> Unit = {}
	private var display: GLES.() -> Unit = {}
	private var dispose: GLES.() -> Unit = {}

	init {
		if (!isInEditMode) {
			require(isGLES20Supported()) { "OpenGL ES 2.0 not supported" }
			initGLES();
		}
	}

	private fun isGLES20Supported(): Boolean {
		val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
		return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000
	}

	private fun initGLES() {
		setZOrderOnTop(true)
		setEGLContextClientVersion(2)
		setEGLConfigChooser(8, 8, 8, 8, 16, 0)
		getHolder().setFormat(PixelFormat.TRANSLUCENT)
		setRenderer(Renderer())
	}

	/**
	 * GL initialization lambda.
	 */
	fun onInit(init: GLES.() -> Unit) {
		this.init = init
	}

	/**
	 * GL resize lambda.
	 *
	 * @param viewport Rendering viewport.
	 */
	fun onResize(reshape: GLES.(viewport: Viewport) -> Unit) {
		this.reshape = reshape
	}

	/**
	 * GL rendering lambda.
	 */
	fun onRender(display: GLES.() -> Unit) {
		this.display = display
	}

	/**
	 * GL dispose lambda.
	 */
	fun onDispose(dispose: GLES.() -> Unit) {
		this.dispose = dispose
	}

	private inner class Renderer : GLSurfaceView.Renderer {

		override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
			gles.init()
		}

		override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
			gles.reshape(Viewport(width, height))
		}

		override fun onDrawFrame(gl: GL10?) {
			gles.display()
		}
	}
}

inline fun ViewManager.glimpseView(theme: Int = 0, init: GlimpseView.() -> Unit): GlimpseView =
		ankoView({ ctx: Context -> GlimpseView(ctx) }, theme) { init() }
