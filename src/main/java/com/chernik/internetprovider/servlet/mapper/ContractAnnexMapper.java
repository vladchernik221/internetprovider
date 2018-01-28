package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Contract;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import javax.servlet.http.HttpServletRequest;

@Component
public class ContractAnnexMapper extends BaseMapper implements Mapper<ContractAnnex> {

    @Override
    public ContractAnnex create(HttpServletRequest request) throws BadRequestException {
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setAddress(getMandatoryString(request, "address"));

        Long tariffPlanId = getMandatoryLong(request, "tariffPlanId");
        contractAnnex.setTariffPlan(new TariffPlan(tariffPlanId));

        Long contractId = Long.valueOf(request.getRequestURI().split("/")[2]);
        contractAnnex.setContract(new Contract(contractId));
        return contractAnnex;
    }
}
