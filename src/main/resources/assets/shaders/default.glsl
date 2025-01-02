#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColour;

uniform mat4 uProj;
uniform mat4 uView;

out vec4 fColour;

void main() {
    fColour = aColour;
    gl_Position = uProj * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core
in vec4 fColour;

uniform float uTime;

out vec4 colour;

void main() {
    float noise = fract(sin(dot(fColour.xy, vec2(12.9898, 78.233))) * 43758.5453);
    colour = fColour * noise;
}