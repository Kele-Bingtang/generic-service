package cn.youngkbt.generic.base.mapper;

import cn.youngkbt.generic.base.model.ColumnInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 23:27
 * @note
 */
@Mapper
public interface ApiMapper {
    
    @Select("${sql}")
    public List<LinkedHashMap<String, Object>> genericSelect(@Param("sql") String sql);

    @Insert("${sql}")
    public Integer genericOperate(@Param("sql") String sql);

    @Select("${sql}")
    public List<String> selectDatabaseInfo(@Param("sql") String sql);

    @Select("SELECT COLUMN_NAME as columnName, COLUMN_TYPE as columnType, DATA_TYPE as dataType, CHARACTER_MAXIMUM_LENGTH as length, IS_NULLABLE as isNull, COLUMN_DEFAULT, COLUMN_COMMENT as comment" +
            "FROM INFORMATION_SCHEMA.COLUMNS where table_schema = #{database}} and table_name  = #{table}")
    public List<ColumnInfo> queryColumnInfoList(String database, String table);
}
