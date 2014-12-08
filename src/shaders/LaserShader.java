#version 400

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCamera;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float drawShadow;

void main(void){

vec3 unitNormal = normalize(surfaceNormal);
vec3 unitLightVector = normalize(toLightVector);

float nDotl= dot(unitNormal,unitLightVector);
float brightness = max(nDotl,0.08);
vec3 diffuse =brightness*lightColor;


vec3 unitVectorToCamera= normalize(toCamera);
vec3 lightDirection =-unitLightVector;
vec3 reflectedLightDirection =reflect(lightDirection,unitNormal);

float specularFactor= dot(reflectedLightDirection,unitVectorToCamera);
specularFactor = max(specularFactor,0.0);
float dampedFactor = pow(specularFactor,shineDamper);
vec3 finalSpecular= dampedFactor*reflectivity*lightColor;




//out_Color=vec4(diffuse,1.0) * texture(textureSampler,pass_textureCoords)+vec4(finalSpecular,1.0);
if(drawShadow>0.5){out_Color=vec4(diffuse,1.0) *texture(textureSampler,pass_textureCoords);}
else{out_Color=texture(textureSampler,pass_textureCoords);}


}