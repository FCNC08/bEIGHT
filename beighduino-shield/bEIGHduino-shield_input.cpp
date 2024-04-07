#include "Arduino.h"
#include "bEIGHduino-shield_input.h"
#include "bEIGHduino-shield_connection.h"

Inputs::Inputs(int pin):Connection(){
  _pin = pin;
  pinMode(pin, INPUT);
}