package zhwy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.GeneralDao;
import zhwy.service.BollService;
import zhwy.service.EquitmentService;
import zhwy.service.RiversService;
import zhwy.util.Common;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class EquitmentServiceImpl implements EquitmentService {

    @Autowired
    GeneralDao generalDao;
    @Autowired
    Common common;


    @Override
    public JSONArray getEquitment(String riversnum,String EquName) throws Exception {
        StringBuffer sql=new StringBuffer("select river.rivers_num as 河道编号,rivers_name as 河道名称,equipment_num as 设备编号,equipment_name as 设备名称,lon as 经度,lat as 纬度,equ.information as 监测信息,equipmenr_status as 是否启用,equipment_type as  设备类型 from rivers_data_info river INNER JOIN equipment_data_info equ on river.rivers_num=equ.rivers_num  where 1=1 ");
        if(riversnum!=null&&!riversnum.equals("")){
            sql.append("  and equ.rivers_num='");
            sql.append(riversnum);
            sql.append("'");
        }
        if(EquName!=null&&!EquName.equals("")){
            sql.append("  and equ.equipment_name='");
            sql.append(EquName);
            sql.append("'");
        }
        String keys[]={"河道编号","河道名称","设备编号","设备名称","经度","纬度","监测信息","是否启用","设备类型"};
        JSONArray array=generalDao.getDataBySql(sql.toString(),keys);
        return array;
    }

    @Override
    public String addEquitment(String equipment_num,String rivers_num, String equipment_name, String lon,String lat,String information,String equipmenr_status,String equipment_type) throws Exception {
        StringBuffer sql=new StringBuffer("insert into equipment_data_info (rivers_num,equipment_num,equipment_name,lon,lat,information,equipment_status,equipment_type) VALUES(?,?,?,?,?,?,?,?)");
        Object[] objects={equipment_num,rivers_num,equipment_name,lon,lat,information,equipmenr_status,equipment_type};
        int num=generalDao.updateSql(sql.toString(),objects);
        if(num>0){
            return "新增成功";
        }else{
            return "新增失败";
        }
    }

    @Override
    public String updateEquitment(String equipment_num, String rivers_num, String equipment_name, String lon,String lat, String information, String equipmenr_status,String equipment_type,String oldEquipmentNum) throws Exception {
        StringBuffer sql=new StringBuffer("update equipment_data_info set equipment_num=?,equipment_name=?,lon=?,lat=?,information=?,equipment_status=?,equipment_type=? where equipment_num='"+oldEquipmentNum+"' and rivers_num='"+rivers_num+"'");
        Object[] objects={equipment_num,equipment_name,lon,lat,information,equipmenr_status,equipment_type};
        int num=generalDao.updateSql(sql.toString(),objects);
        if(num>0){
            return "修改成功";
        }else{
            return "修改失败";
        }
    }

    @Override
    public String delEquitment(String equipment_num, String rivers_num) throws Exception {
        StringBuffer sql=new StringBuffer("delete from equipment_data_info where rivers_num='"+rivers_num+"'");
        if(equipment_num!=null&&!equipment_num.equals("")){
            sql.append(" and equipment_num='"+equipment_num+"'");
        }
       int num=generalDao.excuSql(sql.toString());
        if(num>0){
            return "删除成功";
        }else{
            return "删除失败";
        }
    }

}
