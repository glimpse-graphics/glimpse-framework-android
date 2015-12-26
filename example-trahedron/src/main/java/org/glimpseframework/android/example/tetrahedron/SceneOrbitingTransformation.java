package org.glimpseframework.android.example.tetrahedron;

import java.util.Date;
import org.glimpseframework.api.annotations.Uniform;
import org.glimpseframework.api.primitives.Angle;
import org.glimpseframework.api.primitives.Matrix;
import org.glimpseframework.api.primitives.Point;
import org.glimpseframework.api.primitives.Vector;

/**
 * Scene orbiting transformation.
 * @author Slawomir Czerwinski
 */
public class SceneOrbitingTransformation {

	/**
	 * Creates a new transformation.
	 */
	public SceneOrbitingTransformation() {
		updateProjection(100, 100);
		update();
	}

	/**
	 * Updates transformation matrix.
	 */
	public void update() {
		long time = new Date().getTime();
		Angle polarAngle = Angle.fromDegrees(90 + (float) (60 * Math.sin(0.001 * time)));
		Angle azimuthAngle = Angle.fromDegrees((0.1f * (time % 3600)));
		Point eye = Point.ORIGIN.translate(Vector.fromSphericalCoordinates(EYE_DISTANCE, polarAngle, azimuthAngle));
		mvpMatrix = projectionMatrix.multiply(Matrix.getLookAtMatrix(eye, Point.ORIGIN, Vector.Z_UNIT_VECTOR));
	}

	/**
	 * Updates projection matrix.
	 * @param width viewport width
	 * @param height viewport height
	 */
	public void updateProjection(int width, int height) {
		float aspect = ((float) width) / ((float) height);
		projectionMatrix = Matrix.getPerspectiveMatrix(Angle.fromDegrees(60), aspect, 1.0f, 100.0f);
	}

	private static final float EYE_DISTANCE = 5.0f;

	private Matrix projectionMatrix = Matrix.getPerspectiveMatrix(Angle.fromDegrees(50), 1.0f, 1.0f, 10.0f);

	/**
	 * Model-view-projection matrix.
	 */
	@Uniform(name="u_MVPMatrix")
	private Matrix mvpMatrix;
}
