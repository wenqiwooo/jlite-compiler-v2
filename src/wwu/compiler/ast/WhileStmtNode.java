package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;
import wwu.compiler.exception.TypeCheckException;

public class WhileStmtNode extends StmtNode {
    ExprNode pred;
    List<StmtNode> blk;

    public WhileStmtNode(ExprNode pred, List<StmtNode> blk) {
        this.pred = pred;
        this.blk = blk;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(String.format("while(%s)", pred));
        res.add("{");
        for (StmtNode stmt : blk) {
            for (String s : stmt.getLines()) {
                res.add("\t" + s);
            }
        }
        res.add("}");
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        // Don't allow empty block in while statement, flag this as a type error.
        if (blk.isEmpty()) {
            String errMsg = String.format(
                    "Class %s method %s: while loop must contain at least one statement",
                    env.getCurrentClassName(),
                    env.getCurrentMethodName());
            throw new TypeCheckException(errMsg);
        }

        String checkLabel = env.cgNewLabelName();
        env.cgCode(new Ir3Label(checkLabel));

        /**
         * Predicate must be reduced if it is a boolean expression i.e. || , &&, 
         * but it does not need to if it is a relational expression i.e. more than, less than.
         * 
         * This is a hack to take care of that.
         */
        boolean reducePred = false;
        if (pred instanceof BinaryExprNode) {
            BinaryExprNode exprNode = (BinaryExprNode)pred;
            reducePred = !exprNode.op.isRelational();
        }
        TypeCheckResult resPred = TypeCheckHelper.checkType(env, pred, reducePred);

        if (!resPred.type.equals(Type.BOOL)) {
            String errMsg = String.format(
                    "Class %s method %s: while loop predicate %s is not of type Bool",
                    env.getCurrentClassName(),
                    env.getCurrentMethodName(),
                    resPred.ir3Obj);
            throw new TypeCheckException(errMsg);
        }

        String startLabel = env.cgNewLabelName();
        
        env.cgCode(new Ir3IfGotoStmt(resPred.ir3Obj, startLabel));
        env.cgCode(new Ir3GotoStmt(nextLabel));
        env.cgCode(new Ir3Label(startLabel));

        TypeCheckResult resBlk = TypeCheckHelper.checkType(env, blk);
        
        env.cgCode(new Ir3GotoStmt(checkLabel));

        return resBlk;
    }
}