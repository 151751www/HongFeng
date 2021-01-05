package zhwy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.GeneralDao;
import zhwy.service.RiversService;
import zhwy.util.Common;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Resource
public class RiversServiceImpl implements RiversService {

    @Autowired
    GeneralDao generalDao;
    @Autowired
    Common common;

    @Override
    public JSONArray getRiversInfo(String rivers_name)throws Exception {
        StringBuffer sql=new StringBuffer("select rivers_num as 河道编号, rivers_name as 河道名称,'['+track_point+']' as 经纬度,max_val as 阈值 from rivers_data_info  where 1=1 ");
        if(rivers_name!=null&&!rivers_name.equals("")){
            sql.append(" and rivers_name='");
            sql.append(rivers_name);
            sql.append("'");
        }
        sql.append(" order by rivers_num asc");
        String key[]={"河道编号","河道名称","经纬度","阈值"};
        JSONArray array=generalDao.getDataBySql(sql.toString(),key);
        return array;
    }

    @Override
    public String addRiversInfo(String rivers_Info)throws Exception {
        StringBuffer sql=new StringBuffer("insert into rivers_data_info (rivers_name,track_point,max_val) VALUES(?,?,?)");
        JSONArray array=JSONArray.parseArray(rivers_Info);
        List<Object[]>list=new ArrayList<>();
        Object[] object;
        for (int i=0;i<array.size();i++){
            JSONObject jsonObject=array.getJSONObject(i);
            JSONArray river=getRiversInfo(common.StrNull(jsonObject.getString("河道名称")));
            if(river.size()>0){
                return "新增河道失败，"+common.StrNull(jsonObject.getString("河道名称"))+"已存在";
            }
            object=new Object[3];
            object[0]=common.StrNull(jsonObject.getString("河道名称"));
            object[1]=common.StrNull(jsonObject.getString("流经轨迹"));
            object[2]=common.StrNull(jsonObject.getString("阈值"));
            list.add(object);
        }
        int [] num=generalDao.updateDate(sql.toString(),list);
        if(num.length==array.size()){
            return "新增河道成功";
        }
        return "新增河道失败";
    }

    @Override
    public String updateRiverInfo(String rivers_num, String rivers_aftername, String rivers_lng, String max_val) throws Exception{

        StringBuffer sql=new StringBuffer("update rivers_data_info set rivers_num='"+rivers_num+"'");
        if(rivers_aftername!=null&&!rivers_aftername.equals("")){
            sql.append(" ,rivers_name='"+rivers_aftername+"'");
        }
        if(rivers_lng!=null&&!rivers_lng.equals("")){
            rivers_lng=rivers_lng.replace("[","").replace("]","");
            sql.append(" ,track_point='"+rivers_lng+"'");
        }
        if(max_val!=null&&!max_val.equals("")){
            sql.append(" ,max_val='"+max_val+"'");
        }
        sql.append(" where  rivers_num="+rivers_num);

        int num=generalDao.excuSql(sql.toString());
        if(num>0){
            return "修改河道成功";
        }else{
            return "修改河道失败";
        }
    }

    @Override
    public JSONArray getRiversAndbollInfo(String rivers_num) throws Exception{
        StringBuffer sql=new StringBuffer("select boll_num as 投放点编号, rivers_num as 河道编号, rivers_name as 河道名称,'['+track_point+']' as 流经轨迹,boll_name  as 投放点,'{'+CAST(lon as VARCHAR)+','+CAST(lat as VARCHAR)+'}'  as 经纬度 from rivers_data_info river " +
                " LEFT JOIN boll_data_info boll on river.rivers_num=boll.rivers_num  where 1=1 ");
        if(rivers_num!=null&&!rivers_num.equals("")){
            sql.append(" and rivers_num='");
            sql.append(rivers_num);
            sql.append("'");
        }
        sql.append(" order by rivers_num asc");
        String key[]={"投放点编号","河道编号","河道名称","流经轨迹","投放点","经纬度"};
        JSONArray array=generalDao.getDataBySql(sql.toString(),key);
        return array;
    }

    @Override
    public String delRivers(String rivers_num) throws Exception {
        StringBuffer sql=new StringBuffer("delete from rivers_data_info where rivers_num='");
        sql.append(rivers_num);
        sql.append("'");
        int num=generalDao.excuSql(sql.toString());
        if(num!=-1){
            return "删除成功";
        }else{
            return "删除失败";
        }
    }

}
