precision highp float;

uniform sampler2D texPaint;
uniform sampler2D texInput;
uniform sampler2D texVel;
uniform sampler2D texNoise;

uniform sampler2D texImageInput;
uniform vec3 uImageInputParams;

uniform vec2 uVelScale;
uniform float uFadeCoeff;
uniform float uBorderMirror;
uniform float uBorderRepeat;
uniform vec2 uNoiseIntensityAndSwitch;



varying highp vec2 varyTexCoord;

vec2 repeatAndMirrorTex(vec2 t)
{
#if defined(BORDER_MIRROR)
	t.y = (t.x < 0.0 || t.x > 1.0) ? 1.0 - t.y : t.y;
#endif
#if defined(BORDER_REPEAT)
	t.x = fract(t.x);
#endif
    //t = t.x > 1.0 ? vec2(mix(t.x, t.x - 1.0, uBorderRepeat), mix(t.y, 1.0 - t.y, uBorderMirror)) : t;
    //t = t.x < 0.0 ? vec2(mix(t.x, t.x + 1.0, uBorderRepeat), mix(t.y, 1.0 - t.y, uBorderMirror)) : t;
    return t;
}

void main()
{
	highp vec2 offset = texture2D(texVel, varyTexCoord).rg * uVelScale;
	highp vec2 samplePos = varyTexCoord - offset;
	
#if defined(VELOCITY_NOISE)
	highp vec4 noiseFieldsSample = texture2D(texNoise, varyTexCoord);
	highp vec2 noiseVel = mix(noiseFieldsSample.xy, noiseFieldsSample.zw, uNoiseIntensityAndSwitch.y);
	samplePos -= noiseVel * uVelScale * uNoiseIntensityAndSwitch.x * min(length(offset) * 100.0, 1.0);
#endif
	
	samplePos = repeatAndMirrorTex(samplePos);
	samplePos = repeatAndMirrorTex(samplePos.yx).yx;
	//samplePos.yx = repeatAndMirrorTex(samplePos.yx); // doesn't work on some devices for some reason...!
	
	vec4 sample = texture2D(texPaint, samplePos);
	
	// #ifdef code below is now absolutely terrible adter adding IMAGE_INPUT. If this should get any more complicated, it should probably be rewritten, possibly incorporating shader pieces mechanism.
	// Or maybe one big #ifdef for each for the four permuatations of IMAGE_INPUT and PAINT_SIGNED would me an improvement, despite redundancy
	
#if defined(IMAGE_INPUT)
	vec3 imageSample = texture2D(texImageInput, samplePos).rgb;
#else
	vec4 paintInput = texture2D(texInput, samplePos);
	paintInput = 25.0 * (paintInput * paintInput); // decoding of encoding defined in Procedures.cpp -> copyVelAndPaintInputLoop
#endif


#if defined(PAINT_SIGNED) && defined(IMAGE_INPUT)
	// If one multicolor palette is enabled, sum all channels of the image  to serve as the single channel paint source.
	// If two palettes are enabled, make some modifications
	float imageSampleSum = dot(vec3(1.0), imageSample) / 3.0;
	// this doesn't work well: imageSample.r = mix(imageSampleSum, imageSample.r - (imageSample.b + imageSample.g) * 0.5, uImageInputParams.z);// uImageInputParams.z is 1.0 if PAINT_SIGNED and double palette is enabled
	imageSample.r = mix(imageSampleSum, imageSampleSum * 2.0 - 1.0, uImageInputParams.z);// uImageInputParams.z is 1.0 if PAINT_SIGNED and double palette is enabled
#elif defined(PAINT_SIGNED) && !defined(IMAGE_INPUT)
	float totalInput = paintInput.r - paintInput.g;
	/*if(sign(totalInput) == sign(sample.r))
		sample.r += totalInput;
	else
		sample.r = mix(sample.r + totalInput, totalInput, clamp(abs(totalInput), 0.0, 1.0));*/
		
	// optimization of the commented code above, to remove if/else, working slightly different when sign(sample.r) = 0, but it is actually more desirable
	float inputSwitch = abs(sign(totalInput) - sign(sample.r));
	sample.r = min(2.0 - inputSwitch, 1.0) * (sample.r + totalInput) + max(inputSwitch - 1.0, 0.0) * mix(sample.r + totalInput, totalInput, clamp(abs(totalInput), 0.0, 1.0));
#elif !defined(IMAGE_INPUT)
	sample = abs(sample) + paintInput;
#endif

#if defined(IMAGE_INPUT)
	imageSample *= uImageInputParams.y;
	sample.rgb = mix(imageSample, sample.rgb, uImageInputParams.x);
#else
	sample *= uFadeCoeff;
#endif

#if defined(PAINT_SIGNED)
	sample.gb = vec2(0.0); // .g is used in the lowqual version for a competing color, instead of the opposite sign of float as here
#endif

	sample.w = dot(vec3(1.0), abs(sample.xyz));

	gl_FragColor = sample;
	
	//gl_FragColor = vec4(texture2D(texVel, varyTexCoord).rg * 3.1 + vec2(0.5), 0.0, 1.0); // visualize velocity
	//gl_FragColor = vec4(noiseVel * 0.25 + vec2(0.5), 0.0, 1.0); // visualize noise
}
