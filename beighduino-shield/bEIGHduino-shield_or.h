#ifndef bEIGHduino_shieldor_H
#define bEIGHduino_shield_or_H
#include "bEIGHduino-shield_function.h"

class OR : public Function{
  public:
    OR(int input_count);
    void simulating() override;
};

#endif