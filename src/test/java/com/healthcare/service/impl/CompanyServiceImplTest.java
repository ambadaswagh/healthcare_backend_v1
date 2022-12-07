package com.healthcare.service.impl;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.healthcare.DbUnitIntegrationTestConfiguration;
import com.healthcare.model.entity.Company;
import com.healthcare.service.CompanyService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@TestExecutionListeners(
        value = {DbUnitTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup(value = "/dataset/service/CompanyServiceImplIntegrationTest.xml")
@ContextConfiguration(classes = {DbUnitIntegrationTestConfiguration.class})
@Transactional
@SpringBootTest
public class CompanyServiceImplTest {
    @Autowired
    CompanyService companyService;

    private Company company;

    @Before
    public void setUp() throws Exception {
        company = null;
    }

    @After
    public void tearDown() throws Exception {
        if (company != null)
            companyService.deleteById(company.getId());
    }

    @Test
    public void testDisableById() throws Exception {
        company = createNewCompany();
        Assert.assertNotNull(companyService.save(company).getId());
        companyService.disableById(company.getId());
        Company disableCompany = companyService.findById(company.getId());
        Assert.assertNotNull(disableCompany.getId());
        Assert.assertEquals(0, disableCompany.getStatus());
    }

    private Company createNewCompany() {
        final Long companyId = 100L;
        final Company company = new Company();
        company.setId(companyId);
        return company;
    }
}