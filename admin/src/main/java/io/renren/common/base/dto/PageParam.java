package io.renren.common.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页参数类
 *
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2019-01-16 17:34:20
 */
@ApiModel("分页参数类")
@Data
@Accessors(chain = true)
public abstract class PageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(required=false,value="当前页数",hidden = true)
	private Integer page;

	@ApiModelProperty(required=false,value="每页条数",hidden = true)
	private Integer limit;
}
