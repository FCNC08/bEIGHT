#ifndef bEIGHduino_nand_H
#define bEIGHduino_nand_H
#include "bEIGHduino_function.h"

class NAND : public Function{
  public:
    NAND(int input_count);
    void simulating() override;
};

#endif