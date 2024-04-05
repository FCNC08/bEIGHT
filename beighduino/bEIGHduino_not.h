#ifndef bEIGHduino_not_H
#define bEIGHduino_not_H
#include "bEIGHduino_function.h"

class NOT : public Function{
  public:
    NOT();
    void simulating() override;
};

#endif