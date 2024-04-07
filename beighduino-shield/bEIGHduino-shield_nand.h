#ifndef bEIGHduino_shield_nand_H
#define bEIGHduino_shield_nand_H
#include "bEIGHduino-shield_function.h"

class NAND : public Function{
  public:
    NAND(int input_count);
    void simulating() override;
};

#endif