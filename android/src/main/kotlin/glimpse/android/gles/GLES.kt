package glimpse.android.gles

import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import glimpse.Color
import glimpse.android.gles.delegates.EnableDisableDelegate
import glimpse.gles.*
import glimpse.gles.GLES
import glimpse.gles.delegates.EnumPairSetAndRememberDelegate
import glimpse.gles.delegates.EnumSetAndRememberDelegate
import glimpse.gles.delegates.SetAndRememberDelegate
import glimpse.shaders.ProgramHandle
import glimpse.shaders.ShaderHandle
import glimpse.shaders.ShaderType
import glimpse.textures.TextureHandle
import glimpse.textures.TextureMagnificationFilter
import glimpse.textures.TextureMinificationFilter
import glimpse.textures.TextureWrapping
import java.io.InputStream
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Android implementation of GLES facade.
 */
object GLES : GLES {

	override var viewport: Viewport by SetAndRememberDelegate(Viewport(1, 1)) {
		GLES20.glViewport(0, 0, it.width, it.height)
	}

	override var clearColor: Color by SetAndRememberDelegate(Color.Companion.BLACK) {
		GLES20.glClearColor(it.red, it.green, it.blue, it.alpha)
	}

	override var clearDepth: Float by SetAndRememberDelegate(1f) {
		GLES20.glClearDepthf(it)
	}

	override var isDepthTest: Boolean by EnableDisableDelegate(GLES20.GL_DEPTH_TEST)
	override var depthTestFunction: DepthTestFunction by EnumSetAndRememberDelegate(DepthTestFunction.LESS, depthTestFunctionMapping) {
		GLES20.glDepthFunc(it)
	}

	override var isBlend: Boolean by EnableDisableDelegate(GLES20.GL_BLEND)
	override var blendFunction: Pair<BlendFactor, BlendFactor> by EnumPairSetAndRememberDelegate(BlendFactor.ZERO to BlendFactor.ONE, blendFactorMapping) {
		GLES20.glBlendFunc(it.first, it.second)
	}

	override var isCullFace: Boolean by EnableDisableDelegate(GLES20.GL_CULL_FACE)
	override var cullFaceMode: CullFaceMode by EnumSetAndRememberDelegate(CullFaceMode.BACK, cullFaceModeMapping) {
		GLES20.glCullFace(it)
	}

	override fun clearDepthBuffer() {
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)
	}

	override fun clearColorBuffer() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
	}

	override fun createShader(shaderType: ShaderType) =
			ShaderHandle(GLES20.glCreateShader(shaderTypeMapping[shaderType]!!))

	override fun compileShader(shaderHandle: ShaderHandle, source: String) {
		GLES20.glShaderSource(shaderHandle.value, source)
		GLES20.glCompileShader(shaderHandle.value)
	}

	override fun deleteShader(shaderHandle: ShaderHandle) {
		GLES20.glDeleteShader(shaderHandle.value)
	}

	override fun getShaderCompileStatus(shaderHandle: ShaderHandle) =
			getShaderProperty(shaderHandle, GLES20.GL_COMPILE_STATUS) == GLES20.GL_TRUE

	private fun getShaderProperty(shaderHandle: ShaderHandle, property: Int): Int {
		val result = IntBuffer.allocate(1)
		GLES20.glGetShaderiv(shaderHandle.value, property, result)
		return result[0]
	}

	override fun getShaderLog(shaderHandle: ShaderHandle): String =
			GLES20.glGetShaderInfoLog(shaderHandle.value)

	override fun createProgram() =
			ProgramHandle(GLES20.glCreateProgram())

	override fun attachShader(programHandle: ProgramHandle, shaderHandle: ShaderHandle) {
		GLES20.glAttachShader(programHandle.value, shaderHandle.value)
	}

	override fun linkProgram(programHandle: ProgramHandle) {
		GLES20.glLinkProgram(programHandle.value)
	}

	override fun useProgram(programHandle: ProgramHandle) {
		GLES20.glUseProgram(programHandle.value)
	}

	override fun deleteProgram(programHandle: ProgramHandle) {
		GLES20.glDeleteProgram(programHandle.value)
	}

	override fun getProgramLinkStatus(programHandle: ProgramHandle) =
			getProgramProperty(programHandle, GLES20.GL_LINK_STATUS) == GLES20.GL_TRUE

	private fun getProgramProperty(programHandle: ProgramHandle, property: Int): Int {
		val result = IntBuffer.allocate(1)
		GLES20.glGetProgramiv(programHandle.value, property, result)
		return result[0]
	}

	override fun getProgramLog(programHandle: ProgramHandle): String =
			GLES20.glGetProgramInfoLog(programHandle.value)

	override fun getUniformLocation(handle: ProgramHandle, name: String) =
			UniformLocation(GLES20.glGetUniformLocation(handle.value, name))

	override fun getAttributeLocation(handle: ProgramHandle, name: String) =
			AttributeLocation(GLES20.glGetAttribLocation(handle.value, name))

	override fun uniformFloat(location: UniformLocation, float: Float) {
		GLES20.glUniform1f(location.value, float)
	}

	override fun uniformFloats(location: UniformLocation, floats: FloatArray) {
		GLES20.glUniform1fv(location.value, floats.size, floats, 0)
	}

	override fun uniformInt(location: UniformLocation, int: Int) {
		GLES20.glUniform1i(location.value, int)
	}

	override fun uniformInts(location: UniformLocation, ints: IntArray) {
		GLES20.glUniform1iv(location.value, ints.size, ints, 0)
	}

	override fun uniformMatrix16f(location: UniformLocation, _16f: Array<Float>) {
		GLES20.glUniformMatrix4fv(location.value, 1, false, _16f.toFloatArray(), 0)
	}

	override fun uniform4f(location: UniformLocation, _4f: Array<Float>, count: Int) {
		GLES20.glUniform4fv(location.value, count, _4f.toFloatArray(), 0)
	}

	override fun createAttributeFloatArray(location: AttributeLocation, buffer: FloatBuffer, vectorSize: Int) =
			createAttributeArray(location, buffer, vectorSize, GLES20.GL_FLOAT, 4)

	override fun createAttributeIntArray(location: AttributeLocation, buffer: IntBuffer, vectorSize: Int) =
			createAttributeArray(location, buffer, vectorSize, GLES20.GL_INT, 4)

	private fun createAttributeArray(location: AttributeLocation, buffer: Buffer, vectorSize: Int, type: Int, typeSize: Int): BufferHandle {
		buffer.rewind()
		val handles = IntArray(1)
		GLES20.glGenBuffers(1, handles, 0)
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, handles[0])
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.limit() * typeSize, buffer, GLES20.GL_STATIC_DRAW)
		GLES20.glVertexAttribPointer(location.value, vectorSize, type, false, 0, 0)
		return BufferHandle(handles[0])
	}

	override fun deleteAttributeArray(handle: BufferHandle) {
		GLES20.glDeleteBuffers(1, arrayOf(handle.value).toIntArray(), 0)
	}

	override fun enableAttributeArray(location: AttributeLocation) {
		GLES20.glEnableVertexAttribArray(location.value)
	}

	override fun disableAttributeArray(location: AttributeLocation) {
		GLES20.glDisableVertexAttribArray(location.value)
	}

	override var textureMinificationFilter: TextureMinificationFilter
			by EnumSetAndRememberDelegate(TextureMinificationFilter.NEAREST_MIPMAP_LINEAR, textureMinificationFilterMapping) {
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, it)
			}
	override var textureMagnificationFilter: TextureMagnificationFilter
			by EnumSetAndRememberDelegate(TextureMagnificationFilter.LINEAR, textureMagnificationFilterMapping) {
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, it)
			}
	override var textureWrapping: Pair<TextureWrapping, TextureWrapping>
			by EnumPairSetAndRememberDelegate(TextureWrapping.REPEAT to TextureWrapping.REPEAT, textureWrappingMapping) {
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, it.first)
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, it.second)
			}

	override fun generateTextures(count: Int): Array<TextureHandle> {
		val handles = IntArray(count)
		GLES20.glGenTextures(count, handles, 0)
		return handles.map { TextureHandle(it) }.toTypedArray()
	}

	override fun deleteTextures(count: Int, handles: Array<TextureHandle>) {
		GLES20.glDeleteTextures(count, handles.map { it.value }.toIntArray(), 0)
	}

	override fun bindTexture2D(handle: TextureHandle) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle.value)
	}

	override fun textureImage2D(inputStream: InputStream, fileName: String, withMipmap: Boolean) {
		val bitmap = BitmapFactory.decodeStream(inputStream)
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0)
		if (withMipmap) GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
		bitmap.recycle()
	}

	override fun activeTexture(index: Int) {
		require(index in 0..31) { "Texture index out of bounds: $index" }
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index)
	}

	override fun drawTriangles(verticesCount: Int) {
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, verticesCount)
	}
}
