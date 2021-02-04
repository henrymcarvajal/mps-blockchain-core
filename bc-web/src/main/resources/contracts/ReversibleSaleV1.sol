// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.6.0 <0.8.0;

contract ReturnableSaleV1 {

    enum State {
        FUNDED,
        DISPUTED_PHASE_1,
        DISPUTED_PHASE_2,
        CLOSED
    }


    enum DisputeWinner {
        SELLER,
        BUYER,
        TIE
    }
    
    
    address payable public sponsor;
    address payable public seller;
    address payable public buyer;


    DisputeWinner public disputeWinner;


    uint sellerFee;
    uint buyerFee;
    uint sellerCharges;
    uint buyerCharges;
    bool sellerFeePayed;
    bool buyerFeePayed;
    bool sellerReinbursed;
    bool buyerReinbursed;


    bool chargesApplied;
    

    State public state;
    uint public deposit;
    uint public currentFunds;
    

    event StateChanged();

    
    /**
     * Contract will be created with an initial deposit 
     */
    constructor (address payable _seller, address payable _buyer) payable {
        require(msg.value > 0, "initial funds must be greater than 0");
        sponsor = msg.sender;
        deposit = msg.value;

        currentFunds = deposit;
        seller = _seller;
        buyer = _buyer;
        state = State.FUNDED;

        sellerFee = 0;
        buyerFee = 0;
        sellerCharges = 0;
        buyerCharges = 0;

        sellerFeePayed = false;
        buyerFeePayed = false;
        sellerReinbursed = false;
        buyerReinbursed = false;        

        chargesApplied = false;
    }
    
    
    modifier validState(State _state) {
        require(state == _state, "Invalid state");
        _;
    }
    
    
    modifier validStates(State _state1, State _state2) {
        require(state == _state1 || state == _state2, "Invalid states");
        _;
    }


    function getContractBalance() public view returns (uint) {
        return address(this).balance;
    }
    
    
    /**
     * Normal scenario -> pay to seller: when transaction goes without disputes
     */
    function paySeller() public
    validState(State.FUNDED) {
        require(msg.sender == seller, "Invalid issuer: required 'seller'");
        currentFunds = currentFunds - deposit;
        state = State.CLOSED;
        seller.transfer(deposit);
        emit StateChanged();
    }


    // PHASE 1


    /**
     * Dispute scenario -> mark the beginning of Phase1
     * 
     */
    function setDisputePhase(State phase) internal {
        state = phase;
    }
    
    /**
     * Dispute scenario -> mark the beginning of Phase1
     * 
     */
    function setDisputeOnPhase1() public
    validState(State.FUNDED) {
        require(msg.sender == sponsor, "Invalid issuer: required 'sponsor'");
        setDisputePhase(State.DISPUTED_PHASE_1);
        emit StateChanged();
    }


    /**
     * Dispute scenario -> mark winner of dispute, 
     * 
     */
    function setDisputeWinner(DisputeWinner winner, uint _sellerCharges, uint _buyerCharges) public
    validStates(State.DISPUTED_PHASE_1, State.DISPUTED_PHASE_2) {
        require(msg.sender == sponsor, "Invalid issuer: required 'sponsor'");
        require(!chargesApplied, "Charges already applied");
        disputeWinner = winner;
        sellerCharges = _sellerCharges;
        buyerCharges = _buyerCharges;
        uint charges = sellerCharges + buyerCharges;
        sponsor.transfer(charges);
        currentFunds = currentFunds - charges;
    }
    
    
    /**
     * Dispute scenario: 1 Phase -> reimburse seller: when seller has won dispute
     */
    function reimburseSeller() public
    validStates(State.DISPUTED_PHASE_1, State.DISPUTED_PHASE_2) {         
        require(msg.sender == seller, "Invalid caller: required 'seller'");
        require((disputeWinner == DisputeWinner.SELLER) || (disputeWinner == DisputeWinner.TIE), "Invalid party: required 'seller'");
        uint sellerReinbursement = 0;
        if (disputeWinner == DisputeWinner.SELLER) {
            if (state == State.DISPUTED_PHASE_1) {
                sellerReinbursement = deposit;
            } else if (state == State.DISPUTED_PHASE_2) {
                sellerReinbursement = deposit + buyerFee + sellerFee - sellerCharges;
            }
        } else if (disputeWinner == DisputeWinner.TIE) {
            sellerReinbursement = (deposit / 2) + sellerFee - sellerCharges;
        }
        seller.transfer(sellerReinbursement);
        currentFunds = currentFunds - sellerReinbursement;
        if (buyerReinbursed) {
            state = State.CLOSED;
            emit StateChanged();
        }
    }


    /**
     * Dispute scenario: 1 Phase | Phase 2 -> reimburse buyer: when buyer has won dispute
     */
    function reimburseBuyer() public
    validStates(State.DISPUTED_PHASE_1, State.DISPUTED_PHASE_2) {        
        require(msg.sender == buyer, "Invalid caller: required 'buyer'");
        require((disputeWinner == DisputeWinner.BUYER) || (disputeWinner == DisputeWinner.TIE), "Invalid party: required 'buyer'");
        uint buyerReinbursement = 0;
        if (disputeWinner == DisputeWinner.BUYER) {
            if (state == State.DISPUTED_PHASE_1) {
                buyerReinbursement = deposit;
            } else if (state == State.DISPUTED_PHASE_2) {
                buyerReinbursement = deposit + buyerFee + sellerFee - buyerCharges;
            }
        } else if (disputeWinner == DisputeWinner.TIE) {
            buyerReinbursement = (deposit / 2) + buyerFee - buyerCharges;
        }
        buyer.transfer(buyerReinbursement);
        currentFunds = currentFunds - buyerReinbursement;
        if (sellerReinbursed) {
            state = State.CLOSED;
            emit StateChanged();
        }
    }


    // PHASE 2


    /**
     * Dispute scenario: Phase 2 -> sponsor funds buyer dispute fee
     */
    function fundBuyerFee() public payable
    validStates(State.DISPUTED_PHASE_1, State.DISPUTED_PHASE_2) {
        require(msg.sender == sponsor, "Invalid issuer: required 'sponsor'");
        require(msg.value > 0, "Invalid fee: required greater than 0");
        require(!buyerFeePayed, "Fee already payed");
        buyerFeePayed = true;
        buyerFee = msg.value;
        currentFunds = currentFunds + msg.value;
        setDisputePhase(State.DISPUTED_PHASE_2);
    }
    
    
    /**
     * Dispute scenario: Phase 2 -> sponsor funds seller dispute fee
     */
    function fundSellerFee() public payable
    validStates(State.DISPUTED_PHASE_1, State.DISPUTED_PHASE_2) {
        require(msg.sender == sponsor, "Invalid issuer: required 'sponsor'");
        require(msg.value > 0, "Invalid fee: required greater than 0");
        require(!sellerFeePayed, "Fee already payed");
        sellerFeePayed = true;
        sellerFee =  msg.value;
        currentFunds = currentFunds + msg.value;
        setDisputePhase(State.DISPUTED_PHASE_2);
    }
    
    /**
     * Backdoor recover of funds, just "in case"
     */
    
    function recoverFunds() public {
        require(msg.sender == sponsor, "Invalid issuer: required 'sponsor'");
        sponsor.transfer(getContractBalance());
    }
}