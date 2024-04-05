#ifndef bEIGHduino_and_H
#define bEIGHduino_and_H
#include "bEIGHduino_function.h"

class AND : public Function{
  public:
    AND(int input_count);
    void simulating() override;
};

#endif