package glimpse.android.gles.delegates

import android.opengl.GLES20
import kotlin.reflect.KProperty

/**
 * GLES enable/disable flag property delegate.
 *
 * @property key GLES enable/disable flag key.
 */
internal class EnableDisableDelegate(val key: Int) {

	/**
	 * Gets delegated property value.
	 */
	operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
			GLES20.glIsEnabled(key)

	/**
	 * Sets delegated property value.
	 */
	operator fun setValue(thisRef: Any?, property: KProperty<*>, enabled: Boolean) =
			if (enabled) GLES20.glEnable(key)
			else GLES20.glDisable(key)
}
