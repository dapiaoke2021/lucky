package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.datasources.annotation.Admin;
import io.renren.modules.sys.entity.SysAreaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地域信息表
 *
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2019-02-22 15:12:55
 */
@Mapper
@Admin
public interface SysAreaDao extends BaseMapper<SysAreaEntity> {

}
