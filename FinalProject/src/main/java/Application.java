import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("model")
@EnableJpaRepositories({"repository.user","repository.product","repository.productOrder","repository.creditCard"})
@ComponentScan({"model","repository.creditCard","repository.user","repository.product","repository.productOrder",
        "service.productOrder","service.user","service.product","service.creditCard","controller","config"})
public class Application {

    public static void main(String []args)
    {
        SpringApplication.run(Application.class, args);
    }
}
