package glimpse.android.io

import android.content.Context
import glimpse.models.Mesh
import glimpse.models.loadObjMeshes
import glimpse.textures.Texture
import glimpse.textures.TextureBuilder
import glimpse.textures.loadTexture
import java.io.InputStream

/**
 * Android raw resource.
 *
 * @property context Android context.
 * @property id Resource ID.
 */
class RawResource(val context: Context, val id: Int) {

	/**
	 * Newly opened resource input stream.
	 */
	val inputStream: InputStream get() = context.resources.openRawResource(id)

	/**
	 * Lines in the resource file.
	 */
	val lines: List<String> by lazy { inputStream.reader().readLines() }

	/**
	 * Builds three-dimensional meshes from OBJ file resource.
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
 * Returns an Android raw resource with the given [id] in this [Context].
 */
fun Context.rawResource(id: Int) = RawResource(this, id)
