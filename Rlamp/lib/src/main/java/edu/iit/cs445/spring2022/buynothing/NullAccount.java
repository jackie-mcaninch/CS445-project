package edu.iit.cs445.spring2022.buynothing;

public class NullAccount extends Account {
    @Override
    public boolean isNil() {
        return true;
    }
}
