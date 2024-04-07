#ifndef bEIGHduino_shield_xor_H
#define bEIGHduino_shield_xor_H
#include "bEIGHduino-shield_function.h"

class XOR : public Function{
  public:
    XOR(int input_count);
    void simulating() override;
};

#endif