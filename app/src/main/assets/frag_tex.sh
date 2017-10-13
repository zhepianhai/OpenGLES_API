precision mediump float;//指定精度
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform sampler2D sTexture;//纹理内容数据
void main()
{
    //纹理采样
    gl_FragColor=texture2D(sTexture,vTextureCoord);
}