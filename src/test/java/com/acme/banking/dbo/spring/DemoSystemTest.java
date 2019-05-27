package com.acme.banking.dbo.spring;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.CheckingAccount;
import com.acme.banking.dbo.spring.service.ReportingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:test-spring-config.xml", "classpath:spring-config.xml"})
@ActiveProfiles({"test","system-test"})
@TestPropertySource("classpath:application.properties")
@Transactional
public class DemoSystemTest {
    @Autowired private ReportingService reportingService;
    @Autowired private AccountRepository accountRepository;
    @Autowired ApplicationContext context;

    /** TODO Test State */
    private long justSavedAccountId;

    @Test @DirtiesContext //Performance issue
    public void shouldCreateCorrectContextWithInjections() {
        reportingService.setLayoutMarker("+");
        assertThat(reportingService.accountReport(1))
                .isEqualTo("++ 1 100.0 S");
    }

    @Test
    public void shouldRecreateContextForEachTestRunWhenDirtiesContext() {
        assertThat(reportingService.accountReport(1))
                .isEqualTo("## 1 100.0 S");
    }

    @Test @Rollback(true)
    public void shouldNotLeaveSideEffectInDb() {
        CheckingAccount justSavedAccount = accountRepository.save(new CheckingAccount(100, 200, "xx@xx.ru"));
        justSavedAccountId = justSavedAccount.getId();

        assertThat(accountRepository.findById(justSavedAccountId).get().getId())
                .isEqualTo(justSavedAccount.getId());
    }

    @Test
    public void shouldNotLeaveSideEffectInDbCheck() {
        assertThat(accountRepository.findById(justSavedAccountId).isPresent()).isFalse();
    }
}
