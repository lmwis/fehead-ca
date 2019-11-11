import com.fehead.open.ca.compoment.CertificateGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-11 22:01
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CertificateTest.class)
public class CertificateTest {



    @Test
    public void whenGetKey(){
        System.out.println(CertificateGenerator.getPublicKey());
        System.out.println(CertificateGenerator.getPrivateKey());
    }
}
