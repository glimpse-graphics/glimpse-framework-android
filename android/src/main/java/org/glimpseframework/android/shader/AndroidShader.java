package org.glimpseframework.android.shader;

import android.opengl.GLES20;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.glimpseframework.api.shader.Shader;
import org.glimpseframework.api.shader.ShaderCompileException;
import org.glimpseframework.api.shader.parameters.Parameter;

public class AndroidShader implements Shader {

	public AndroidShader(Type type, String source) {
		this.type = type;
		this.source = source;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void compile() throws ShaderCompileException {
		handle = GLES20.glCreateShader(SHADER_TYPE_MAPPING.get(type));
		if (handle != 0) {
			GLES20.glShaderSource(handle, source);
			GLES20.glCompileShader(handle);
			verifyCompileStatus();
		}
	}

	private void verifyCompileStatus() throws ShaderCompileException {
		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		if (compileStatus[0] == 0) {
			String errorMessage = GLES20.glGetShaderInfoLog(handle);
			delete();
			throw new ShaderCompileException(errorMessage);
		}
	}

	@Override
	public Set<Parameter> getParameters() {
		return new HashSet<Parameter>(parameters);
	}

	@Override
	public void delete() {
		GLES20.glDeleteShader(handle);
	}

	int getHandle() {
		return handle;
	}

	private static final Map<Type, Integer> SHADER_TYPE_MAPPING = new EnumMap<Type, Integer>(Type.class);
	static {
		SHADER_TYPE_MAPPING.put(Type.VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
		SHADER_TYPE_MAPPING.put(Type.FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
	}

	private Type type;
	private String source;

	private Set<Parameter> parameters;

	private int handle;
}
