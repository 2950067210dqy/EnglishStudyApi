package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicProductSetting;

import com.dqy.englishstudyapi.tablebean.Products;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class ProductSimpleFull {
    Products products;
    String dsc;
    String parentDsc;
}
