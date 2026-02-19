package np.dev.kd.springbootdemoapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootdemoapplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootdemoapplicationApplication.class, args);
	}

}
