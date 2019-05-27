package com.acme.banking.dbo.spring.service;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:test-spring-config.xml", "classpath:spring-config.xml"})
@ActiveProfiles("test")
public class DemoIntegrationTestWithSpringBootTest {
    @Autowired private ReportingService reportingService;
    @Autowired private TransferService transferService;
    @MockBean private AccountRepository accounts; //mock(AccountRepository.class);

    @Test
    public void shouldUseStubInsteadOfProductionClassWhenCreateSpringContext() {
        Account accountStub = mock(Account.class);
        when(accountStub.toString()).thenReturn("0 100.0 S");
        when(accounts.findById(anyLong())).thenReturn(Optional.of(accountStub));

        assertThat(reportingService.accountReport(1L))
                .isEqualTo("## 0 100.0 S");
    }

    @Test //TODO Interaction-based VS State-based testing
    public void shouldCallPropertiesForAccountsWhenTransfer() {
        Account accountFromStub = mock(Account.class);
        when(accountFromStub.getAmount()).thenReturn(100d);
        when(accounts.findById(1L)).thenReturn(Optional.of(accountFromStub));

        Account accountToStub = mock(Account.class);
        when(accountToStub.getAmount()).thenReturn(100d);
        when(accounts.findById(2L)).thenReturn(Optional.of(accountToStub));

        transferService.transfer(1L, 2L, 100);

        verify(accountFromStub, times(1)).setAmount(0);
        verify(accountToStub).setAmount(200);
    }
}
