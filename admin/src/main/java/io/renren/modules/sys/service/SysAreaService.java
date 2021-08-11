package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.SysAreaEntity;

import java.util.List;

/**
 * 地域信息表
 *
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2019-02-22 15:12:55
 */
public interface SysAreaService extends IService<SysAreaEntity> {

    PageUtils queryPage(SysAreaEntity sysArea);

    List<SysAreaEntity> listThreeLevel();
}

