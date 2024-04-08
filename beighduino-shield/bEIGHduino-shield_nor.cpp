#include "bEIGHduino-shield_nor.h"
#include "HardwareSerial.h"
#include "bEIGHduino-shield_function.h"
#include "bEIGHduino-shield_connection.h"

NOR::NOR(int input_count):Function(input_count, 1){}

void NOR::simulating(){
  bool out_state = false;
  for(int i = 0; i<_input_count; i++){
    out_state = out_state||static_cast<Connection*>(inputs.get(i))->getState();
  }
  static_cast<Connection*>(outputs.get(0))->setState(!out_state);
}