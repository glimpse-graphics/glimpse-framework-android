package org.glimpseframework.android.shader;

import org.glimpseframework.api.shader.Shader;
import org.glimpseframework.api.shader.ShaderProgramBuilder;

public class AndroidShaderProgramBuilder extends ShaderProgramBuilder<AndroidShader, AndroidShaderProgram> {

	@Override
	public ShaderProgramBuilder setSource(Shader.Type shaderType, String source) {
		addShader(new AndroidShader(shaderType, source));
		return this;
	}

	@Override
	protected AndroidShaderProgram createProgram() {
		return new AndroidShaderProgram(getShaders().toArray(new AndroidShader[Shader.Type.values().length]));
	}
}
