package es.thepuar.InfiniteSpace;

import es.thepuar.InfiniteSpace.service.api.FileToPng;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


//@ExtendWith(SpringExtension.class)
//@SpringBootTest
class InfiniteSpaceApplicationTests {

	@Autowired
	FileToPng fileToPng;

//	@Test
	void contextLoads() {
		fileToPng.test();
		assertThat(fileToPng).isNotNull();
	}

}
