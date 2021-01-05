package zhwy.dao;

import com.alibaba.fastjson.JSONArray;
import java.util.List;
import java.util.Map;

public interface GeneralDao {

     JSONArray getDataBySql(String sql, String[] keys) throws Exception;
     int updateSql(String sql, Object[] objects) throws Exception;
     Map<String, Object> getDataMap(String sql) throws Exception;
     List<Map<String, Object>> getDataList(String sql) throws Exception;
     int[]  updateDate(String sql, List<Object[]> batchArgs) throws Exception;
     int  excuSql(String sql) throws Exception;
}
