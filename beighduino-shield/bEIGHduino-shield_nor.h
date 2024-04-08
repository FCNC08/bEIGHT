#ifndef bEIGHduino_shield_nor_H
#define bEIGHduino_shield_nor_H
#include "bEIGHduino-shield_function.h"

class NOR : public Function{
  public:
    NOR(int input_count);
    void simulating() override;
};

#endif