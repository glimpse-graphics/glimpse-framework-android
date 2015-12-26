package org.glimpseframework.android.example.tetrahedron;

import android.support.annotation.NonNull;
import java.nio.FloatBuffer;
import org.glimpseframework.api.annotations.Attribute;
import org.glimpseframework.api.models.Model;
import org.glimpseframework.api.primitives.Point;
import org.glimpseframework.api.primitives.vbo.FloatVBO;
import org.glimpseframework.api.shader.ShaderProgram;

/**
 * Tetrahedron model.
 * @author Slawomir Czerwinski
 */
public final class Tetrahedron implements Model {

	/**
	 * Creates a tetrahedron model.
	 * @param shaderProgram shader program used to render this model
	 */
	public Tetrahedron(@NonNull ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		vertices = new FloatVBO(initVertices(), 3);
	}

	private float[] initVertices() {
		FloatBuffer buffer = FloatBuffer.allocate(FACES.length * 3);
		for (int vertex : FACES) {
			buffer.put(VERTICES[vertex].get3f());
		}
		return buffer.array();
	}

	@Override
	public int getNumberOfVertices() {
		return FACES.length;
	}

	@Override
	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	private static final Point[] VERTICES = {
			new Point(1, 1, 1), new Point(1, -1, -1), new Point(-1, 1, -1), new Point(-1, -1, 1)
	};
	private static final int[] FACES = { 0, 1, 2, 1, 2, 3, 2, 3, 0, 3, 0, 1 };

	private final ShaderProgram shaderProgram;

	/**
	 * VBO containing positions of vertices to be rendered.
	 */
	@Attribute(name = "a_VertexPosition")
	private final FloatVBO vertices;
}
