#include "bEIGHduino_output.h"
#include "Arduino.h"
#include "bEIGHduino_connection.h"
#include "bEIGHduino_function.h"

Output::Output(int pin):Connection(){
  _type = Connection_OUTPUT;
  _pin = pin;
  pinMode(pin, OUTPUT);
}

void Output::setState(bool state){
  if(_state != state){
    _state = state;
    if(state){
      digitalWrite(_pin, HIGH);
    }else{
      digitalWrite(_pin, LOW);
    }
    for(int i = 0; i<functions.size(); i++){
      Function* comp = static_cast<Function*>(functions.get(i));
      comp->simulate();
    }
  }

}