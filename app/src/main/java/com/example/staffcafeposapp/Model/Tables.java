package com.example.staffcafeposapp.Model;

public class Tables {
    private String tableNo, tableStatus;
    private int tableNoOfSeat;

    public Tables(){}

    public Tables(String tableNo, int tableNoOfSeat, String tableStatus) {
        this.tableNo = tableNo;
        this.tableStatus = tableStatus;
        this.tableNoOfSeat = tableNoOfSeat;
    }

    public String getTableNo() {
        return tableNo;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public int getTableNoOfSeat() {
        return tableNoOfSeat;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public void setTableNoOfSeat(int tableNoOfSeat) {
        this.tableNoOfSeat = tableNoOfSeat;
    }
}
