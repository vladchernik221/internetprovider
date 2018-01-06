package com.chernik.internetprovider.persistence.repository.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Discount;
import com.chernik.internetprovider.persistence.repository.DiscountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class DiscountRepositoryImpl implements DiscountRepository{
    private final static Logger LOGGER = LogManager.getLogger(DiscountRepositoryImpl.class);


    private final static String CREATE_DISCOUNT = "INSERT INTO `discount`(`description`, `amount`, `start_date`, `end_date`, `only_for_new_client`) VALUES (?,?,?,?,?)";

    private final static String UPDATE_DISCOUNT = "UPDATE `discount` SET `description`=?, `amount`=?, `end_date`=?, `only_for_new_client`=? WHERE `discount_id`=?";

    private final static String GET_DISCOUNT_PAGE_COUNT = "SELECT CEIL(COUNT(*)/?) FROM `discount`";

    private final static String GET_DISCOUNT_PAGE = "SELECT `discount_id`, `description`, `amount`, `start_date`, `end_date`, `only_for_new_client` FROM `discount` LIMIT ? OFFSET ?";

    private final static String GET_DISCOUNT_BY_ID = "SELECT `discount_id`, `description`, `amount`, `start_date`, `end_date`, `only_for_new_client` FROM `discount` WHERE `discount_id`=?";

    private final static String REMOVE_DISCOUNT = "DELETE FROM `discount` WHERE `discount_id`=?";

    private final static String EXISTS_DISCOUNT_BY_ID = "SELECT EXISTS(SELECT 1 FROM `discount` WHERE `discount_id`=?)";


    @Autowired
    private ConnectionPool connectionPool;


    @Override
    public Long create(Discount discount) {
        return null;
    }

    @Override
    public void update(Discount discount) {

    }

    @Override
    public Page<Discount> getDiscounts(Pageable pageable) {
        return null;
    }

    @Override
    public Discount getDiscount(Long id) {
        return null;
    }

    @Override
    public void remove(Discount discount) {

    }

    @Override
    public boolean isDiscountExistWithId(Long id) {
        return false;
    }
}
