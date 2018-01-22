package com.chernik.internetprovider.servlet.command.commandimpl.transaction;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.Transaction;
import com.chernik.internetprovider.service.TransactionService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.TransactionMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/transaction/new", method = RequestType.POST)
public class TransactionCreateCommandPost implements Command {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Transaction transaction = transactionMapper.create(request);
        transactionService.create(transaction);
    }
}
