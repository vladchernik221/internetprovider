package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import javax.servlet.http.HttpServletRequest;

@Component
public class ContractAnnexMapper extends Mapper<ContractAnnex> {

    @Override
    public ContractAnnex create(HttpServletRequest request) throws BadRequestException {
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setAddress(getMandatoryString(request.getParameter("address")));

        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setTariffPlanId(getMandatoryLong(request.getParameter("tariffPlanId")));
        contractAnnex.setTariffPlan(tariffPlan);

        Contract contract = new Contract();
        contract.setContractId(Long.valueOf(request.getRequestURI().split("/")[2]));
        contractAnnex.setContract(contract);
        return contractAnnex;
    }
}
