precision mediump float;

void main() {
	float value = 3.0 - 3.0 * gl_FragCoord.z;
	gl_FragColor = vec4(value, value, value, 1.0);
}
