#include "bEIGHduino-shield_not.h"
#include "HardwareSerial.h"
#include "bEIGHduino-shield_function.h"
#include "bEIGHduino-shield_connection.h"

NOT::NOT():Function(1, 1){}

void NOT::simulating(){
  bool out_state =!static_cast<Connection*>(inputs.get(0))->getState();
  static_cast<Connection*>(outputs.get(0))->setState(out_state);
}