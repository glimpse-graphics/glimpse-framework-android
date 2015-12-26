package org.glimpseframework.android.shader;

import android.support.annotation.NonNull;
import org.glimpseframework.api.shader.Shader;
import org.glimpseframework.api.shader.ShaderProgramBuilder;

/**
 * Android implementation of OpenGL shader program builder.
 * @author Slawomir Czerwinski
 */
public class AndroidShaderProgramBuilder extends ShaderProgramBuilder<AndroidShader, AndroidShaderProgram> {

	@Override
	public ShaderProgramBuilder setSource(@NonNull Shader.Type shaderType, @NonNull String source) {
		AndroidShader shader = new AndroidShader(shaderType, source);
		shader.setParameters(parseParameters(source));
		addShader(shader);
		return this;
	}

	@Override
	@NonNull
	protected AndroidShaderProgram createProgram() {
		return new AndroidShaderProgram(getShaders().toArray(new AndroidShader[Shader.Type.values().length]));
	}
}
