package glimpse.android.io

import android.content.Context
import glimpse.models.Mesh
import glimpse.models.loadObjMeshes
import glimpse.textures.Texture
import glimpse.textures.TextureBuilder
import glimpse.textures.loadTexture
import java.io.InputStream

/**
 * Android asset.
 *
 * @property context Android context.
 * @property name Asset name.
 */
class Asset(val context: Context, val name: String) {

	/**
	 * Newly opened asset input stream.
	 */
	val inputStream: InputStream get() = context.assets.open(name)

	/**
	 * Lines in the asset file.
	 */
	val lines: List<String> by lazy { inputStream.reader().readLines() }

	/**
	 * Builds three-dimensional meshes from OBJ file asset.
	 */
	fun loadObjMeshes(): List<Mesh> = lines.loadObjMeshes()

	/**
	 * Builds texture initialized with an [init] function.
	 */
	fun loadTexture(init: TextureBuilder.() -> Unit = {}): Texture =
			inputStream.loadTexture {
				init()
			}
}

/**
 * Returns an Android asset with the given [name] in this [Context].
 */
fun Context.asset(name: String) = Asset(this, name)
