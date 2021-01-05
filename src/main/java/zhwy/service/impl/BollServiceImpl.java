package zhwy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.GeneralDao;
import zhwy.service.BollService;
import zhwy.service.RiversService;
import zhwy.util.Common;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class BollServiceImpl implements BollService {

    @Autowired
    GeneralDao generalDao;
    @Autowired
    Common common;
    @Autowired
    RiversService riversService;



    @Override
    public JSONArray getBollInfo(String riversnum,String bollName)throws Exception {
        StringBuffer sql=new StringBuffer("select rivers_name as 河道名称,boll_name  as 投放点,'{'+CAST(lon as VARCHAR)+','+CAST(lat as VARCHAR)+'}'  as 经纬度,boll_staus as 是否投放 from boll_data_info where 1=1 ");
        if(riversnum!=null&&!riversnum.equals("")){
            sql.append(" and  rivers_num='");
            sql.append(riversnum);
            sql.append("'");
        }
        if(bollName!=null&&!bollName.equals("")){
            sql.append(" and  boll_name='");
            sql.append(bollName);
            sql.append("'");
        }
        sql.append(" order by rivers_name asc,lon asc");
        String key[]={"河道名称","投放点","经纬度","是否投放"};
        JSONArray array=generalDao.getDataBySql(sql.toString(),key);
        return array;
    }


    @Override
    public String addBollToRivers(String array) throws Exception {
        String sql=" insert into boll_data_info (rivers_name,boll_name,lon,lat,boll_staus) VALUES(?,?,?,?,?)";
        String result="新增失败";
        List<Object[]>list=new ArrayList<>();
        Object[] bollInfo;
        List<Map<String, Object>> bolllist=common.getList(array,new String[]{"河道名称","投放点","经纬度"});
        for (int i=0;i<bolllist.size();i++){
            bollInfo=new Object[5];
            JSONArray river=riversService.getRiversInfo(bolllist.get(i).get("河道名称").toString());
            JSONObject riverobject=river.getJSONObject(0);
            bollInfo[0]=riverobject.get("河道编号");
            bollInfo[1]=bolllist.get(i).get("投放点");
            JSONArray bollinfo=getBollInfo(bolllist.get(i).get("河道名称").toString(),bolllist.get(i).get("投放点").toString());
            if(bollinfo.size()>0){
                result="新增失败，"+bolllist.get(i).get("河道名称")+","+bolllist.get(i).get("投放点")+"已存在。";
                return result;
            }
            String lonlat=bolllist.get(i).get("经纬度").toString().replace("{","").replace("}","");
            String[]latlon=lonlat.split(",");
            if(latlon.length==2){
                bollInfo[2]=latlon[0];
                bollInfo[3]=latlon[1];
                bollInfo[4]="0";
                list.add(bollInfo);
            }else{
                result="新增失败，"+bolllist.get(i).get("投放点")+"填写经纬度错误";
                return result;
            }
        }
        int[] num=generalDao.updateDate(sql,list);
        if(num.length==list.size()){
            result="新增成功";
        }
        return  result;
    }

    @Override
    public String updateBollToRivers(String boll_num, String boll_name, String lot) throws Exception {
        lot=lot.replace("{","").replace("}","");
        String [] lonAndlat=lot.split(",");
        if(lonAndlat.length==2){
            StringBuffer sql=new StringBuffer("update boll_data_info set boll_num='"+boll_num+"'");
            if(boll_name!=null&&!boll_name.equals("")){
                sql.append(" ,boll_name='");
                sql.append( boll_name);
                sql.append("' , lon='");
                sql.append(lonAndlat[0]);
                sql.append("',lat='");
                sql.append(lonAndlat[1]);
                sql.append("' where boll_num='");
                sql.append(boll_num);
                sql.append("'");
            }
            int num=generalDao.excuSql(sql.toString());
            if(num>0){
                return "修改投放点信息成功";
            }
        }else{
            return "修改失败，经纬度信息填写错误";
        }
        return null;
    }

    @Override
    public String delBollToRivers(String boll_num,String rivers_num) throws Exception {
        StringBuffer sql=new StringBuffer("delete from boll_data_info where rivers_num='");
        sql.append("delete from boll_data_info where rivers_num='");
        sql.append(rivers_num);
        sql.append("'");
        if(boll_num!=null&&!boll_num.equals("")){
            sql.append(" and boll_num='");
            sql.append(boll_num);
            sql.append("'");
        }
        int num=generalDao.excuSql(sql.toString());
        if(num!=-1){
            return "删除成功";
        }else{
            return "删除失败";
        }
    }
}
