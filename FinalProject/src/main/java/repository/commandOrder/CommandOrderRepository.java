package repository.commandOrder;

import model.CommandOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandOrderRepository  extends JpaRepository<CommandOrder,Long> {
}
