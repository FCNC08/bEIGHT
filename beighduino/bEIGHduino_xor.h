#ifndef bEIGHduino_xor_H
#define bEIGHduino_xor_H
#include "bEIGHduino_function.h"

class XOR : public Function{
  public:
    XOR(int input_count);
    void simulating() override;
};

#endif