"""
This script is used to generate JLite test classes. 
"""
import os

def write_class(output_file, name, attributes):
    with open(output_file, 'w+') as f:
        f.write('class {} {{\n'.format(name))
        for attr, typ in attributes:
            f.write('\t{} {};\n'.format(typ, attr)) 
        f.write('}\n')


if __name__ == '__main__':
    output_file = '../tests/final/offset_test'
    name = 'TestClass'
    attributes = tuple(('v{}'.format(i), 'Int') for i in range(1600))

    write_class(output_file, name, attributes)
    
