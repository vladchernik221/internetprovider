package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.persistence.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ServiceRepositoryImpl {
    private final static Logger LOGGER = LogManager.getLogger(ServiceRepositoryImpl.class);


    private final static String CREATE_SERVICE = "INSERT INTO `service`(`name`, `description`, `price`, `archived`) VALUES(?,?,?,?)";

    private final static String UPDATE_SERVICE = "UPDATE `service` SET `name`=?, `description`=?, `price`=?, `archived`=? WHERE `service_id`=?";

    private final static String GET_SERVICE_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `service`";


    @Autowired
    private ConnectionPool connectionPool;
}
