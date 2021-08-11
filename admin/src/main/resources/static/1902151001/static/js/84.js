webpackJsonp([84],{tB1y:function(e,r,a){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var t={data:function(){return{visible:!1,dataForm:{memberIntegralChangeRecordId:0,memberId:"",changeIntegral:"",beforeChange:"",afterIntegral:"",changeType:"",changeDesc:"",orderId:"",orderNo:"",state:"",createTime:""},dataRule:{memberId:[{required:!0,message:"会员id不能为空",trigger:"blur"}],changeIntegral:[{required:!0,message:"变动积分不能为空",trigger:"blur"}],beforeChange:[{required:!0,message:"变动前积分不能为空",trigger:"blur"}],afterIntegral:[{required:!0,message:"变动后积分不能为空",trigger:"blur"}],changeType:[{required:!0,message:"变动类型不能为空",trigger:"blur"}],changeDesc:[{required:!0,message:"变动描述不能为空",trigger:"blur"}],orderId:[{required:!0,message:"订单id不能为空",trigger:"blur"}],orderNo:[{required:!0,message:"订单编号不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(e){var r=this;this.dataForm.memberIntegralChangeRecordId=e||0,this.visible=!0,this.$nextTick(function(){r.$refs.dataForm.resetFields(),r.dataForm.memberIntegralChangeRecordId&&r.$http({url:r.$http.adornUrl("/cellar/cellarmemberintegralchangerecorddb/info/"+r.dataForm.memberIntegralChangeRecordId),method:"get",params:r.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(r.dataForm.memberId=a.cellarmemberintegralchangerecorddb.memberId,r.dataForm.changeIntegral=a.cellarmemberintegralchangerecorddb.changeIntegral,r.dataForm.beforeChange=a.cellarmemberintegralchangerecorddb.beforeChange,r.dataForm.afterIntegral=a.cellarmemberintegralchangerecorddb.afterIntegral,r.dataForm.changeType=a.cellarmemberintegralchangerecorddb.changeType,r.dataForm.changeDesc=a.cellarmemberintegralchangerecorddb.changeDesc,r.dataForm.orderId=a.cellarmemberintegralchangerecorddb.orderId,r.dataForm.orderNo=a.cellarmemberintegralchangerecorddb.orderNo,r.dataForm.state=a.cellarmemberintegralchangerecorddb.state,r.dataForm.createTime=a.cellarmemberintegralchangerecorddb.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(r){r&&e.$http({url:e.$http.adornUrl("/cellar/cellarmemberintegralchangerecorddb/"+(e.dataForm.memberIntegralChangeRecordId?"update":"save")),method:"post",data:e.$http.adornData({memberIntegralChangeRecordId:e.dataForm.memberIntegralChangeRecordId||void 0,memberId:e.dataForm.memberId,changeIntegral:e.dataForm.changeIntegral,beforeChange:e.dataForm.beforeChange,afterIntegral:e.dataForm.afterIntegral,changeType:e.dataForm.changeType,changeDesc:e.dataForm.changeDesc,orderId:e.dataForm.orderId,orderNo:e.dataForm.orderNo,state:e.dataForm.state,createTime:e.dataForm.createTime})}).then(function(r){var a=r.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},o={render:function(){var e=this,r=e.$createElement,a=e._self._c||r;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(r){e.visible=r}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(r){if(!("button"in r)&&e._k(r.keyCode,"enter",13,r.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"会员id",prop:"memberId"}},[a("el-input",{attrs:{placeholder:"会员id"},model:{value:e.dataForm.memberId,callback:function(r){e.$set(e.dataForm,"memberId",r)},expression:"dataForm.memberId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"变动积分",prop:"changeIntegral"}},[a("el-input",{attrs:{placeholder:"变动积分"},model:{value:e.dataForm.changeIntegral,callback:function(r){e.$set(e.dataForm,"changeIntegral",r)},expression:"dataForm.changeIntegral"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"变动前积分",prop:"beforeChange"}},[a("el-input",{attrs:{placeholder:"变动前积分"},model:{value:e.dataForm.beforeChange,callback:function(r){e.$set(e.dataForm,"beforeChange",r)},expression:"dataForm.beforeChange"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"变动后积分",prop:"afterIntegral"}},[a("el-input",{attrs:{placeholder:"变动后积分"},model:{value:e.dataForm.afterIntegral,callback:function(r){e.$set(e.dataForm,"afterIntegral",r)},expression:"dataForm.afterIntegral"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"变动类型",prop:"changeType"}},[a("el-input",{attrs:{placeholder:"变动类型"},model:{value:e.dataForm.changeType,callback:function(r){e.$set(e.dataForm,"changeType",r)},expression:"dataForm.changeType"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"变动描述",prop:"changeDesc"}},[a("el-input",{attrs:{placeholder:"变动描述"},model:{value:e.dataForm.changeDesc,callback:function(r){e.$set(e.dataForm,"changeDesc",r)},expression:"dataForm.changeDesc"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"订单id",prop:"orderId"}},[a("el-input",{attrs:{placeholder:"订单id"},model:{value:e.dataForm.orderId,callback:function(r){e.$set(e.dataForm,"orderId",r)},expression:"dataForm.orderId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"订单编号",prop:"orderNo"}},[a("el-input",{attrs:{placeholder:"订单编号"},model:{value:e.dataForm.orderNo,callback:function(r){e.$set(e.dataForm,"orderNo",r)},expression:"dataForm.orderNo"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"state"}},[a("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.state,callback:function(r){e.$set(e.dataForm,"state",r)},expression:"dataForm.state"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[a("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(r){e.$set(e.dataForm,"createTime",r)},expression:"dataForm.createTime"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(r){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(r){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=a("46Yf")(t,o,!1,null,null,null);r.default=l.exports}});