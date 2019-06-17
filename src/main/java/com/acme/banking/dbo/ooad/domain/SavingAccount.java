package com.acme.banking.dbo.ooad.domain;

import com.acme.banking.dbo.legacy.domain.AccountType;

/**
 * Encapsulated state + behavior
 * POJO
 */
public class SavingAccount implements Account {
    private long id;
    private double amount = 0;

    public SavingAccount(long id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    //Contract
    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    /**
     * TM
     */
    @Override
    public void withdraw(double amount) {
        if (validate(amount)) throw new IllegalStateException();
        this.amount -= amount;
    }

    @Override
    public void deposit(double amount) {
        this.amount += amount;
    }

    protected boolean validate(double amount) {
        return amount > this.amount;
    }
}
