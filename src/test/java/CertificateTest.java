import com.fehead.open.ca.compoment.CertificateGenerator;
import com.fehead.open.ca.repository.CertificateRepository;
import com.fehead.open.ca.service.CertificateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-11 22:01
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CertificateTest.class)
public class CertificateTest {



    @Autowired
    CertificateRepository certificateRepository;

//    public CertificateTest(CertificateRepository certificateRepository){
//        this.certificateRepository = certificateRepository;
//    }

    @Test
    public void whenGetKey(){
        System.out.println(CertificateGenerator.getPublicKey());
        System.out.println(CertificateGenerator.getPrivateKey());
    }

    @Test
    public void whenFindCertificateByApplication(){
        System.out.println(certificateRepository.findByFeheadAdminApplication("fehead-ca"));
    }
}
