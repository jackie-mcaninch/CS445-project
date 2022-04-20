package edu.iit.cs445.spring2022.buynothing;

public interface BNBoundaryInterface {
    BuyNothingObj create(BuyNothingObj raw_obj);
    int activate(String id);
    int replace(String old_id, BuyNothingObj new_obj);
    void delete(String id);
    BuyNothingObj viewAccount(String id);
}
