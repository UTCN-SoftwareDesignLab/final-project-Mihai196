package repository.productOrder;

import model.CommandOrder;
import model.ProductOrder;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Long> {
    List<ProductOrder> findByClient(User client);
    List<ProductOrder> findByCommandOrder(CommandOrder commandOrder);
}
