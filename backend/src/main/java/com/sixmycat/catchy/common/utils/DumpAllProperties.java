package com.sixmycat.catchy.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class DumpAllProperties implements ApplicationRunner {

    @Autowired
    private Environment env;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("S3와 관련된 환경변수 로그 출력");
        for (Iterator<PropertySource<?>> it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource<?> propertySource = it.next();
            if (propertySource instanceof EnumerablePropertySource<?>) {
                for (String name : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                    if (name.contains("cloud") || name.contains("aws") || name.contains("s3")) {
                        System.out.printf("  ✅ %s = %s%n", name, env.getProperty(name));
                    }
                }
            }
        }
    }
}
