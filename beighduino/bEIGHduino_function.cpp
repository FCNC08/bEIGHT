#include "bEIGHduino_function.h"
#include "bEIGHduino_connection.h"
Function::Function(int input_count, int output_count){
  _input_count = input_count;
  _output_count = output_count;
}

void Function::addInput(void* in){
  inputs.add(in);
}

void Function::addOutput(void* out){
  outputs.add(out);
}

void Function::simulate(){
  if(_input_count==inputs.size()&&_output_count==outputs.size()){
    simulating();
  }
}
