#pragma once

#include <stdlib.h>
#include <stdio.h>

void* malloc(size_t size) { 
    return malloc(size); 
}

void free(void* ptr) { 
    free(ptr); 
}
