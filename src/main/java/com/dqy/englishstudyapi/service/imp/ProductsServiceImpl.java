package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Products;
import com.dqy.englishstudyapi.mapper.ProductsMapper;
import com.dqy.englishstudyapi.service.ProductsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-21
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService {

}
