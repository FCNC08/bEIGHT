#include "bEIGHduino_connection.h"
#include "bEIGHduino_function.h"
#include "Arduino.h"

Connection::Connection(){
  _state = false;
}

void Connection::addFunction(void* func){
  functions.add(func);
  static_cast<Function*>(func)->simulate();
}

void Connection::setState(bool state){
  if(state!=_state){
    _state = state;
      for(int i = 0; i<functions.size(); i++){
      Function* comp = static_cast<Function*>(functions.get(i));
      comp->simulate();
    }
  }
}

bool Connection::getState(){
  return _state;
}