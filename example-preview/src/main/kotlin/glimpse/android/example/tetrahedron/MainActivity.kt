package glimpse.android.example.tetrahedron

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import glimpse.Color
import glimpse.Vector
import glimpse.android.GlimpseView
import glimpse.android.glimpseView
import glimpse.cameras.camera
import glimpse.cameras.perspective
import glimpse.cameras.targeted
import glimpse.degrees
import glimpse.lights.Light
import glimpse.materials.Material
import glimpse.materials.Plastic
import glimpse.materials.Textured
import glimpse.models.*
import glimpse.textures.Texture
import glimpse.textures.mipmap
import glimpse.textures.readTexture
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.relativeLayout
import java.util.*

class MainActivity : AppCompatActivity() {

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

	val lights = listOf(Light.DirectionLight(Vector(-1f, -1f, 0f)))

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		relativeLayout {
			glimpseView = glimpseView {
				onInit {
					clearColor = Color.BLACK transparent 0f
					isDepthTest = true
					textures[Textured.TextureType.AMBIENT] = assets.open("ambient.png").readTexture { name = "ambient.png" with mipmap }
					textures[Textured.TextureType.DIFFUSE] = assets.open("diffuse.png").readTexture { name = "diffuse.png" with mipmap }
					textures[Textured.TextureType.SPECULAR] = assets.open("specular.png").readTexture { name = "specular.png" with mipmap }
				}
				onResize { v ->
					viewport = v
					aspect = viewport.aspect
				}
				onRender {
					clearColorBuffer()
					clearDepthBuffer()
					try {
						material.render(model, camera, lights)
					} catch (e : Exception) {
						Log.e("GLIMPSE", "Rendering error", e)
					}
				}
				onDispose {
				}
			}.lparams(width = matchParent, height = matchParent)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main, menu)
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

	override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
		R.id.menu_model_tetrahedron -> {
			model = tetrahedron
			item?.isChecked = true
			true
		}
		R.id.menu_model_cube -> {
			model = cube
			item?.isChecked = true
			true
		}
		R.id.menu_model_sphere -> {
			model = sphere
			item?.isChecked = true
			true
		}
		R.id.menu_material_plastic -> {
			material = plasticMaterial
			item?.isChecked = true
			true
		}
		R.id.menu_material_textured -> {
			material = texturedMaterial
			item?.isChecked = true
			true
		}
		else -> super.onOptionsItemSelected(item)
	}

	private fun transform(mesh: Mesh): Model = mesh.transform {
		val time = (Date().time / 50L) % 360L
		rotateZ(time.degrees)
		rotateX(-23.5.degrees)
	}
}