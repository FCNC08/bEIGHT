#include "bEIGHduino_not.h"
#include "HardwareSerial.h"
#include "bEIGHduino_function.h"
#include "bEIGHduino_connection.h"

NOT::NOT():Function(1, 1){}

void NOT::simulating(){
  bool out_state =!static_cast<Connection*>(inputs.get(0))->getState();
  static_cast<Connection*>(outputs.get(0))->setState(out_state);
}