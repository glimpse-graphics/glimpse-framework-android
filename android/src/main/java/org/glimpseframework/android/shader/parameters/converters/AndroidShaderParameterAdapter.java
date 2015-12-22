package org.glimpseframework.android.shader.parameters.converters;

import android.opengl.GLES20;
import java.nio.Buffer;
import org.glimpseframework.api.primitives.vbo.VBO;
import org.glimpseframework.api.shader.ShaderProgram;
import org.glimpseframework.api.shader.parameters.converters.InvalidNumberOfValuesException;
import org.glimpseframework.api.shader.parameters.converters.ShaderParameterAdapter;

public class AndroidShaderParameterAdapter extends ShaderParameterAdapter {

	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	@Override
	public void vertexAttributeBuffer(
			String parameterName, int vectorSize, VBO.VBOType type, boolean normalized, int stride, Buffer buffer) {
		int elementType;
		switch (type) {
			case BYTE:
				elementType = GLES20.GL_BYTE;
				break;
			case INT:
				elementType = GLES20.GL_INT;
				break;
			case FLOAT:
				elementType = GLES20.GL_FLOAT;
				break;
			default:
				elementType = 0;
		}
		GLES20.glVertexAttribPointer(
				shaderProgram.getAttributeLocation(parameterName), vectorSize, elementType, normalized, stride, buffer);
	}

	@Override
	public void uniformMatrix4(String parameterName, int count, boolean transpose, float[] value, int offset) {
		GLES20.glUniformMatrix4fv(shaderProgram.getUniformLocation(parameterName), count, transpose, value, offset);
	}

	@Override
	public void uniform(String parameterName, int... values) {
		switch (values.length) {
			case 1:
				GLES20.glUniform1i(shaderProgram.getUniformLocation(parameterName), values[0]);
				break;
			case 2:
				GLES20.glUniform2i(shaderProgram.getUniformLocation(parameterName), values[0], values[1]);
				break;
			case 3:
				GLES20.glUniform3i(shaderProgram.getUniformLocation(parameterName), values[0], values[1], values[2]);
				break;
			case 4:
				GLES20.glUniform4i(shaderProgram.getUniformLocation(parameterName), values[0], values[1], values[2], values[3]);
				break;
			default:
				throwInvalidNumberOfValuesException(values.length);
		}
	}

	private void throwInvalidNumberOfValuesException(int actual) {
		throw new InvalidNumberOfValuesException(actual, 1, 4);
	}

	@Override
	public void uniform(String parameterName, float... values) {
		switch (values.length) {
			case 1:
				GLES20.glUniform1f(shaderProgram.getUniformLocation(parameterName), values[0]);
				break;
			case 2:
				GLES20.glUniform2f(shaderProgram.getUniformLocation(parameterName), values[0], values[1]);
				break;
			case 3:
				GLES20.glUniform3f(shaderProgram.getUniformLocation(parameterName), values[0], values[1], values[2]);
				break;
			case 4:
				GLES20.glUniform4f(shaderProgram.getUniformLocation(parameterName), values[0], values[1], values[2], values[3]);
				break;
			default:
				throwInvalidNumberOfValuesException(values.length);
		}
	}

	@Override
	public void drawTriangles(int numberOfVertices) {
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numberOfVertices);
	}

	private ShaderProgram shaderProgram;
}
