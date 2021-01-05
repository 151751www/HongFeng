package zhwy.service;

import com.alibaba.fastjson.JSONArray;

public interface EquitmentService {

 //加载设备信息
 JSONArray  getEquitment(String riversNum,String EquName)throws Exception;
 //新增设备
 String addEquitment(String equipment_num,String rivers_num, String equipment_name, String lon,String lat,String information,String equipmenr_status,String equipment_type)throws Exception;
 //修改设备
 String updateEquitment(String equipment_num,String rivers_num, String equipment_name, String lon,String lat,String information,String equipmenr_status,String equipment_type,String oldEquipmentNum)throws Exception;
//删除设备
 String delEquitment(String equipment_num,String rivers_num)throws Exception;
}
