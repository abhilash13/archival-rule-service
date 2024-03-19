package com.archive.ruleservice;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BeanDependencyChecker {
    @Autowired
    private ApplicationContext applicationContext;

    public void printBeanDependencies() {
        String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class);
        for (String beanName : beanNames) {
            String[] dependencies = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class);
            if (dependencies != null && dependencies.length > 0) {
                System.out.println("Bean '" + beanName + "' depends on:");
                Arrays.stream(dependencies).forEach(dependency -> System.out.println("- " + dependency));
            }
        }
    }
}
