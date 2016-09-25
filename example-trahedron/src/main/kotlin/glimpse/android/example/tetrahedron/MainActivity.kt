package glimpse.android.example.tetrahedron

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import glimpse.Color
import glimpse.Vector
import glimpse.android.GlimpseView
import glimpse.android.glimpseView
import glimpse.cameras.camera
import glimpse.cameras.perspective
import glimpse.cameras.targeted
import glimpse.degrees
import glimpse.gles.BlendFactor
import glimpse.gles.DepthTestFunction
import glimpse.lights.Light
import glimpse.materials.Material
import glimpse.materials.Plastic
import glimpse.materials.Textured
import glimpse.models.Mesh
import glimpse.models.Model
import glimpse.models.tetrahedron
import glimpse.textures.Texture
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
			fov { 45.degrees }
			aspect { aspect }
			distanceRange(1f to 20f)
		}
	}

	var model = transform(tetrahedron())

	val textures = mutableMapOf<Textured.TextureType, Texture>()

	val plasticMaterial = Plastic(Color.GREEN)

	var material: Material = plasticMaterial

	val lights = listOf(Light.DirectionLight(Vector(-1f, -1f, 0f)))

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		relativeLayout {
			glimpseView = glimpseView {
				onInit {
					clearColor = Color.BLACK transparent 0f
					clearDepth = 1f
					isDepthTest = true
					depthTestFunction = DepthTestFunction.LESS_OR_EQUAL
					isBlend = true
					blendFunction = BlendFactor.SRC_ALPHA to BlendFactor.ONE_MINUS_SRC_ALPHA
					isCullFace = false
				}
				onResize { v ->
					viewport = v
					aspect = viewport.aspect
				}
				onRender {
					clearColorBuffer()
					clearDepthBuffer()
					material.render(model, camera, lights)
				}
				onDispose {
				}
			}.lparams(width = matchParent, height = matchParent)
		}
	}

	override fun onResume() {
		super.onResume()
		glimpseView.onResume()
	}

	override fun onPause() {
		super.onPause()
		glimpseView.onPause()
	}

	private fun transform(mesh: Mesh): Model = mesh.transform {
		val time = (Date().time / 50L) % 360L
		rotateZ(time.degrees)
		rotateX(-23.5.degrees)
	}
}