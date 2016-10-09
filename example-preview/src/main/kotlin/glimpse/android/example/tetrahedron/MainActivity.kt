package glimpse.android.example.tetrahedron

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import glimpse.Color
import glimpse.Vector
import glimpse.android.GlimpseView
import glimpse.android.glimpseView
import glimpse.android.io.asset
import glimpse.cameras.camera
import glimpse.cameras.perspective
import glimpse.cameras.targeted
import glimpse.degrees
import glimpse.gles.Disposables
import glimpse.lights.Light
import glimpse.lights.directionLight
import glimpse.materials.Material
import glimpse.materials.Plastic
import glimpse.materials.Textured
import glimpse.models.*
import glimpse.textures.Texture
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.relativeLayout
import java.util.*

class MainActivity : AppCompatActivity() {

	val LOG_TAG = "Glimpse Preview"

	lateinit var glimpseView: GlimpseView

	var aspect: Float = 1.333f

	val camera = camera {
		targeted {
			position { Vector(5f, 60.degrees, 0.degrees).toPoint() }
		}
		perspective {
			fov { 60.degrees }
			aspect { aspect }
			distanceRange(1f to 20f)
		}
	}

	val tetrahedron = transform(tetrahedron())
	val cube = transform(cube())
	val sphere = transform(sphere(12))

	var model = tetrahedron

	val textures = mutableMapOf<Textured.TextureType, Texture>()

	val texturedMaterial = Textured { textures[it]!! }
	val plasticMaterial = Plastic(Color.GREEN)

	var material: Material = plasticMaterial

	val lights = listOf<Light>(
			directionLight {
				direction { Vector(-1f, -1f, 0f) }
			})

	var selectedModel = R.id.menu_model_tetrahedron
	var selectedMaterial = R.id.menu_material_plastic

	override fun onCreate(savedInstanceState: Bundle?) {
		selectedModel = savedInstanceState?.getInt("selectedModel") ?: R.id.menu_model_tetrahedron
		selectedMaterial = savedInstanceState?.getInt("selectedMaterial") ?: R.id.menu_material_plastic
		Log.d(LOG_TAG, "$selectedModel made of $selectedMaterial")
		super.onCreate(savedInstanceState)
		relativeLayout {
			glimpseView = glimpseView {
				onInit {
					Log.d(LOG_TAG, "Initializing")
					clearColor = Color.BLACK transparent 0f
					isDepthTest = true
					textures[Textured.TextureType.AMBIENT] = asset("ambient.png").loadTexture { withMipmap() }
					textures[Textured.TextureType.DIFFUSE] = asset("diffuse.png").loadTexture { withMipmap() }
					textures[Textured.TextureType.SPECULAR] = asset("specular.png").loadTexture { withMipmap() }
				}
				onResize { v ->
					Log.d(LOG_TAG, "Resizing")
					viewport = v
					aspect = viewport.aspect
				}
				onRender {
					clearColorBuffer()
					clearDepthBuffer()
					try {
						material.render(model, camera, lights)
					} catch (e : Exception) {
						Log.e(LOG_TAG, "Rendering error", e)
						finish()
					}
				}
				onDispose {
					Log.d(LOG_TAG, "Disposing")
				}
			}.lparams(width = matchParent, height = matchParent)
		}
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main, menu)
		menu?.findItem(selectedModel)?.isChecked = true
		menu?.findItem(selectedMaterial)?.isChecked = true
		onOptionsItemSelected(menu?.findItem(selectedModel))
		onOptionsItemSelected(menu?.findItem(selectedMaterial))
		return true
	}

	override fun onResume() {
		super.onResume()
		glimpseView.onResume()
	}

	override fun onPause() {
		super.onPause()
		glimpseView.onPause()
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		Log.d(LOG_TAG, "$selectedModel made of $selectedMaterial")
		outState?.putInt("selectedModel", selectedModel)
		outState?.putInt("selectedMaterial", selectedMaterial)
		super.onSaveInstanceState(outState)
	}

	override fun onDestroy() {
		super.onDestroy()
		Disposables.disposeAll()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.groupId) {
		R.id.menu_model -> selectModel(item!!, item.itemId)
		R.id.menu_material -> selectMaterial(item!!, item.itemId)
		else -> super.onOptionsItemSelected(item)
	}

	private fun selectModel(item: MenuItem, id: Int): Boolean {
		selectedModel = id
		item.isChecked = true
		model = when (id) {
			R.id.menu_model_tetrahedron -> tetrahedron
			R.id.menu_model_cube -> cube
			R.id.menu_model_sphere -> sphere
			else -> mesh {}.transform {}
		}
		return true
	}

	private fun selectMaterial(item: MenuItem, id: Int): Boolean {
		selectedMaterial = id
		item.isChecked = true
		material = when (id) {
			R.id.menu_material_plastic -> plasticMaterial
			R.id.menu_material_textured -> texturedMaterial
			else -> plasticMaterial
		}
		return true
	}

	private fun transform(mesh: Mesh): Model = mesh.transform {
		val time = (Date().time / 50L) % 360L
		rotateZ(time.degrees)
		rotateX(-23.5.degrees)
	}
}