package repository.creditCard;

import model.CreditCard;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard,Long> {

    List<CreditCard> findByClient(User client);
}
