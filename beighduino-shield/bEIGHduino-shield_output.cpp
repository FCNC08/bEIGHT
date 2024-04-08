#include "bEIGHduino-shield_output.h"
#include "Arduino.h"
#include "bEIGHduino-shield_connection.h"
#include "bEIGHduino-shield_function.h"
const uint32_t darkgreen = 200;
const uint32_t lightgreen = 0x005050ff; 

Output::Output(int pin, Adafruit_NeoPixel* outputpixel):Connection(){
  _type = Connection_OUTPUT;
  _pin = pin;
  _outputpixel = outputpixel;
}

void Output::setState(bool state){
  if(_state != state){
    _state = state;
    if(state){
      _outputpixel->setPixelColor(_pin, lightgreen);
    }else{
      _outputpixel->setPixelColor(_pin, darkgreen);
    }
    for(int i = 0; i<functions.size(); i++){
      Function* comp = static_cast<Function*>(functions.get(i));
      comp->simulate();
    }
  }

}