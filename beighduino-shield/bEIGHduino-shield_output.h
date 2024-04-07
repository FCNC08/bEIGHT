#ifndef bEIGHduino_shield_output_H
#define bEIGHduino_shield_output_H

#include "bEIGHduino-shield_connection.h"
#include "Adafruit_NeoPixel.h"

class Output : public Connection{
  public:
    Output(int pin, Adafruit_NeoPixel* outputpixel);
    void setState(bool state) override;
  private:
    int _pin;
    Adafruit_NeoPixel* _outputpixel;
};

#endif