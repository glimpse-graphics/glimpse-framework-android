package glimpse.android.gles

import android.opengl.GLES20
import glimpse.gles.BlendFactor
import glimpse.gles.CullFaceMode
import glimpse.gles.DepthTestFunction
import glimpse.shaders.ShaderType
import glimpse.textures.TextureMagnificationFilter
import glimpse.textures.TextureMinificationFilter
import glimpse.textures.TextureWrapping

internal val depthTestFunctionMapping = mapOf(
		DepthTestFunction.NEVER to GLES20.GL_NEVER,
		DepthTestFunction.LESS to GLES20.GL_LESS,
		DepthTestFunction.EQUAL to GLES20.GL_EQUAL,
		DepthTestFunction.LESS_OR_EQUAL to GLES20.GL_LEQUAL,
		DepthTestFunction.GREATER to GLES20.GL_GREATER,
		DepthTestFunction.NOT_EQUAL to GLES20.GL_NOTEQUAL,
		DepthTestFunction.GREATER_OR_EQUAL to GLES20.GL_GEQUAL,
		DepthTestFunction.ALWAYS to GLES20.GL_ALWAYS)

internal val blendFactorMapping = mapOf(
		BlendFactor.ZERO to GLES20.GL_ZERO,
		BlendFactor.ONE to GLES20.GL_ONE,
		BlendFactor.SRC_COLOR to GLES20.GL_SRC_COLOR,
		BlendFactor.ONE_MINUS_SRC_COLOR to GLES20.GL_ONE_MINUS_SRC_COLOR,
		BlendFactor.DST_COLOR to GLES20.GL_DST_COLOR,
		BlendFactor.ONE_MINUS_DST_COLOR to GLES20.GL_ONE_MINUS_DST_COLOR,
		BlendFactor.SRC_ALPHA to GLES20.GL_SRC_ALPHA,
		BlendFactor.ONE_MINUS_SRC_ALPHA to GLES20.GL_ONE_MINUS_SRC_ALPHA,
		BlendFactor.DST_ALPHA to GLES20.GL_DST_ALPHA,
		BlendFactor.ONE_MINUS_DST_ALPHA to GLES20.GL_ONE_MINUS_DST_ALPHA,
		BlendFactor.CONSTANT_COLOR to GLES20.GL_CONSTANT_COLOR,
		BlendFactor.ONE_MINUS_CONSTANT_COLOR to GLES20.GL_ONE_MINUS_CONSTANT_COLOR,
		BlendFactor.CONSTANT_ALPHA to GLES20.GL_CONSTANT_ALPHA,
		BlendFactor.ONE_MINUS_CONSTANT_ALPHA to GLES20.GL_ONE_MINUS_CONSTANT_ALPHA)

internal val cullFaceModeMapping = mapOf(
		CullFaceMode.BACK to GLES20.GL_BACK,
		CullFaceMode.FRONT to GLES20.GL_FRONT,
		CullFaceMode.FRONT_AND_BACK to GLES20.GL_FRONT_AND_BACK)

internal val textureMinificationFilterMapping = mapOf(
		TextureMinificationFilter.NEAREST to GLES20.GL_NEAREST,
		TextureMinificationFilter.LINEAR to GLES20.GL_LINEAR,
		TextureMinificationFilter.NEAREST_MIPMAP_NEAREST to GLES20.GL_NEAREST_MIPMAP_NEAREST,
		TextureMinificationFilter.LINEAR_MIPMAP_NEAREST to GLES20.GL_LINEAR_MIPMAP_NEAREST,
		TextureMinificationFilter.NEAREST_MIPMAP_LINEAR to GLES20.GL_NEAREST_MIPMAP_LINEAR,
		TextureMinificationFilter.LINEAR_MIPMAP_LINEAR to GLES20.GL_LINEAR_MIPMAP_LINEAR)

internal val textureMagnificationFilterMapping = mapOf(
		TextureMagnificationFilter.NEAREST to GLES20.GL_NEAREST,
		TextureMagnificationFilter.LINEAR to GLES20.GL_LINEAR)

internal val textureWrappingMapping = mapOf(
		TextureWrapping.REPEAT to GLES20.GL_REPEAT,
		TextureWrapping.MIRRORED_REPEAT to GLES20.GL_MIRRORED_REPEAT,
		TextureWrapping.CLAMP_TO_EDGE to GLES20.GL_CLAMP_TO_EDGE)

internal val shaderTypeMapping = mapOf(
		ShaderType.VERTEX to GLES20.GL_VERTEX_SHADER,
		ShaderType.FRAGMENT to GLES20.GL_FRAGMENT_SHADER)
