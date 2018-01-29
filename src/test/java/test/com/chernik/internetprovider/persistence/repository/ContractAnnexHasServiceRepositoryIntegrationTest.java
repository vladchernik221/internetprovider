package test.com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.ContextInitializer;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.persistence.repository.ContractAnnexHasServiceRepository;
import com.chernik.internetprovider.persistence.repository.ServiceRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.com.chernik.internetprovider.util.RepositoryIntegrationTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ContractAnnexHasServiceRepositoryIntegrationTest extends RepositoryIntegrationTest {

    private ContractAnnexHasServiceRepository contractAnnexHasServiceRepository;

    private ServiceRepository serviceRepository;

    public ContractAnnexHasServiceRepositoryIntegrationTest(String dataScriptName) {
        super("ContractAnnexHasServiceRepositoryIntegrationTest");
    }

    @BeforeClass
    public void init() {
        ContextInitializer contextInitializer = ContextInitializer.getInstance();
        contractAnnexHasServiceRepository = contextInitializer.getComponent(ContractAnnexHasServiceRepository.class);
        serviceRepository = contextInitializer.getComponent(ServiceRepository.class);
    }


    private Page<Service> createServicePage() {
        List<Service> services = new ArrayList<>();

        Service service = new Service();
        service.setServiceId(1L);
        service.setName("Service 1");
        service.setArchived(false);
        service.setPrice(new BigDecimal("12.22"));
        services.add(service);

        service = new Service();
        service.setServiceId(2L);
        service.setName("Service 2");
        service.setArchived(true);
        service.setPrice(new BigDecimal("6.99"));
        services.add(service);

        Page<Service> servicePage = new Page<>();
        servicePage.setPagesCount(1);
        servicePage.setData(services);

        return servicePage;
    }

    @Test
    public void createShouldSaveCreatedContractAnnexHasService() throws Exception {
        contractAnnexHasServiceRepository.create(1L, 1L);
        contractAnnexHasServiceRepository.create(1L, 2L);
        Page<Service> expected = createServicePage();
        Page<Service> actual = serviceRepository.getPageByContractAnnexId(1L, new Pageable(0, 2));

        assertEquals(actual, expected);
    }
}
