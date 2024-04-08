#ifndef bEIGHduino_shield_not_H
#define bEIGHduino_shield_not_H
#include "bEIGHduino-shield_function.h"

class NOT : public Function{
  public:
    NOT();
    void simulating() override;
};

#endif