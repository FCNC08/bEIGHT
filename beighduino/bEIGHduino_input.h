#ifndef bEIGHduino_input_H
#define bEIGHduino_input_H
#include "bEIGHduino_connection.h"

class Inputs : public Connection{
  public: 
    Inputs(int pin);
    int _pin;
};

#endif