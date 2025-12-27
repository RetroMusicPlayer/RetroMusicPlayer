attribute vec4 a_Position;
attribute vec4 a_Color;
varying vec4 v_Color;

void main() {
    v_Color = a_Color;
    gl_Position = a_Position;
}
