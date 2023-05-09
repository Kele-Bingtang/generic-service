package cn.youngkbt.generic.mapper;

import cn.youngkbt.generic.common.model.ColumnInfo;
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
    /**
     * 查询操作
     * @param sql 查询 sql
     * @return 查询出的数据
     */
    @Select("${sql}")
    public List<LinkedHashMap<String, Object>> genericSelect(@Param("sql") String sql);

    /**
     * 增删改操作
     * @param sql 增删改 sql
     * @return 成功与否
     */
    @Insert("${sql}")
    public Integer genericOperate(@Param("sql") String sql);

    /**
     * 查询所有数据库名
     * @param sql
     * @return
     */
    @Select("${sql}")
    public List<String> selectDatabaseInfo(@Param("sql") String sql);

    /**
     * 查询表的字段名、类型等相关信息
     * @param database 数据库名
     * @param table 表
     * @return 表的字段名、类型等相关信息
     */
    @Select("SELECT COLUMN_NAME as columnName, COLUMN_TYPE as columnType, DATA_TYPE as dataType, CHARACTER_MAXIMUM_LENGTH as length, IS_NULLABLE as isNull, COLUMN_DEFAULT, COLUMN_COMMENT as comment" +
            "FROM INFORMATION_SCHEMA.COLUMNS where table_schema = #{database}} and table_name  = #{table}")
    public List<ColumnInfo> queryColumnInfoList(String database, String table);
}
