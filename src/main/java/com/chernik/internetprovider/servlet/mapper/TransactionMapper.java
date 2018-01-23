package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.Account;
import com.chernik.internetprovider.persistence.entity.ContractAnnex;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.persistence.entity.TransactionType;

import javax.servlet.http.HttpServletRequest;

@Component
public class TransactionMapper extends Mapper<Transaction> {


    @Override
    public Transaction create(HttpServletRequest request) throws BadRequestException {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.valueOf(getMandatoryString(request, "type")));
        transaction.setAmount(getMandatoryBigDecimal(request, "amount"));

        Long contractAnnexId = getMandatoryLong(request, "contractAnnexId");
        ContractAnnex contractAnnex = new ContractAnnex();
        contractAnnex.setContractAnnexId(contractAnnexId);
        Account account = new Account();
        account.setContractAnnex(contractAnnex);
        transaction.setAccount(account);
        return transaction;
    }
}
