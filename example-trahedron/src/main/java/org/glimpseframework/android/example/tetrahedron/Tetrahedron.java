package org.glimpseframework.android.example.tetrahedron;

import java.nio.FloatBuffer;
import org.glimpseframework.api.annotations.Attribute;
import org.glimpseframework.api.models.Model;
import org.glimpseframework.api.primitives.Point;
import org.glimpseframework.api.primitives.vbo.FloatVBO;
import org.glimpseframework.api.shader.ShaderProgram;

public class Tetrahedron implements Model {

	public Tetrahedron(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		initVertices();
	}

	private void initVertices() {
		FloatBuffer buffer = FloatBuffer.allocate(FACES.length * 3);
		for (int vertex : FACES) {
			buffer.put(VERTICES[vertex].get3f());
		}
		vertices = new FloatVBO(buffer.array(), 3);
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

	private ShaderProgram shaderProgram;

	@Attribute(name = "a_VertexPosition")
	private FloatVBO vertices;
}
