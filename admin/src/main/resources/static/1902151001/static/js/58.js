webpackJsonp([58,103],{rSfd:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{feedbackId:0,memberId:"",state:"",createTime:"",feedbackContent:""},dataRule:{memberId:[{required:!0,message:"会员id不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],feedbackContent:[{required:!0,message:"反馈内容不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.feedbackId=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.feedbackId&&t.$http({url:t.$http.adornUrl("/cellar/cellarfeedbackdb/info/"+t.dataForm.feedbackId),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.memberId=a.cellarfeedbackdb.memberId,t.dataForm.state=a.cellarfeedbackdb.state,t.dataForm.createTime=a.cellarfeedbackdb.createTime,t.dataForm.feedbackContent=a.cellarfeedbackdb.feedbackContent)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/cellar/cellarfeedbackdb/"+(e.dataForm.feedbackId?"update":"save")),method:"post",data:e.$http.adornData({feedbackId:e.dataForm.feedbackId||void 0,memberId:e.dataForm.memberId,state:e.dataForm.state,createTime:e.dataForm.createTime,feedbackContent:e.dataForm.feedbackContent})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},n={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"会员id",prop:"memberId"}},[a("el-input",{attrs:{placeholder:"会员id"},model:{value:e.dataForm.memberId,callback:function(t){e.$set(e.dataForm,"memberId",t)},expression:"dataForm.memberId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"state"}},[a("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.state,callback:function(t){e.$set(e.dataForm,"state",t)},expression:"dataForm.state"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[a("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"反馈内容",prop:"feedbackContent"}},[a("el-input",{attrs:{placeholder:"反馈内容"},model:{value:e.dataForm.feedbackContent,callback:function(t){e.$set(e.dataForm,"feedbackContent",t)},expression:"dataForm.feedbackContent"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=a("46Yf")(r,n,!1,null,null,null);t.default=l.exports},v0ea:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("rSfd").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/cellar/cellarfeedbackdb/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},deleteHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.feedbackId});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/cellar/cellarfeedbackdb/delete"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})}}},n={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:e.dataForm.key,callback:function(t){e.$set(e.dataForm,"key",t)},expression:"dataForm.key"}})],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("cellar:cellarfeedbackdb:delete")?a("el-button",{attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.deleteHandle()}}},[e._v("批量删除")]):e._e()],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),a("el-table-column",{attrs:{prop:"feedbackId","header-align":"center",align:"center",label:"意见反馈id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"memberId","header-align":"center",align:"center",label:"会员id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cellarMemberDbEntity.nickname","header-align":"center",align:"center",label:"会员名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"stateStr","header-align":"center",align:"center",label:"状态"}}),e._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),e._v(" "),a("el-table-column",{attrs:{prop:"feedbackContent","header-align":"center",align:"center",label:"反馈内容"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteHandle(t.row.feedbackId)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},l=a("46Yf")(r,n,!1,null,null,null);t.default=l.exports}});