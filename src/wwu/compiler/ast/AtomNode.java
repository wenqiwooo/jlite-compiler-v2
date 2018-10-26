package wwu.compiler.ast;

import java.io.*;
import java.util.*;

/**
 * AtomNode can be one of the following: 
 * 
 * IdNode: identifiers
 * FdNode: attribute of an object
 * CallNode: method call of an object
 * ConstructorNode: class constructor
 * NullNode: null object
 * AtomExprNode: for handling parenthesized expressions
 * 
 * Generally, atoms can be thought of as chainable components.
 */
public abstract class AtomNode extends ExprNode {

}