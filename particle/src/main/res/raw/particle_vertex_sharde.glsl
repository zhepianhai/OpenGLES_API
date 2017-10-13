#version 120
uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DriectionVector;
attribute float a_ParticleStartTime;


varying vec3 v_Color;
varying float v_ElaspedTime;
void main() {
    v_Color=a_Color;
    v_ElaspedTime=u_Time-a_ParticleStartTime;
    vec3 currentPosition=a_Position+(a_DriectionVector*v_ElaspedTime);
    gl_Position=u_Matrix*vec4(currentPosition,1.0);
    gl_PositionSize=10.0;
}
