#include "bEIGHduino-shield_util.h"
#include <Adafruit_NeoPixel.h>
#define PIN1 10
#define PIN2 11
#define inputlenght 8
#define outputlenght 8
Adafruit_NeoPixel input_pixel = Adafruit_NeoPixel(inputlenght, PIN1, NEO_GRB +NEO_KHZ800);
Adafruit_NeoPixel output_pixel= Adafruit_NeoPixel(outputlenght, PIN2, NEO_GRB +NEO_KHZ800);
uint32_t green = input_pixel.Color(0,0,255);
const int input_size = 2;
Inputs** inputs = new Inputs*[input_size];
void setup() {
inputs[0] = new Inputs(2);
inputs[1] = new Inputs(3);
Output* AND0_out0 = new Output(0, &output_pixel);
AND* AND0 = new AND(2);
AND0->addInput(inputs[0]);
inputs[0]->addFunction(AND0);
AND0->addInput(inputs[1]);
inputs[1]->addFunction(AND0);
AND0->addOutput(AND0_out0);
for(int i = 0; i<input_size; i++){
  input_pixel.setPixelColor(i, green);
}
}
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