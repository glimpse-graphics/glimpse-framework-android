package org.glimpseframework.android.shader;

import android.opengl.GLES20;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.glimpseframework.api.shader.Shader;
import org.glimpseframework.api.shader.ShaderProgram;
import org.glimpseframework.api.shader.ShaderProgramLinkException;
import org.glimpseframework.api.shader.parameters.Parameter;

class AndroidShaderProgram implements ShaderProgram {

	AndroidShaderProgram(AndroidShader... shaders) {
		for (AndroidShader shader : shaders) {
			this.shaders.put(shader.getType(), shader);
		}
	}

	@Override
	public void link() throws ShaderProgramLinkException {
		handle = GLES20.glCreateProgram();
		if (handle != 0) {
			for (AndroidShader shader : shaders.values()) {
				GLES20.glAttachShader(handle, shader.getHandle());
			}
			GLES20.glLinkProgram(handle);
			verifyLinkStatus();
		}
	}

	private void verifyLinkStatus() throws ShaderProgramLinkException {
		final int[] linkStatus = new int[1];
		GLES20.glGetProgramiv(handle, GLES20.GL_LINK_STATUS, linkStatus, 0);
		if (linkStatus[0] == 0) {
			String errorMessage = GLES20.glGetProgramInfoLog(handle);
			delete();
			throw new ShaderProgramLinkException(errorMessage);
		}
	}

	@Override
	public Set<Parameter> getParameters() {
		Set<Parameter> parameters = new HashSet<Parameter>();
		for (AndroidShader shader : shaders.values()) {
			parameters.addAll(shader.getParameters());
		}
		return parameters;
	}

	@Override
	public void use() {
		GLES20.glUseProgram(handle);
	}

	@Override
	public void delete() {
		GLES20.glDeleteProgram(handle);
		for (AndroidShader shader : shaders.values()) {
			shader.delete();
		}
	}

	@Override
	public int getAttributeLocation(String name) {
		return GLES20.glGetAttribLocation(handle, name);
	}

	@Override
	public int getUniformLocation(String name) {
		return GLES20.glGetUniformLocation(handle, name);
	}

	private Map<Shader.Type, AndroidShader> shaders = new EnumMap<Shader.Type, AndroidShader>(Shader.Type.class);

	private int handle;
}
