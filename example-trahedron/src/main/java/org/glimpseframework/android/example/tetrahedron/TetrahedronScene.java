package org.glimpseframework.android.example.tetrahedron;

import android.content.Context;
import org.glimpseframework.android.AndroidScene;
import org.glimpseframework.android.shader.AndroidShaderProgramBuilder;
import org.glimpseframework.android.util.RawResourceLoader;
import org.glimpseframework.api.GlimpseException;
import org.glimpseframework.api.shader.Shader;
import org.glimpseframework.api.shader.ShaderProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenGL scene with a single tetrahedron.
 * @author Slawomir Czerwinski
 */
public class TetrahedronScene extends AndroidScene {

	/**
	 * Creates a new scene.
	 * @param context Android context
	 */
	public TetrahedronScene(Context context) {
		this.context = context;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		initShaderProgram();
		initModels();
		initTransformation();
	}

	private void initShaderProgram() {
		try {
			RawResourceLoader rawResourceLoader = new RawResourceLoader(context);
			shaderProgram = new AndroidShaderProgramBuilder()
					.setSource(Shader.Type.VERTEX_SHADER, rawResourceLoader.loadResource(R.raw.glimpse_tetrahedron_vertex))
					.setSource(Shader.Type.FRAGMENT_SHADER, rawResourceLoader.loadResource(R.raw.glimpse_tetrahedron_fragment))
					.build();
		} catch (GlimpseException e) {
			LOG.error("Shader initialization failed", e);
		}
	}

	private void initModels() {
		tetrahedron = new Tetrahedron(shaderProgram);
		put(tetrahedron);
	}

	private void initTransformation() {
		transformation = new SceneOrbitingTransformation();
		register(transformation);
	}

	@Override
	protected void onResize(int width, int height) {
		super.onResize(width, height);
		transformation.updateProjection(width, height);
	}

	@Override
	protected void onPostRender() {
		transformation.update();
	}

	private static final Logger LOG = LoggerFactory.getLogger(TetrahedronScene.class);

	private Context context;

	private ShaderProgram shaderProgram;
	private Tetrahedron tetrahedron;
	private SceneOrbitingTransformation transformation;
}
