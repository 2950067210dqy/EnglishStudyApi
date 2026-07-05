package com.dqy.englishstudyapi.entity.frontEntity.OrderFull;

import com.dqy.englishstudyapi.tablebean.Address;
import com.dqy.englishstudyapi.tablebean.Orders;
import com.dqy.englishstudyapi.tablebean.Products;
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
public class OrderFull {
    Logistics logistics;
    Orders orders;
    Products products;
    Address address;
}
