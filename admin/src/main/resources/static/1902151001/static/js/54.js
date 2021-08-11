webpackJsonp([54,97],{"8+BB":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{addressId:0,memberId:"",contact:"",contactPhone:"",provinceId:"",provinceName:"",cityId:"",cityName:"",countyId:"",countyName:"",detailedAddress:"",createTime:"",state:"",idDefault:""},dataRule:{memberId:[{required:!0,message:"会员id不能为空",trigger:"blur"}],contact:[{required:!0,message:"联系人不能为空",trigger:"blur"}],contactPhone:[{required:!0,message:"联系电话不能为空",trigger:"blur"}],provinceId:[{required:!0,message:"省id不能为空",trigger:"blur"}],provinceName:[{required:!0,message:"省名称不能为空",trigger:"blur"}],cityId:[{required:!0,message:"市id不能为空",trigger:"blur"}],cityName:[{required:!0,message:"市名称不能为空",trigger:"blur"}],countyId:[{required:!0,message:"县id不能为空",trigger:"blur"}],countyName:[{required:!0,message:"县名称不能为空",trigger:"blur"}],detailedAddress:[{required:!0,message:"详细地址不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}],idDefault:[{required:!0,message:"是否默认 1 默认 0 非默认不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.addressId=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.addressId&&t.$http({url:t.$http.adornUrl("/cellar/cellarmemberaddressdb/info/"+t.dataForm.addressId),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.memberId=a.cellarmemberaddressdb.memberId,t.dataForm.contact=a.cellarmemberaddressdb.contact,t.dataForm.contactPhone=a.cellarmemberaddressdb.contactPhone,t.dataForm.provinceId=a.cellarmemberaddressdb.provinceId,t.dataForm.provinceName=a.cellarmemberaddressdb.provinceName,t.dataForm.cityId=a.cellarmemberaddressdb.cityId,t.dataForm.cityName=a.cellarmemberaddressdb.cityName,t.dataForm.countyId=a.cellarmemberaddressdb.countyId,t.dataForm.countyName=a.cellarmemberaddressdb.countyName,t.dataForm.detailedAddress=a.cellarmemberaddressdb.detailedAddress,t.dataForm.createTime=a.cellarmemberaddressdb.createTime,t.dataForm.state=a.cellarmemberaddressdb.state,t.dataForm.idDefault=a.cellarmemberaddressdb.idDefault)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/cellar/cellarmemberaddressdb/"+(e.dataForm.addressId?"update":"save")),method:"post",data:e.$http.adornData({addressId:e.dataForm.addressId||void 0,memberId:e.dataForm.memberId,contact:e.dataForm.contact,contactPhone:e.dataForm.contactPhone,provinceId:e.dataForm.provinceId,provinceName:e.dataForm.provinceName,cityId:e.dataForm.cityId,cityName:e.dataForm.cityName,countyId:e.dataForm.countyId,countyName:e.dataForm.countyName,detailedAddress:e.dataForm.detailedAddress,createTime:e.dataForm.createTime,state:e.dataForm.state,idDefault:e.dataForm.idDefault})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},d={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"会员id",prop:"memberId"}},[a("el-input",{attrs:{placeholder:"会员id"},model:{value:e.dataForm.memberId,callback:function(t){e.$set(e.dataForm,"memberId",t)},expression:"dataForm.memberId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"联系人",prop:"contact"}},[a("el-input",{attrs:{placeholder:"联系人"},model:{value:e.dataForm.contact,callback:function(t){e.$set(e.dataForm,"contact",t)},expression:"dataForm.contact"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"联系电话",prop:"contactPhone"}},[a("el-input",{attrs:{placeholder:"联系电话"},model:{value:e.dataForm.contactPhone,callback:function(t){e.$set(e.dataForm,"contactPhone",t)},expression:"dataForm.contactPhone"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"省id",prop:"provinceId"}},[a("el-input",{attrs:{placeholder:"省id"},model:{value:e.dataForm.provinceId,callback:function(t){e.$set(e.dataForm,"provinceId",t)},expression:"dataForm.provinceId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"省名称",prop:"provinceName"}},[a("el-input",{attrs:{placeholder:"省名称"},model:{value:e.dataForm.provinceName,callback:function(t){e.$set(e.dataForm,"provinceName",t)},expression:"dataForm.provinceName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"市id",prop:"cityId"}},[a("el-input",{attrs:{placeholder:"市id"},model:{value:e.dataForm.cityId,callback:function(t){e.$set(e.dataForm,"cityId",t)},expression:"dataForm.cityId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"市名称",prop:"cityName"}},[a("el-input",{attrs:{placeholder:"市名称"},model:{value:e.dataForm.cityName,callback:function(t){e.$set(e.dataForm,"cityName",t)},expression:"dataForm.cityName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"县id",prop:"countyId"}},[a("el-input",{attrs:{placeholder:"县id"},model:{value:e.dataForm.countyId,callback:function(t){e.$set(e.dataForm,"countyId",t)},expression:"dataForm.countyId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"县名称",prop:"countyName"}},[a("el-input",{attrs:{placeholder:"县名称"},model:{value:e.dataForm.countyName,callback:function(t){e.$set(e.dataForm,"countyName",t)},expression:"dataForm.countyName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"详细地址",prop:"detailedAddress"}},[a("el-input",{attrs:{placeholder:"详细地址"},model:{value:e.dataForm.detailedAddress,callback:function(t){e.$set(e.dataForm,"detailedAddress",t)},expression:"dataForm.detailedAddress"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[a("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"state"}},[a("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.state,callback:function(t){e.$set(e.dataForm,"state",t)},expression:"dataForm.state"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"是否默认 1 默认 0 非默认",prop:"idDefault"}},[a("el-input",{attrs:{placeholder:"是否默认 1 默认 0 非默认"},model:{value:e.dataForm.idDefault,callback:function(t){e.$set(e.dataForm,"idDefault",t)},expression:"dataForm.idDefault"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=a("46Yf")(r,d,!1,null,null,null);t.default=l.exports},s1rC:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("8+BB").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/cellar/cellarmemberaddressdb/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},deleteHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.addressId});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/cellar/cellarmemberaddressdb/delete"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})}}},d={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:e.dataForm.key,callback:function(t){e.$set(e.dataForm,"key",t)},expression:"dataForm.key"}})],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("cellar:cellarmemberaddressdb:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addOrUpdateHandle()}}},[e._v("新增")]):e._e(),e._v(" "),e.isAuth("cellar:cellarmemberaddressdb:delete")?a("el-button",{attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.deleteHandle()}}},[e._v("批量删除")]):e._e()],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),a("el-table-column",{attrs:{prop:"addressId","header-align":"center",align:"center",label:"地址id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"memberId","header-align":"center",align:"center",label:"会员id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"contact","header-align":"center",align:"center",label:"联系人"}}),e._v(" "),a("el-table-column",{attrs:{prop:"contactPhone","header-align":"center",align:"center",label:"联系电话"}}),e._v(" "),a("el-table-column",{attrs:{prop:"provinceId","header-align":"center",align:"center",label:"省id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"provinceName","header-align":"center",align:"center",label:"省名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cityId","header-align":"center",align:"center",label:"市id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cityName","header-align":"center",align:"center",label:"市名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"countyId","header-align":"center",align:"center",label:"县id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"countyName","header-align":"center",align:"center",label:"县名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"detailedAddress","header-align":"center",align:"center",label:"详细地址"}}),e._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),e._v(" "),a("el-table-column",{attrs:{prop:"state","header-align":"center",align:"center",label:"状态"}}),e._v(" "),a("el-table-column",{attrs:{prop:"idDefault","header-align":"center",align:"center",label:"是否默认 1 默认 0 非默认"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.addOrUpdateHandle(t.row.addressId)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteHandle(t.row.addressId)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},l=a("46Yf")(r,d,!1,null,null,null);t.default=l.exports}});