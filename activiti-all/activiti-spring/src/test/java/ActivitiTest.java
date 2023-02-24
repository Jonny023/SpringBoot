import com.example.MainConfig;
import org.activiti.engine.RuntimeService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfig.class)
public class ActivitiTest {

    @Autowired
    private ProcessEngineFactoryBean processEngineFactoryBean;

    @Resource
    private RuntimeService runtimeService;

    @Test
    public void testRepo() throws Exception {
        System.out.println(processEngineFactoryBean);
        System.out.println(runtimeService);
        System.out.println(processEngineFactoryBean.getObject().getRuntimeService());
    }
}
