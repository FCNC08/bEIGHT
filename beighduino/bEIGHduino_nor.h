#ifndef bEIGHduino_nor_H
#define bEIGHduino_nor_H
#include "bEIGHduino_function.h"

class NOR : public Function{
  public:
    NOR(int input_count);
    void simulating() override;
};

#endif