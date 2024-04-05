#include "Arduino.h"
#include "bEIGHduino_input.h"
#include "bEIGHduino_connection.h"

Inputs::Inputs(int pin):Connection(){
  _pin = pin;
  pinMode(pin, INPUT);
}