package io.renren.common.base.conver;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2020-04-19 16:59
 */
@Mapper
public interface IPageConver {


    IPageConver MAPPER =  Mappers.getMapper(IPageConver.class);


    Page conver(IPage iPage);
}
