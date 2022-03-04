package cz.fit.cvut.contract_manager.entity;

public enum ContractState {
    VALID,      // Contract is valid when when the date did not cross the expire date yet
    EXPIRED,    // Contract is expired when the date crossed the expire date
    WITHDRAWN,  // Contract is withdrawn when a Customer took out the item before it was taken out for resell
    TAKEN_OUT   // Contract is taken_out when the item is taken out by an employer for resell
}
