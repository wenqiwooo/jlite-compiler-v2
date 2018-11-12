package wwu.compiler.cfg;

public abstract class TransferFunction {
    
    abstract void initEntryState(BasicBlock entry);

    abstract void initExitState(BasicBlock exit);

    // Initialize state of basic block
    abstract void initBasicBlockState(BasicBlock bb);

    /**
     * @param bb BasicBlock
     * @return true to continue analysis upstream, false otherwise
     */
    boolean backwardUpdate(BasicBlock bb, CFGraph cfg) {
        return false;
    }

    /**
     * @param bb BasicBlock
     * @return true to continue analysis downstream, false otherwise
     */
    boolean forwardUpdate(BasicBlock bb, CFGraph cfg) {
        return false;
    }
}