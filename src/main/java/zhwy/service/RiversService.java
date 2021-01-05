package zhwy.service;

import com.alibaba.fastjson.JSONArray;

public interface RiversService {

 //仅加载河道
 JSONArray  getRiversInfo(String rivers_name)throws Exception;
 //新增河道信息
 String addRiversInfo(String rivers_info)throws Exception;
 //修改河道信息
 String updateRiverInfo(String rivers_num, String rivers_aftername, String rivers_lng, String max_val)throws Exception;
 //加载河道和投放点
 JSONArray getRiversAndbollInfo(String rivers_name)throws Exception;
//删除河道信息
 String delRivers(String rivers_num)throws Exception;
}
