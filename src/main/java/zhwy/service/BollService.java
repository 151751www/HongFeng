package zhwy.service;

import com.alibaba.fastjson.JSONArray;

public interface BollService {

 //仅加载河道
 JSONArray  getBollInfo(String riversName,String bollName)throws Exception;
 //新增投放点
 String addBollToRivers(String  array)throws Exception;
 //修改投放点
 String updateBollToRivers(String boll_num,String boll_name,String lot)throws Exception;

 String delBollToRivers(String boll_num,String rivers_num)throws Exception;
}
