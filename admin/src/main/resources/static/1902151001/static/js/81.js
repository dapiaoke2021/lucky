webpackJsonp([81],{pFrA:function(e,t,m){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{memberVideoCommentThumbId:0,thumbMemberId:"",memberVideoCommentId:"",memberVideoId:"",state:"",createTime:""},dataRule:{thumbMemberId:[{required:!0,message:"点赞人不能为空",trigger:"blur"}],memberVideoCommentId:[{required:!0,message:"会员交友视频评论id不能为空",trigger:"blur"}],memberVideoId:[{required:!0,message:"会员交友视频id不能为空",trigger:"blur"}],state:[{required:!0,message:"状态不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.memberVideoCommentThumbId=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.memberVideoCommentThumbId&&t.$http({url:t.$http.adornUrl("/cellar/cellarmembervideocommentthumbdb/info/"+t.dataForm.memberVideoCommentThumbId),method:"get",params:t.$http.adornParams()}).then(function(e){var m=e.data;m&&0===m.code&&(t.dataForm.thumbMemberId=m.cellarmembervideocommentthumbdb.thumbMemberId,t.dataForm.memberVideoCommentId=m.cellarmembervideocommentthumbdb.memberVideoCommentId,t.dataForm.memberVideoId=m.cellarmembervideocommentthumbdb.memberVideoId,t.dataForm.state=m.cellarmembervideocommentthumbdb.state,t.dataForm.createTime=m.cellarmembervideocommentthumbdb.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/cellar/cellarmembervideocommentthumbdb/"+(e.dataForm.memberVideoCommentThumbId?"update":"save")),method:"post",data:e.$http.adornData({memberVideoCommentThumbId:e.dataForm.memberVideoCommentThumbId||void 0,thumbMemberId:e.dataForm.thumbMemberId,memberVideoCommentId:e.dataForm.memberVideoCommentId,memberVideoId:e.dataForm.memberVideoId,state:e.dataForm.state,createTime:e.dataForm.createTime})}).then(function(t){var m=t.data;m&&0===m.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(m.msg)})})}}},a={render:function(){var e=this,t=e.$createElement,m=e._self._c||t;return m("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[m("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[m("el-form-item",{attrs:{label:"点赞人",prop:"thumbMemberId"}},[m("el-input",{attrs:{placeholder:"点赞人"},model:{value:e.dataForm.thumbMemberId,callback:function(t){e.$set(e.dataForm,"thumbMemberId",t)},expression:"dataForm.thumbMemberId"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"会员交友视频评论id",prop:"memberVideoCommentId"}},[m("el-input",{attrs:{placeholder:"会员交友视频评论id"},model:{value:e.dataForm.memberVideoCommentId,callback:function(t){e.$set(e.dataForm,"memberVideoCommentId",t)},expression:"dataForm.memberVideoCommentId"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"会员交友视频id",prop:"memberVideoId"}},[m("el-input",{attrs:{placeholder:"会员交友视频id"},model:{value:e.dataForm.memberVideoId,callback:function(t){e.$set(e.dataForm,"memberVideoId",t)},expression:"dataForm.memberVideoId"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"状态",prop:"state"}},[m("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.state,callback:function(t){e.$set(e.dataForm,"state",t)},expression:"dataForm.state"}})],1),e._v(" "),m("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[m("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1)],1),e._v(" "),m("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[m("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),m("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=m("46Yf")(r,a,!1,null,null,null);t.default=o.exports}});