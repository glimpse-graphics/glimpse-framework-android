package org.glimpseframework.android.shader.parameters.converters;

import android.opengl.GLES20;
import java.nio.Buffer;
import org.glimpseframework.api.primitives.vbo.VBO;
import org.glimpseframework.api.shader.ShaderProgram;
import org.glimpseframework.api.shader.parameters.converters.ShaderParameterAdapter;

public class AndroidShaderParameterAdapter extends ShaderParameterAdapter {

	public AndroidShaderParameterAdapter(ShaderProgram shaderProgram) {
		super(shaderProgram);
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
}
