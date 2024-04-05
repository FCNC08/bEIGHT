#include "bEIGHduino_and.h"
#include "HardwareSerial.h"
#include "bEIGHduino_function.h"
#include "bEIGHduino_connection.h"

AND::AND(int input_count):Function(input_count, 1){}

void AND::simulating(){
  bool out_state = true;
  for(int i = 0; i<_input_count; i++){
    out_state = out_state&&static_cast<Connection*>(inputs.get(i))->getState();
  }
  static_cast<Connection*>(outputs.get(0))->setState(out_state);
}