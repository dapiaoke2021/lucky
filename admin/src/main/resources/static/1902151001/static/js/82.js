webpackJsonp([82],{vCrV:function(e,t,m){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{memberVideoCommentId:0,commentMemberId:"",commentContent:"",memberVideoId:"",state:"",createTime:"",commentThumbNumber:""},dataRule:{commentMemberId:[{required:!0,message:"评论人不能为空",trigger:"blur"}],commentContent:[{required:!0,message:"评论内容不能为空",trigger:"blur"}],memberVideoId:[{required:!0,message:"会员交友视频id不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],commentThumbNumber:[{required:!0,message:"点赞数量不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.memberVideoCommentId=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.memberVideoCommentId&&t.$http({url:t.$http.adornUrl("/cellar/cellarmembervideocommentdb/info/"+t.dataForm.memberVideoCommentId),method:"get",params:t.$http.adornParams()}).then(function(e){var m=e.data;m&&0===m.code&&(t.dataForm.commentMemberId=m.cellarmembervideocommentdb.commentMemberId,t.dataForm.commentContent=m.cellarmembervideocommentdb.commentContent,t.dataForm.memberVideoId=m.cellarmembervideocommentdb.memberVideoId,t.dataForm.state=m.cellarmembervideocommentdb.state,t.dataForm.createTime=m.cellarmembervideocommentdb.createTime,t.dataForm.commentThumbNumber=m.cellarmembervideocommentdb.commentThumbNumber)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/cellar/cellarmembervideocommentdb/"+(e.dataForm.memberVideoCommentId?"update":"save")),method:"post",data:e.$http.adornData({memberVideoCommentId:e.dataForm.memberVideoCommentId||void 0,commentMemberId:e.dataForm.commentMemberId,commentContent:e.dataForm.commentContent,memberVideoId:e.dataForm.memberVideoId,state:e.dataForm.state,createTime:e.dataForm.createTime,commentThumbNumber:e.dataForm.commentThumbNumber})}).then(function(t){var m=t.data;m&&0===m.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(m.msg)})})}}},a={render:function(){var e=this,t=e.$createElement,m=e._self._c||t;return m("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[m("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[m("el-form-item",{attrs:{label:"评论人",prop:"commentMemberId"}},[m("el-input",{attrs:{placeholder:"评论人"},model:{value:e.dataForm.commentMemberId,callback:function(t){e.$set(e.dataForm,"commentMemberId",t)},expression:"dataForm.commentMemberId"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"评论内容",prop:"commentContent"}},[m("el-input",{attrs:{placeholder:"评论内容"},model:{value:e.dataForm.commentContent,callback:function(t){e.$set(e.dataForm,"commentContent",t)},expression:"dataForm.commentContent"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"会员交友视频id",prop:"memberVideoId"}},[m("el-input",{attrs:{placeholder:"会员交友视频id"},model:{value:e.dataForm.memberVideoId,callback:function(t){e.$set(e.dataForm,"memberVideoId",t)},expression:"dataForm.memberVideoId"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"状态",prop:"state"}},[m("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.state,callback:function(t){e.$set(e.dataForm,"state",t)},expression:"dataForm.state"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[m("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"点赞数量",prop:"commentThumbNumber"}},[m("el-input",{attrs:{placeholder:"点赞数量"},model:{value:e.dataForm.commentThumbNumber,callback:function(t){e.$set(e.dataForm,"commentThumbNumber",t)},expression:"dataForm.commentThumbNumber"}})],1)],1),e._v(" "),m("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[m("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),m("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=m("46Yf")(r,a,!1,null,null,null);t.default=o.exports}});