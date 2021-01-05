package zhwy.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import zhwy.service.EquitmentService;
import zhwy.service.RiversService;
import zhwy.util.Common;

import java.net.URLDecoder;

@Api(position = 9,tags = "洪峰----设备信息管理")
@RestController
@SessionAttributes
@RequestMapping("/EquitmentInfo")
public class EquitmentController {
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    Common common;
    @Autowired
    EquitmentService equitmentService;
    @Autowired
    RiversService riversService;


    @ApiOperation(value = "设备信息查询")
    @PostMapping("/getEquitmentInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rivers_name", value = "河道名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EquName", value = "设备名称", required = false, paramType = "query", dataType = "String")

    })
    public String getEquitmentInfo(String rivers_num,String EquName) {
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            JSONArray jsonArray =equitmentService.getEquitment(rivers_num,EquName);
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

    @ApiOperation(value = "新增设备信息")
    @PostMapping("/addEquitmentInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipment_num", value = "设备编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="rivers_name",value = "河道名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="equipment_name",value = "设备名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="lon",value = "经度", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="lat",value = "纬度", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="information",value = "检测信息", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="equipmenr_status",value = "设备状态（0，停用，1启用）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="equipment_type",value = "设备类型", required = true, paramType = "query", dataType = "String")

    })
    public String addEquitmentInfo(String equipment_num,String rivers_name, String equipment_name, String lon,String lat,String information,String equipmenr_status,String equipment_type){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        String result="";
        try {
            JSONArray riverArr=riversService.getRiversAndbollInfo(rivers_name);
            if(riverArr.size()>0){
                JSONObject riverobj=riverArr.getJSONObject(0);
                String rivers_num=riverobj.getString("河道编号");
                JSONArray equArray =equitmentService.getEquitment(rivers_num,equipment_name);
                if(equArray.size()==0){
                    result=equitmentService.addEquitment( equipment_num, rivers_num,  equipment_name,  lon, lat, information, equipmenr_status, equipment_type);
                }else{
                    result="新增失败，已存在相同河道和相同编号的设备信息。";
                }
            }else{
                result="新增失败，河道名称不存在";
            }
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
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }
    @ApiOperation(value = "修改投放点信息")
    @PostMapping("/updateEquitmentInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipment_oldnum", value = "原设备编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "equipment_num", value = "修改后设备编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="rivers_name",value = "河道名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="equipment_name",value = "设备名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="lon",value = "经度", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="lat",value = "纬度", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="information",value = "检测信息", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="equipmenr_status",value = "设备状态（0，停用，1启用）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="equipment_type",value = "设备类型", required = true, paramType = "query", dataType = "String")
    })
    public String updateEquitmentInfo(String equipment_num,String rivers_name, String equipment_name, String lon,String lat,String information,String equipmenr_status,String equipment_type,String equipment_oldnum){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        String result="";
        try {
            JSONArray riverArr=riversService.getRiversAndbollInfo(rivers_name);
            if(riverArr.size()>0){
                JSONObject riverobj=riverArr.getJSONObject(0);
                String rivers_num=riverobj.getString("河道编号");
                JSONArray equArray =equitmentService.getEquitment(rivers_num,equipment_name);
                if(equArray.size()==0){
                     result =equitmentService.updateEquitment(equipment_num,rivers_name,equipment_name,lon,lat,information,equipmenr_status,equipment_type,equipment_oldnum);
                }else{
                    result="修改失败，已存在相同河道和相同编号的设备信息。";
                }
            }else{
                result="修改失败，河道名称不存在";
            }
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
    @ApiOperation(value = "删除投放点信息")
    @PostMapping("/delEquitmentInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipment_num", value = "设备编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name="rivers_name",value = "河道名称", required = true, paramType = "query", dataType = "String")
    })
    public String delEquitmentInfo(String equipment_num,String rivers_name ){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            String result="";
            JSONArray riverArr=riversService.getRiversAndbollInfo(rivers_name);
            if(riverArr.size()>0){
                String riverName=riverArr.getJSONObject(0).getString("河道编号");
                result =equitmentService.delEquitment(equipment_num,riverName);
            }
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
