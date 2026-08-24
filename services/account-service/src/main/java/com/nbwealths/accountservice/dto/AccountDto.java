package com.nbwealths.accountservice.dto;

public class AccountDto {
    private Long id;
    private String owner;
    private String type;
    private String currency;
    private Double balance;

    public AccountDto() {}

    public AccountDto(Long id, String owner, String type, String currency, Double balance) {
        this.id = id;
        this.owner = owner;
        this.type = type;
        this.currency = currency;
        this.balance = balance;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}
