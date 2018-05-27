package model;


import javax.persistence.*;

@Entity
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "command_id")
    private CommandOrder commandOrder;


    public CommandOrder getCommandOrder() {
        return commandOrder;
    }

    public void setCommandOrder(CommandOrder commandOrder) {
        this.commandOrder = commandOrder;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderId { " + id +
                ", product=" + product.getName() +
                ", client=" + client.getUsername() +
                ", onDate=" + commandOrder.getExpectedArrivalDate() +
                ", quantity=" + quantity +
                '}';
    }
}
