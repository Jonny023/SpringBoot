package org.core.acid.config.freemarker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreemarkerConfig {

    /**
     * 增加自定义视图变量和方法
     *
     * @return
     */
    @Bean
    public CommandLineRunner customFreemarker(FreeMarkerViewResolver resolver) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                //增加视图
                resolver.setViewClass(MyFreemarkerView.class);
            }
        };
    }

}
