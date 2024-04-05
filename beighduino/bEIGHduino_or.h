#ifndef bEIGHduino_or_H
#define bEIGHduino_or_H
#include "bEIGHduino_function.h"

class OR : public Function{
  public:
    OR(int input_count);
    void simulating() override;
};

#endif