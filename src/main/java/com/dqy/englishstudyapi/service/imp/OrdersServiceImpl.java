package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.ProductsService;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.ScoresourceService;
import com.dqy.englishstudyapi.tablebean.Orders;
import com.dqy.englishstudyapi.mapper.OrdersMapper;
import com.dqy.englishstudyapi.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.tablebean.Products;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.Scoresource;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-22
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    ProductsService productsService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    TimeUtil timeUtil;
    @Override
    public SubReturnVo cancel(Integer id) {
        SubReturnVo subReturnVO = new SubReturnVo();
        Orders orders = getById(id);
        if (orders != null) {
            //退款
            if (orders.getOrderstatus()!=5){
                Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid",orders.getUid()));
                if (score!=null){
                    Products products = productsService.getOne(new QueryWrapper<Products>().eq("id",orders.getPid()));
                    if (products!=null){
                        score.setScore(products.getPrice().intValue()+score.getScore());
                        boolean result =  scoreService.updateById(score);
                        if (result){
                            Scoresource scoresource = new Scoresource();
                            scoresource.setUid(orders.getUid());
                            scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                            scoresource.setDeleted(0);
                            scoresource.setNum(Long.valueOf(products.getPrice().intValue()));
                            scoresource.setSourceid(13);
                            boolean result2 =  scoresourceService.save(scoresource);
                            if (!result2){
                                subReturnVO.setCode(500);
                                subReturnVO.setMessage("积分来源保存错误");
                                subReturnVO.setResult(false);
                                return subReturnVO;
                            }
                        }else{
                            subReturnVO.setCode(500);
                            subReturnVO.setMessage("退款错误");
                            subReturnVO.setResult(false);
                            return subReturnVO;
                        }
                    }else{
                        subReturnVO.setCode(500);
                        subReturnVO.setMessage("商品错误");
                        subReturnVO.setResult(false);
                        return subReturnVO;
                    }
                }else{
                    subReturnVO.setCode(500);
                    subReturnVO.setMessage("获取账户错误");
                    subReturnVO.setResult(false);
                    return subReturnVO;
                }
            }
            //退款end
            orders.setOrderstatus(4);
            orders.setUpdatetime(timeUtil.getNowLocalDateTime());
            boolean result = updateById(orders);
            if (result) {
                Products products = productsService.getById(orders.getPid());
                if (products != null) {
                    products.setNum(products.getNum() + 1);
                    boolean result0 = productsService.updateById(products);
                    if (result0) {
                        subReturnVO.setCode(200);
                        subReturnVO.setMessage("更新订单成功");
                        subReturnVO.setResult(true);
                        return subReturnVO;
                    } else {
                        subReturnVO.setCode(500);
                        subReturnVO.setMessage("更新商品数量失败");
                        subReturnVO.setResult(false);
                        return subReturnVO;
                    }
                } else {
                    subReturnVO.setCode(500);
                    subReturnVO.setMessage("获取商品错误");
                    subReturnVO.setResult(false);
                    return subReturnVO;
                }

            } else {
                subReturnVO.setCode(500);
                subReturnVO.setMessage("更新订单错误");
                subReturnVO.setResult(false);
                return subReturnVO;
            }

        }else{
            subReturnVO.setCode(500);
            subReturnVO.setResult(false);
            subReturnVO.setMessage("获取订单错误");
            return  subReturnVO;
        }
    }
}
