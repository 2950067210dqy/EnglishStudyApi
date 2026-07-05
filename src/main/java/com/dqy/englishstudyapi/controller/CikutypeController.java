package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.service.CikutypeService;
import com.dqy.englishstudyapi.tablebean.Cikutype;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@RestController
@RequestMapping("cikutype")
public class CikutypeController {
    ReturnVO returnVO;
    @Autowired
    CikutypeService cikutypeService;
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public ReturnVO getAll(){
        returnVO = new ReturnVO();
        List<Cikutype> cikutypes = cikutypeService.list();
        if (cikutypes!=null&&cikutypes.size()!=0){
            returnVO.setData(cikutypes);
            returnVO.setMessage("获取词库类型成功");
            returnVO.setCode(200);
        }else{
            returnVO.setMessage("获取词库类型失败");
            returnVO.setCode(500);
        }
        return  returnVO;
    }

    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public ReturnVO getAll(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null||id==0){
            returnVO.setMessage("数据为空");
            returnVO.setCode(500);
            return returnVO;
        }
        Cikutype cikutype = cikutypeService.getById(id);
        if (cikutype!=null){
            returnVO.setData(cikutype);
            returnVO.setMessage("获取词库类型成功");
            returnVO.setCode(200);
        }else{
            returnVO.setMessage("获取词库类型失败");
            returnVO.setCode(500);
        }
        return  returnVO;
    }
}
