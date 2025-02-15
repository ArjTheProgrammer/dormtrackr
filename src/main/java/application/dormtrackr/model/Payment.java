package application.dormtrackr.model;

import java.sql.Date;
import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int dormerId;
    private LocalDate paymentDate;

    public Payment(){

    }

    //without the id constructor
    public Payment(int dormerId, LocalDate paymentDate){
        this.dormerId = dormerId;
        this.paymentDate = paymentDate;
    }

    public Payment(int paymentId, int dormerId, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.dormerId = dormerId;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getDormerId() {
        return dormerId;
    }

    public void setDormerId(int dormerId) {
        this.dormerId = dormerId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getSqlDate(){
        return Date.valueOf(paymentDate);
    }
}
