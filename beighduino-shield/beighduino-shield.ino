#include "bEIGHduino-shield_util.h"
#include <Adafruit_NeoPixel.h>
#define PIN1 10
#define PIN2 11
#define inputlenght 8
#define outputlenght 8
Adafruit_NeoPixel input_pixel = Adafruit_NeoPixel(inputlenght, PIN1, NEO_GRB +NEO_KHZ800);
Adafruit_NeoPixel output_pixel= Adafruit_NeoPixel(outputlenght, PIN2, NEO_GRB +NEO_KHZ800);
uint32_t blue = input_pixel.Color(0,0,255);
const int input_size = 1;
Inputs** inputs = new Inputs*[input_size];
void setup() {
input_pixel.begin();
output_pixel.begin();
inputs[0] = new Inputs(2);
Output* NOT0_out0 = new Output(7, &output_pixel);
NOT* NOT0 = new NOT();
NOT0->addInput(inputs[0]);
inputs[0]->addFunction(NOT0);
NOT0->addOutput(NOT0_out0);
for(int i = inputlenght-1; i>=inputlenght-input_size; i--){
  input_pixel.setPixelColor(i, blue);
}
input_pixel.show();}
void loop(){
  for(int i = 0; i<input_size; i++){
    int state = digitalRead(inputs[i]->_pin);
    if(state == HIGH){
      inputs[i]->setState(true);
    }else{
      inputs[i]->setState(false);
    }
  }
}