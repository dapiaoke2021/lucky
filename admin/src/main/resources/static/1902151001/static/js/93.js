webpackJsonp([93],{V9TS:function(e,r,a){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var t={data:function(){return{visible:!1,dataForm:{memberBargainingRecordId:0,memberBargainingInformationId:"",memberId:"",cutPrice:"",beforeCutPrice:"",afterCutPrice:"",createTime:"",state:""},dataRule:{memberBargainingInformationId:[{required:!0,message:"会员砍价信息id不能为空",trigger:"blur"}],memberId:[{required:!0,message:"会员id不能为空",trigger:"blur"}],cutPrice:[{required:!0,message:"砍价金额不能为空",trigger:"blur"}],beforeCutPrice:[{required:!0,message:"砍价之前金额不能为空",trigger:"blur"}],afterCutPrice:[{required:!0,message:"砍价之后金额不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}]}}},methods:{init:function(e){var r=this;this.dataForm.memberBargainingRecordId=e||0,this.visible=!0,this.$nextTick(function(){r.$refs.dataForm.resetFields(),r.dataForm.memberBargainingRecordId&&r.$http({url:r.$http.adornUrl("/cellar/cellarmemberbargainingrecorddb/info/"+r.dataForm.memberBargainingRecordId),method:"get",params:r.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(r.dataForm.memberBargainingInformationId=a.cellarmemberbargainingrecorddb.memberBargainingInformationId,r.dataForm.memberId=a.cellarmemberbargainingrecorddb.memberId,r.dataForm.cutPrice=a.cellarmemberbargainingrecorddb.cutPrice,r.dataForm.beforeCutPrice=a.cellarmemberbargainingrecorddb.beforeCutPrice,r.dataForm.afterCutPrice=a.cellarmemberbargainingrecorddb.afterCutPrice,r.dataForm.createTime=a.cellarmemberbargainingrecorddb.createTime,r.dataForm.state=a.cellarmemberbargainingrecorddb.state)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(r){r&&e.$http({url:e.$http.adornUrl("/cellar/cellarmemberbargainingrecorddb/"+(e.dataForm.memberBargainingRecordId?"update":"save")),method:"post",data:e.$http.adornData({memberBargainingRecordId:e.dataForm.memberBargainingRecordId||void 0,memberBargainingInformationId:e.dataForm.memberBargainingInformationId,memberId:e.dataForm.memberId,cutPrice:e.dataForm.cutPrice,beforeCutPrice:e.dataForm.beforeCutPrice,afterCutPrice:e.dataForm.afterCutPrice,createTime:e.dataForm.createTime,state:e.dataForm.state})}).then(function(r){var a=r.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},i={render:function(){var e=this,r=e.$createElement,a=e._self._c||r;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(r){e.visible=r}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(r){if(!("button"in r)&&e._k(r.keyCode,"enter",13,r.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"会员砍价信息id",prop:"memberBargainingInformationId"}},[a("el-input",{attrs:{placeholder:"会员砍价信息id"},model:{value:e.dataForm.memberBargainingInformationId,callback:function(r){e.$set(e.dataForm,"memberBargainingInformationId",r)},expression:"dataForm.memberBargainingInformationId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"会员id",prop:"memberId"}},[a("el-input",{attrs:{placeholder:"会员id"},model:{value:e.dataForm.memberId,callback:function(r){e.$set(e.dataForm,"memberId",r)},expression:"dataForm.memberId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"砍价金额",prop:"cutPrice"}},[a("el-input",{attrs:{placeholder:"砍价金额"},model:{value:e.dataForm.cutPrice,callback:function(r){e.$set(e.dataForm,"cutPrice",r)},expression:"dataForm.cutPrice"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"砍价之前金额",prop:"beforeCutPrice"}},[a("el-input",{attrs:{placeholder:"砍价之前金额"},model:{value:e.dataForm.beforeCutPrice,callback:function(r){e.$set(e.dataForm,"beforeCutPrice",r)},expression:"dataForm.beforeCutPrice"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"砍价之后金额",prop:"afterCutPrice"}},[a("el-input",{attrs:{placeholder:"砍价之后金额"},model:{value:e.dataForm.afterCutPrice,callback:function(r){e.$set(e.dataForm,"afterCutPrice",r)},expression:"dataForm.afterCutPrice"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[a("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(r){e.$set(e.dataForm,"createTime",r)},expression:"dataForm.createTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"state"}},[a("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.state,callback:function(r){e.$set(e.dataForm,"state",r)},expression:"dataForm.state"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(r){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(r){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=a("46Yf")(t,i,!1,null,null,null);r.default=o.exports}});