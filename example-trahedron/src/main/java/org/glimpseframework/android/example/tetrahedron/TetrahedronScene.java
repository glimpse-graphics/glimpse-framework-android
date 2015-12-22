package org.glimpseframework.android.example.tetrahedron;

import android.content.Context;
import java.util.Date;
import org.glimpseframework.android.AndroidScene;
import org.glimpseframework.android.shader.AndroidShaderProgramBuilder;
import org.glimpseframework.android.util.RawResourceLoader;
import org.glimpseframework.api.GlimpseException;
import org.glimpseframework.api.annotations.Uniform;
import org.glimpseframework.api.primitives.Angle;
import org.glimpseframework.api.primitives.Matrix;
import org.glimpseframework.api.primitives.Point;
import org.glimpseframework.api.primitives.Vector;
import org.glimpseframework.api.shader.Shader;
import org.glimpseframework.api.shader.ShaderCompileException;
import org.glimpseframework.api.shader.ShaderProgram;
import org.glimpseframework.api.shader.ShaderProgramLinkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TetrahedronScene extends AndroidScene {

	public TetrahedronScene(Context context) {
		this.context = context;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		try {
			initShaderProgram();
		} catch (GlimpseException e) {
			LOG.error("Shader initialization failed", e);
		}
		initModels();
		initMatrix();
	}

	private void initShaderProgram() throws ShaderCompileException, ShaderProgramLinkException {
		RawResourceLoader rawResourceLoader = new RawResourceLoader(context);
		shaderProgram = new AndroidShaderProgramBuilder()
				.setSource(Shader.Type.VERTEX_SHADER, rawResourceLoader.loadResource(R.raw.glimpse_tetrahedron_vertex))
				.setSource(Shader.Type.FRAGMENT_SHADER, rawResourceLoader.loadResource(R.raw.glimpse_tetrahedron_fragment))
				.build();
	}

	private void initModels() {
		tetrahedron = new Tetrahedron(shaderProgram);
		put(tetrahedron);
	}

	private void initMatrix() {
		recalculateMatrix();
		register(this);
	}

	private void recalculateMatrix() {
		Point eye = Point.ORIGIN.translate(Vector.fromSphericalCoordinates(EYE_DISTANCE, polarAngle, azimuthAngle));
		mvpMatrix = projectionMatrix.multiply(Matrix.getLookAtMatrix(eye, Point.ORIGIN, Vector.Z_UNIT_VECTOR));
	}

	@Override
	protected void onResize(int width, int height) {
		super.onResize(width, height);
		float aspect = ((float) width) / ((float) height);
		projectionMatrix = Matrix.getPerspectiveMatrix(Angle.fromDegrees(60), aspect, 1.0f, 100.0f);
	}

	@Override
	protected void onPostRender() {
		long time = new Date().getTime();
		polarAngle = Angle.fromDegrees(90 + (float) (60 * Math.sin(0.001 * time)));
		azimuthAngle = Angle.fromDegrees((0.1f * (time % 3600)));
		recalculateMatrix();
	}

	private static final Logger LOG = LoggerFactory.getLogger(TetrahedronScene.class);

	private Context context;
	private ShaderProgram shaderProgram;
	private Tetrahedron tetrahedron;

	@Uniform(name="u_MVPMatrix")
	private Matrix mvpMatrix;

	private Matrix projectionMatrix = Matrix.getPerspectiveMatrix(Angle.fromDegrees(60), 1.0f, 1.0f, 10.0f);

	private static final float EYE_DISTANCE = 5.0f;
	private Angle polarAngle = Angle.RIGHT_ANGLE;
	private Angle azimuthAngle = Angle.FULL_ANGLE;
}
