#ifndef bEIGHduino_output_H
#define bEIGHduino_output_H

#include "bEIGHduino_connection.h"

class Output : public Connection{
  public:
    Output(int pin);
    void setState(bool state) override;
  private:
    int _pin;
};

#endif