package com.lib.weight.treelist;

/**
 * 递归跳出的异常
 */
public class StopMsgException extends RuntimeException {

    private Tree tree;

    public <T extends Tree> T getTree() {
        return (T) tree;
    }

    public StopMsgException setTree(Tree tree) {
        this.tree = tree;
        return this;
    }

    public StopMsgException(String message) {
        super(message);
    }
}
