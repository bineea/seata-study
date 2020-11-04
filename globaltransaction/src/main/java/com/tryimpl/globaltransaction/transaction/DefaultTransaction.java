package com.tryimpl.globaltransaction.transaction;

import com.tryimpl.globaltransaction.util.Task;

public class DefaultTransaction {

    private String command;
    private String groupId;
    private String transactionId;
    private TransactionType transactionType;
    private Task task;
    private boolean isEnd;

    public DefaultTransaction(String groupId, String transactionId, boolean isEnd) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.isEnd = isEnd;
        this.task = new Task();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
