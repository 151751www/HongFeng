package zhwy.controller;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import zhwy.pojo.JsonResult;
import zhwy.service.BollService;
import zhwy.service.RiversService;
import zhwy.util.Common;

import java.net.URLDecoder;

@Api(position = 9,tags = "洪峰----投放点信息管理")
@RestController
@SessionAttributes
@RequestMapping("/BollInfo")
public class BollController {
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    Common common;
    @Autowired
    BollService bollService;


    @ApiOperation(value = "投放点信息查询")
    @PostMapping("/getBollInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rivers_name", value = "河道名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bollName", value = "投放点名称", required = false, paramType = "query", dataType = "String")

    })
    public String getBollInfo(String rivers_name,String bollName) {
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            JSONArray jsonArray =bollService.getBollInfo(rivers_name,bollName);
            jsonResult.setData(jsonArray);
            jsonResult.setStatus("0");
            jsonResult.setErrorMessage("");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            jsonResult.setData("");
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }

    @ApiOperation(value = "新增投放点信息")
    @PostMapping("/addBollInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BollInfo", value = "投放点信息字符串[{\"河道名称\"：\"滹沱河\",\"投放点\"：\"投放点1\"：\"经纬度\"：\"{114.9782,38.0886}\"，...}]", required = true, paramType = "query", dataType = "String")
    })
    public String addRiversInfo(String BollInfo){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            BollInfo= URLDecoder.decode(BollInfo,"utf-8");
            String result =bollService.addBollToRivers(BollInfo);
            if(result.indexOf("失败")!=-1){
                jsonResult.setData("");
                jsonResult.setStatus("0");
                jsonResult.setErrorMessage(result);
            }else{
                jsonResult.setData(result);
                jsonResult.setStatus("0");
                jsonResult.setErrorMessage("");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            jsonResult.setData("");
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }
    @ApiOperation(value = "修改投放点信息")
    @PostMapping("/updateBollInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boll_num", value = "投放点编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "boll_name", value = "投放点", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "lot", value = "投放点经纬度", required = false, paramType = "query", dataType = "String")
    })
    public String updateBollInfo(String boll_num,String boll_name,String lot){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            if((boll_name==null||boll_name.equals(""))&&(lot==null||lot.equals(""))){
                jsonResult.setData("");
                jsonResult.setStatus("0");
                jsonResult.setErrorMessage("请输入需要修改的投放点的数据");
            }else{
                String result =bollService.updateBollToRivers(boll_num,boll_name,lot);
                jsonResult.setStatus("0");
                if(result.indexOf("成功")!=-1){
                    jsonResult.setData(result);
                    jsonResult.setErrorMessage("");
                }else{
                    jsonResult.setData("");
                    jsonResult.setErrorMessage(result);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }
    @ApiOperation(value = "删除投放点信息")
    @PostMapping("/delBollInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boll_num", value = "投放点编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "rivers_num", value = "河道编号", required = true, paramType = "query", dataType = "String")
    })
    public String delBollInfo(String boll_num,String rivers_num){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {

            String result =bollService.delBollToRivers(boll_num,rivers_num);
            jsonResult.setStatus("0");
            if(result.indexOf("成功")!=-1){
                jsonResult.setData(result);
                jsonResult.setErrorMessage("");
            }else{
                jsonResult.setData("");
                jsonResult.setErrorMessage(result);
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }
}
