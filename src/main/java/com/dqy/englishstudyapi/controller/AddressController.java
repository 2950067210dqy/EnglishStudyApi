package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.AddressService;
import com.dqy.englishstudyapi.tablebean.Address;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-22
 */
@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;
    @PostMapping("/selectExist")
    public  ReturnVO selectExist(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            ArrayList<Address> addresses = (ArrayList<Address>) addressService.list(new QueryWrapper<Address>().eq("uid",uid));
            if (addresses!=null&&addresses.size()!=0){
                returnVO.setCode(200);
                returnVO.setMessage("存在收获地址");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在收获地址");
                return returnVO;
            }
        }
    }

    @PostMapping("/selectDefault")
    public  ReturnVO selectDefault(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Map<String,Object> params = new HashMap<>();
            params.put("uid",uid);
            params.put("defaults",0);
            Address address = addressService.getOne(new QueryWrapper<Address>().allEq(params));
            if (address!=null){
                returnVO.setCode(200);
                returnVO.setMessage("存在默认地址");
                returnVO.setData(address);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在默认地址");
                return returnVO;
            }
        }
    }

    @PostMapping("/select")
    public  ReturnVO select(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            ArrayList<Address> addresses = (ArrayList<Address>) addressService.list(new QueryWrapper<Address>().eq("uid",uid));
            if (addresses!=null&&addresses.size()!=0){
                returnVO.setCode(200);
                returnVO.setMessage("存在收获地址");
                returnVO.setData(addresses);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在收获地址");
                return returnVO;
            }
        }
    }

    @PostMapping("/delete")
    public  ReturnVO delete(@RequestParam("id")Integer id,@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (id==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Address address = addressService.getById(id);
            if (address!=null){
                boolean result0 =addressService.removeById(address);
                if (result0){
                    if (address.getDefaults()==0){
                        ArrayList<Address> addresses = (ArrayList<Address>) addressService.list(new QueryWrapper<Address>().eq("uid",address.getUid()));
                        if (addresses!=null){
                            addresses.get(0).setDefaults(0);
                            boolean result =  addressService.updateById(addresses.get(0));
                            if (result){
                                returnVO.setCode(200);
                                returnVO.setMessage("修改默认地址成功");
                                return returnVO;

                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("修改其他地址为默认地址失败");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("该用户不存在收获地址");
                            return returnVO;
                        }
                    }
                    returnVO.setCode(200);
                    returnVO.setMessage("删除地址成功");
                    return returnVO;

                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除地址失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在该地址");
                return returnVO;
            }
        }
    }



    @PostMapping("/setDefault")
    public  ReturnVO setDefault(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Address address = addressService.getById(id);
            if (address!=null){
                address.setDefaults(0);
                ArrayList<Address> addresses = (ArrayList<Address>) addressService.list(new QueryWrapper<Address>().eq("uid",address.getUid()));
                if (addresses!=null&&addresses.size()!=0){
                    for (int i = 0; i <addresses.size() ; i++) {
                        addresses.get(i).setDefaults(1);
                    }
                    boolean result =  addressService.updateBatchById(addresses);
                    if (result){
                        boolean result2 =addressService.updateById(address);
                        if (result2){
                            returnVO.setCode(200);
                            returnVO.setMessage("修改默认地址成功");
                            return returnVO;
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("修改默认地址失败");
                            return returnVO;
                        }

                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("修改其他地址为非默认地址失败");
                        return returnVO;
                    }

                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("不存在收获地址");
                    return returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在默认地址");
                return returnVO;
            }
        }
    }

    @PostMapping("/insert")
    public  ReturnVO insert(@RequestBody Address address){
        returnVO = new ReturnVO();
        if (address==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            address.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            address.setDeleted(0);
            Map<String,Object> params = new HashMap<>();
            params.put("uid",address.getUid());
            params.put("defaults",0);
            Address addresss = addressService.getOne(new QueryWrapper<Address>().allEq(params));
            if (addresss!=null){
                address.setDefaults(1);
            }else{
                address.setDefaults(0);
            }
            Map<String,Object> params2 = new HashMap<>();
            params2.put("uid",address.getUid());
            params2.put("address",address.getAddress());
            params2.put("phone",address.getPhone());
            params2.put("name",address.getName());
            Address addressss = addressService.getOne(new QueryWrapper<Address>().allEq(params2));
            if (addressss !=null){
                returnVO.setCode(500);
                returnVO.setMessage("已存在该地址和手机号和姓名");
                return returnVO;
            }else{
                boolean result = addressService.save(address);
                if (result){
                    returnVO.setCode(200);
                    returnVO.setMessage("保存成功");
                    return returnVO;
                }else {
                    returnVO.setCode(500);
                    returnVO.setMessage("保存失败");
                    return returnVO;
                }
            }

        }
    }

    @PostMapping("/update")
    public  ReturnVO update(@RequestBody Address address){
        returnVO = new ReturnVO();
        if (address==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Map<String,Object> params = new HashMap<>();
            params.put("uid",address.getUid());
            params.put("address",address.getAddress());
            params.put("phone",address.getPhone());
            params.put("name",address.getName());
            Address addresss = addressService.getOne(new QueryWrapper<Address>().allEq(params));
            if (addresss!=null){
                returnVO.setCode(500);
                returnVO.setMessage("已存在该地址和手机号和姓名");
                return returnVO;
            }else{
                boolean result = addressService.updateById(address);
                if (result){
                    returnVO.setCode(200);
                    returnVO.setMessage("修改成功");
                    return returnVO;
                }else {
                    returnVO.setCode(500);
                    returnVO.setMessage("修改失败");
                    return returnVO;
                }
            }

        }
    }
}
