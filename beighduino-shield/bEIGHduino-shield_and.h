#ifndef bEIGHduino_shield_and_H
#define bEIGHduino_shield_and_H
#include "bEIGHduino-shield_function.h"

class AND : public Function{
  public:
    AND(int input_count);
    void simulating() override;
};

#endif