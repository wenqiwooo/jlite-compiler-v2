package wwu.compiler.ast;

import java.io.*;
import java.util.*;

public class MdBody {
    public List<VarDeclNode> varDecls;
    public List<StmtNode> stmts;

    public MdBody(List<VarDeclNode> varDecls, List<StmtNode> stmts) {
        this.varDecls = varDecls;
        this.stmts = stmts;
    }
}