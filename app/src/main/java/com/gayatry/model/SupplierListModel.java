package com.gayatry.model;

/**
 * Created by Admin on 31-May-16.
 */
public class SupplierListModel {

    public String getPartyID() {
        return PartyID;
    }

    public void setPartyID(String partyID) {
        PartyID = partyID;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getPartyType_Id() {
        return PartyType_Id;
    }

    public void setPartyType_Id(String partyType_Id) {
        PartyType_Id = partyType_Id;
    }

    public String getParty_Type() {
        return Party_Type;
    }

    public void setParty_Type(String party_Type) {
        Party_Type = party_Type;
    }

    public String PartyID;
    public String PartyName;
    public String PartyType_Id;
    public String Party_Type;
}
