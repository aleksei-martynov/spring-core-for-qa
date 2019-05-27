package com.acme.banking.dbo.spring;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.SavingAccount;
import com.acme.banking.dbo.spring.service.ReportingService;
import com.acme.banking.dbo.spring.service.TransferService;
import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "test,prod"); //-Dspring.profiles.active="test,prod"

        /** ApplicationContext vs BeanFactory: https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#context-introduction-ctx-vs-beanfactory */
        try(ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test-spring-config.xml", "spring-config.xml")) {
            //TODO Spring component acqusition styles: by type, by id, both
            Logger logger = context.getBean(Logger.class);

            AccountRepository accountRepository = context.getBean("accountRepository", AccountRepository.class);
            accountRepository.saveAndFlush(new SavingAccount(0, "correct@mail.ru")); /** save() don't call validation till commit */

            ReportingService reportingService = (ReportingService) context.getBean("reportingService");
            logger.info(">>>>> " + reportingService.accountReport(1L)); //TODO Refer project DDL and DML
            logger.info(">>>>> " + reportingService.accountReport(2L));

            context.getBean(TransferService.class).transfer(1L, 2L, 100);
            logger.info(">>>>> " + reportingService.accountReport(1L));
            logger.info(">>>>> " + reportingService.accountReport(2L));

            accountRepository.findAll().forEach(account -> logger.info(">>>>> " + account));
            //TODO And now move to spring-config.xml and make action 'Show Diagram...'
        }
    }
}
