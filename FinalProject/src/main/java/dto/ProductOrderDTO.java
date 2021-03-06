package dto;

public class ProductOrderDTO {

    private Long id;
    private Long productId;
    private int quantity;
    private String accountString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAccountString() {
        return accountString;
    }

    public void setAccountString(String accountString) {
        this.accountString = accountString;
    }
}
