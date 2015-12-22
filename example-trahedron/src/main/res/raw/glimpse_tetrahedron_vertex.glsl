uniform mat4 u_MVPMatrix;
attribute vec4 a_VertexPosition;

void main() {
	gl_Position = u_MVPMatrix * a_VertexPosition;
}
