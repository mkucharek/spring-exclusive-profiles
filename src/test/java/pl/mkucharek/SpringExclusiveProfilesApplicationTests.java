package pl.mkucharek;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mkucharek.springframework.exclusiveprofiles.SpringExclusiveProfilesApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringExclusiveProfilesApplication.class)
public class SpringExclusiveProfilesApplicationTests {

    @Test
    public void contextLoads() {
    }

}
