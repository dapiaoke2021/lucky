webpackJsonp([73],{KTrn:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={components:{ueditor:r("t1fE").default},data:function(){return{ruletypelist:[],visible:!1,dataForm:{ruleId:0,ruleType:"",ruleDescription:"",ruleDescriptionStr:"",createTime:"",state:""},dataRule:{ruleType:[{required:!0,message:"规则类型不能为空",trigger:"blur"}],ruleDescription:[{required:!0,message:"规则描述不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}]}}},methods:{ruleTypeList:function(){var e=this;this.$http({url:this.$http.adornUrl("/sys/constants/ruletypelist"),method:"get",params:this.$http.adornParams()}).then(function(t){var r=t.data;r&&0===r.code?e.ruletypelist=r.data:e.$message.error(r.msg)})},init:function(e){var t=this;this.ruleTypeList(),this.dataForm.ruleId=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.ruleId?t.$http({url:t.$http.adornUrl("/cellar/cellarruledb/info/"+t.dataForm.ruleId),method:"get",params:t.$http.adornParams()}).then(function(e){var r=e.data;r&&0===r.code&&(t.dataForm.ruleType=r.cellarRuleDb.ruleType,t.dataForm.ruleDescriptionStr=r.cellarRuleDb.ruleDescriptionStr,t.$refs.ueditor.init(t.dataForm.ruleDescriptionStr),t.dataForm.createTime=r.cellarRuleDb.createTime,t.dataForm.state=r.cellarRuleDb.state)}):t.$refs.ueditor.init("")})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/cellar/cellarruledb/"+(e.dataForm.ruleId?"update":"save")),method:"post",data:e.$http.adornData({ruleId:e.dataForm.ruleId||void 0,ruleType:e.dataForm.ruleType,ruleDescription:e.dataForm.ruleDescription,ruleDescriptionStr:e.$refs.ueditor.getContent(),createTime:e.dataForm.createTime,state:e.dataForm.state})}).then(function(t){var r=t.data;r&&0===r.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(r.msg)})})}}},l={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[r("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"规则描述",prop:"ruleType"}},[r("el-select",{attrs:{clearable:"",placeholder:"规则类型"},model:{value:e.dataForm.ruleType,callback:function(t){e.$set(e.dataForm,"ruleType",t)},expression:"dataForm.ruleType"}},e._l(e.ruletypelist,function(e){return r("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"规则描述",prop:"ruleDescriptionStr"}},[r("ueditor",{ref:"ueditor"})],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},i=r("46Yf")(a,l,!1,null,null,null);t.default=i.exports}});