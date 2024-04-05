#ifndef bEIGHduino_function_H
#define bEIGHduino_function_H

#include "LinkedList.h"

class Function{
  public:
    Function(int input_count, int output_count);
    void simulate();
    virtual void simulating();
    void addInput(void* in);
    void addOutput(void* out);
  protected:
    int _input_count;
    LinkedList inputs;
    int _output_count;
    LinkedList outputs;
};

#endif