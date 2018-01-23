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
        transaction.setType(TransactionType.REFILL);
        transaction.setAmount(getMandatoryBigDecimal(request, "amount"));

        Account account = new Account();
        Long contractAnnexId = getMandatoryLong(request, "contractAnnexId");
        account.setContractAnnex(new ContractAnnex(contractAnnexId));
        transaction.setAccount(account);
        return transaction;
    }
}
