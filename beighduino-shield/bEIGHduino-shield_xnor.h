#ifndef bEIGHduino_shield_xnor_H
#define bEIGHduino_shield_xnor_H
#include "bEIGHduino-shield_function.h"

class XNOR : public Function{
  public:
    XNOR(int input_count);
    void simulating() override;
};

#endif