#ifndef bEIGHduino_shield_input_H
#define bEIGHduino_shield_input_H
#include "bEIGHduino-shield_connection.h"

class Inputs : public Connection{
  public: 
    Inputs(int pin);
    int _pin;
};

#endif