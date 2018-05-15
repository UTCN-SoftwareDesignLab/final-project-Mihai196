package dto;

public class CreditCardDTO {
    private Long id;
    private double balance;

    private String bankName;

    private Long cardNr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getCardNr() {
        return cardNr;
    }

    public void setCardNr(Long cardNr) {
        this.cardNr = cardNr;
    }
}
