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
import zhwy.service.RiversService;
import zhwy.util.Common;

import java.net.URLDecoder;
import java.util.Map;

@Api(position = 9,tags = "洪峰----河道信息管理")
@RestController
@SessionAttributes
@RequestMapping("/RiversInfo")
public class RiversController {
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    Common common;
    @Autowired
    RiversService riversService;
    @Autowired
    BollService bollService;


    @ApiOperation(value = "河道信息查询")
    @PostMapping("/getRiversInfo")
    @ApiImplicitParam(name = "rivers_name", value = "河道名称", required = false, paramType = "query", dataType = "String")

    public String getRiversInfo(String rivers_name) {
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            JSONArray jsonArray =riversService.getRiversInfo(rivers_name);
            jsonResult.setData(jsonArray);
            jsonResult.setStatus("0");
            jsonResult.setErrorMessage("");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }

    @ApiOperation(value = "河道和投放点信息查询")
    @PostMapping("/getRiversAndbollInfo")
    @ApiImplicitParam(name = "rivers_name", value = "河道名称", required = false, paramType = "query", dataType = "String")

    public String getRiversAndbollInfo(String rivers_name) {
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            JSONArray jsonArray =riversService.getRiversAndbollInfo(rivers_name);
            jsonResult.setData(jsonArray);
            jsonResult.setStatus("0");
            jsonResult.setErrorMessage("");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }
    @ApiOperation(value = "新增河道信息")
    @PostMapping("/addRiversInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rivers_info", value = "河道信息字符串[{ \"河道编号\":10001, \"河道名称\":\"滹沱河\",\"经纬度\":\"[{lng:113.535564040048480,lat:38.423803324963501},...]\", \"投放点\":\"[{投放点1：{113.145，38.521}}，{投放点2：{113.145，34.521}},...]\"}]", required = true, paramType = "query", dataType = "String")

    })
    public String addRiversInfo(String rivers_info){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            rivers_info= URLDecoder.decode(rivers_info,"utf-8");
            String result =riversService.addRiversInfo(rivers_info);
            if(result.indexOf("成功")>-1){
                JSONArray array=JSONArray.parseArray(rivers_info);
                JSONArray resultarr=new JSONArray();
                for (int n=0;n<array.size();n++){
                    JSONObject riverinfo=array.getJSONObject(n);
                    String riversname=common.StrNull(riverinfo.getString("河道名称"));
                    String toufangdian=common.StrNull(riverinfo.getString("投放点"));
                    if(!toufangdian.equals("")){
                        JSONArray toufangdians=JSONArray.parseArray(toufangdian);
                        for (int i=0;i<toufangdians.size();i++){
                            JSONObject info=new JSONObject();
                            JSONObject dian=toufangdians.getJSONObject(i);
                            for (Map.Entry<String, Object> map:dian.entrySet()) {
                                info.put("河道名称",riversname);
                                info.put("投放点",map.getKey());
                                info.put("经纬度",map.getValue().toString());
                            }
                            resultarr.add(info);
                        }
                    }
                }
                String bollresu=bollService.addBollToRivers(resultarr.toJSONString());
                if(bollresu.indexOf("成功")>-1){
                    jsonResult.setData("新增成功");
                    jsonResult.setStatus("0");
                    jsonResult.setErrorMessage("");
                }else{
                    jsonResult.setData("");
                    jsonResult.setStatus("0");
                    jsonResult.setErrorMessage(bollresu);
                }
            }else{
                jsonResult.setData("");
                jsonResult.setStatus("0");
                jsonResult.setErrorMessage(result);
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

    @ApiOperation(value = "修改河道信息")
    @PostMapping("/updateRiversInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rivers_num", value = "河道编号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "rivers_aftername", value = "修改后河道名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "rivers_lng", value = "修改后河道经纬度", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "max_val", value = "修改后阈值", required = false, paramType = "query", dataType = "String")

    })
    public String updateRiversInfo(String rivers_beforname,String rivers_aftername,String rivers_lng,String max_val){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {
            if((rivers_aftername==null||rivers_aftername.equals(""))&&(rivers_lng==null||rivers_lng.equals(""))&&(max_val==null||max_val.equals(""))){
                jsonResult.setData("");
                jsonResult.setStatus("0");
                jsonResult.setErrorMessage("请输入需要修改的河道的数据");
            }else{
                String result =riversService.updateRiverInfo(rivers_beforname, rivers_aftername, rivers_lng, max_val);
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
            jsonResult.setData("");
            jsonResult.setStatus("1");
            jsonResult.setErrorMessage(e.getMessage());
        }
        return jsonResult.toJsonResut();
    }
    @ApiOperation(value = "删除河道信息")
    @PostMapping("/delRiver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rivers_num", value = "河道编号", required = true, paramType = "query", dataType = "String")
    })
    public String delRivers(String rivers_num){
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        try {

            String result =riversService.delRivers(rivers_num);
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
