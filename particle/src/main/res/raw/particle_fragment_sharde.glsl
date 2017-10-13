#version 120
precision  mediump float;
varying vec3 v_Color;
varying float v_ElaspedTime;
void main() {
    gl_FragColor=vec4(v_Color/v_ElaspedTime,1.0);
}
