package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * entity公共类
 *
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2019-01-16 17:34:20
 */
@ApiModel("entity公共类")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Data
@Accessors(chain = true)
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@TableField(exist = false)
	@ApiModelProperty(required=false,value="搜索关键字",hidden = true)
	private String key;
	@TableField(exist = false)
	@ApiModelProperty(required=false,value="当前页数",hidden = true)
	private Integer page;
	@TableField(exist = false)
	@ApiModelProperty(required=false,value="每页条数",hidden = true)
	private Integer limit;
	@TableField(exist = false)
	@ApiModelProperty(required=false,value="暂时无用",hidden = true)
	private String sidx;
	@TableField(exist = false)
	@ApiModelProperty(required=false,value="暂时无用",hidden = true)
	private String order;
}
