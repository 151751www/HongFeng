package zhwy.controller;

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
import zhwy.util.Common;


@Api(position = 9,tags = "洪峰----用户信息")
@RestController
@SessionAttributes
@RequestMapping("/UserInfo")
public class UserInfoController {
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    Common common;


    /*@ApiOperation(value = "用户新增")
    @PostMapping("/addUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public String addUser() {
        common.getCrossOrigin();
        JsonResult jsonResult=new JsonResult();
        jsonResult.setData("新增成功");
        jsonResult.setStatus("0");
        jsonResult.setErrorMessage("");
        return jsonResult.toJsonResut();
    }*/

}
