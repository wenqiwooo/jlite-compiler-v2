package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class IfStmtNode extends StmtNode {
    ExprNode pred;
    List<StmtNode> thenBlk;
    List<StmtNode> elseBlk;

    public IfStmtNode(ExprNode pred, List<StmtNode> thenBlk, List<StmtNode> elseBlk) {
        this.pred = pred;
        this.thenBlk = thenBlk;
        this.elseBlk = elseBlk;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(String.format("if(%s)", pred));
        res.add("{");
        for (StmtNode stmt : thenBlk) {
            for (String s : stmt.getLines()) {
                res.add("\t" + s);
            }
        }
        res.add("}");
        res.add("else");
        res.add("{");
        for (StmtNode stmt : elseBlk) {
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
        /**
         * If predicate must be reduced if it is a boolean expression i.e. || , &&, 
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
                    "Class %s method %s: if statement predicate %s is not of type Bool",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(),  
                    resPred.ir3Obj);
            throw new TypeCheckException(errMsg);
        }

        String trueLabel = env.cgNewLabelName();
        env.cgCode(new Ir3IfGotoStmt(resPred.ir3Obj, trueLabel));

        TypeCheckResult resElse = TypeCheckHelper.checkType(env, elseBlk, nextLabel);

        env.cgCode(new Ir3GotoStmt(nextLabel));
        env.cgCode(new Ir3Label(trueLabel));

        TypeCheckResult resThen = TypeCheckHelper.checkType(env, thenBlk, nextLabel);
        
        if (TypeHelper.isASuperclassOfB(resThen.type, resElse.type)) {
            return new TypeCheckResult(resThen.type);
        } else if (TypeHelper.isASuperclassOfB(resElse.type, resThen.type)) {
            return new TypeCheckResult(resElse.type);
        }

        String errMsg = String.format(
                "Class %s method %s: if statement then-block and else-block have conflicting types %s and %s",
                env.getCurrentClassName(), 
                env.getCurrentMethodName(),  
                resThen.type, 
                resElse.type);
        throw new TypeCheckException(errMsg);
    }
}
