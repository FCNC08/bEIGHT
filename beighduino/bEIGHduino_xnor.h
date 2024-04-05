#ifndef bEIGHduino_xnor_H
#define bEIGHduino_xnor_H
#include "bEIGHduino_function.h"

class XNOR : public Function{
  public:
    XNOR(int input_count);
    void simulating() override;
};

#endif