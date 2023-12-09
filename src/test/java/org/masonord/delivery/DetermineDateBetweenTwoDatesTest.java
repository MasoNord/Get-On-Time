package org.masonord.delivery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.repository.dao.CompletedOrderDao;
import org.masonord.delivery.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.text.ParseException;
import java.util.List;

@SpringBootTest
public class DetermineDateBetweenTwoDatesTest {
    @Autowired
    CompletedOrderDao completedOrderDao;

    @Test
    @Description("Should determine if a date lays between two dates")
    void testDetermineDateBetweenTwoDatesFunction() throws ParseException {
        List<CompletedOrder> completedOrderList = completedOrderDao.getCompletedOrders();


        Assertions.assertTrue( DateUtils.DetermineDateBetweenTwoDates("2023-11-20", "2023-11-28", completedOrderList.get(1).getCompletedTime()));
    }
}
