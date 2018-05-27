package repository.product;

import model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByName(String name);
    List<Product> findBySportCategory(String sportCategory);
    List<Product> findByTypeCategory(String typeCategory);

}
