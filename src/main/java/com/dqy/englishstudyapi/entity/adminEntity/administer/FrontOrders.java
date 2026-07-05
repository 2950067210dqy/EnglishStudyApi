package com.dqy.englishstudyapi.entity.adminEntity.administer;

import com.dqy.englishstudyapi.entity.frontEntity.OrderFull.Logistics;
import com.dqy.englishstudyapi.tablebean.Address;
import com.dqy.englishstudyapi.tablebean.Orders;
import com.dqy.englishstudyapi.tablebean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class FrontOrders {
    Orders order;
    FrontProducts product;
    Address address;
    Logistics logistics;
    User user;
}
