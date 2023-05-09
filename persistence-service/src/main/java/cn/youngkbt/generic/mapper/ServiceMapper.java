package cn.youngkbt.generic.mapper;

import cn.youngkbt.generic.common.dto.service.ServiceResultDTO;
import cn.youngkbt.generic.common.dto.serviceCol.ServiceColResultDTO;
import cn.youngkbt.generic.common.model.GenericService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:23
 * @note 1.0
 */
@Mapper
public interface ServiceMapper extends BaseMapper<GenericService> {
    /**
     * 查询项目包含的接口和接口包含的字段
     * @param projectId 项目 id
     * @return 项目包含的接口和接口包含的字段
     */
    @Select("select distinct s.id as service_id, s.service_name from generic.service s, generic.service_col sc where project_id = #{projectId} and s.id = sc.service_id")
    @Results(id = "serviceResultMap", value = {
            @Result(property = "serviceId", column = "service_id"),
            @Result(property = "serviceName", column = "service_name"),
            @Result(property = "serviceColList", column = "service_id",
                    many = @Many(select = "cn.youngkbt.generic.mapper.ServiceMapper.queryServiceColList"))
    })
    List<ServiceResultDTO> queryServiceAndServiceColList(Integer projectId);

    /**
     * 通过接口 id 查询接口包含的字段集合
     * @param serviceId 接口 id 
     * @return 接口包含的字段集合
     */
    @Select("select id as serviceColId, json_col as serviceColJsonName from generic.service_col where service_id = #{serviceId}")
    List<ServiceColResultDTO> queryServiceColList(@Param("serviceId") Integer serviceId);

    /**
     * 通过项目密钥和接口 url 查询接口信息
     * @param secretKey 项目密钥
     * @param serviceUrl 接口 url
     * @return 接口信息
     */
    @Select("select s.is_auth from generic.project p, generic.service s where p.secret_key = #{secretKey} and s.service_url = #{serviceUrl} and p.id = s.project_id")
    GenericService queryServiceBySecretKeyAndUrl(@Param("secretKey") String secretKey, @Param("serviceUrl") String serviceUrl);
}