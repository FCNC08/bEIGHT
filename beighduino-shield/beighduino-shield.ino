#include "bEIGHduino-shield_util.h"
#include <Adafruit_NeoPixel.h>
#define PIN1 10
#define PIN2 11
#define inputlenght 8
#define outputlenght 8
Adafruit_NeoPixel input_pixel = Adafruit_NeoPixel(inputlenght, PIN1, NEO_GRB +NEO_KHZ800);
Adafruit_NeoPixel output_pixel= Adafruit_NeoPixel(outputlenght, PIN2, NEO_GRB +NEO_KHZ800);
uint32_t blue = input_pixel.Color(0,0,255);
const int input_size = 2;
Inputs** inputs = new Inputs*[input_size];
void setup() {
input_pixel.begin();
output_pixel.begin();
inputs[0] = new Inputs(2);
Connection* NOT0_out0 = new Connection();
inputs[1] = new Inputs(3);
Connection* AND0_out0 = new Connection();
Connection* AND1_out0 = new Connection();
Output* NOR1_out0 = new Output(6, &output_pixel);
Output* NOR0_out0 = new Output(7, &output_pixel);
inputs[1] = new Inputs(3);
inputs[0] = new Inputs(2);
NOT* NOT0 = new NOT();
AND* AND0 = new AND(2);
NOR* NOR0 = new NOR(2);
NOR* NOR1 = new NOR(2);
AND* AND1 = new AND(2);
NOT0->addInput(inputs[0]);
inputs[0]->addFunction(NOT0);
NOT0->addOutput(NOT0_out0);
AND0->addInput(NOT0_out0);
NOT0_out0->addFunction(AND0);
AND0->addInput(inputs[1]);
inputs[1]->addFunction(AND0);
AND0->addOutput(AND0_out0);
NOR0->addInput(AND0_out0);
AND0_out0->addFunction(NOR0);
NOR0->addInput(NOR1_out0);
NOR1_out0->addFunction(NOR0);
NOR0->addOutput(NOR0_out0);
NOR1->addInput(NOR0_out0);
NOR0_out0->addFunction(NOR1);
NOR1->addInput(AND1_out0);
AND1_out0->addFunction(NOR1);
NOR1->addOutput(NOR1_out0);
AND1->addInput(inputs[1]);
inputs[1]->addFunction(AND1);
AND1->addInput(inputs[0]);
inputs[0]->addFunction(AND1);
AND1->addOutput(AND1_out0);
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