<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="接收者（用户）的 openid" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="接收者（用户）的 openid"></el-input>
    </el-form-item>
    <el-form-item label="form_id" prop="formId">
      <el-input v-model="dataForm.formId" placeholder="form_id"></el-input>
    </el-form-item>
    <el-form-item label="模板消息的id" prop="templateId">
      <el-input v-model="dataForm.templateId" placeholder="模板消息的id"></el-input>
    </el-form-item>
    <el-form-item label="小程序路径" prop="page">
      <el-input v-model="dataForm.page" placeholder="小程序路径"></el-input>
    </el-form-item>
    <el-form-item label="form_id类型 1-客户端登录 2-开锁 3-支付 4-运维端登录" prop="formIdType">
      <el-input v-model="dataForm.formIdType" placeholder="form_id类型 1-客户端登录 2-开锁 3-支付 4-运维端登录"></el-input>
    </el-form-item>
    <el-form-item label="推送状态 0-未使用 1-待推送（刚加入队列） 2-已推送 3-推送失败 4-已过期" prop="status">
      <el-input v-model="dataForm.status" placeholder="推送状态 0-未使用 1-待推送（刚加入队列） 2-已推送 3-推送失败 4-已过期"></el-input>
    </el-form-item>
    <el-form-item label="点击状态 0-未点击 1-已点击" prop="isClick">
      <el-input v-model="dataForm.isClick" placeholder="点击状态 0-未点击 1-已点击"></el-input>
    </el-form-item>
    <el-form-item label="关键词数据" prop="keyword">
      <el-input v-model="dataForm.keyword" placeholder="关键词数据"></el-input>
    </el-form-item>
    <el-form-item label="错误信息" prop="failedInfo">
      <el-input v-model="dataForm.failedInfo" placeholder="错误信息"></el-input>
    </el-form-item>
    <el-form-item label="失败次数" prop="failedNum">
      <el-input v-model="dataForm.failedNum" placeholder="失败次数"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="pushAt">
      <el-input v-model="dataForm.pushAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="clickAt">
      <el-input v-model="dataForm.clickAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="failedAt">
      <el-input v-model="dataForm.failedAt" placeholder=""></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          openid: '',
          formId: '',
          templateId: '',
          page: '',
          formIdType: '',
          status: '',
          isClick: '',
          keyword: '',
          failedInfo: '',
          failedNum: '',
          createdAt: '',
          updatedAt: '',
          pushAt: '',
          clickAt: '',
          failedAt: ''
        },
        dataRule: {
          openid: [
            { required: true, message: '接收者（用户）的 openid不能为空', trigger: 'blur' }
          ],
          formId: [
            { required: true, message: 'form_id不能为空', trigger: 'blur' }
          ],
          templateId: [
            { required: true, message: '模板消息的id不能为空', trigger: 'blur' }
          ],
          page: [
            { required: true, message: '小程序路径不能为空', trigger: 'blur' }
          ],
          formIdType: [
            { required: true, message: 'form_id类型 1-客户端登录 2-开锁 3-支付 4-运维端登录不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '推送状态 0-未使用 1-待推送（刚加入队列） 2-已推送 3-推送失败 4-已过期不能为空', trigger: 'blur' }
          ],
          isClick: [
            { required: true, message: '点击状态 0-未点击 1-已点击不能为空', trigger: 'blur' }
          ],
          keyword: [
            { required: true, message: '关键词数据不能为空', trigger: 'blur' }
          ],
          failedInfo: [
            { required: true, message: '错误信息不能为空', trigger: 'blur' }
          ],
          failedNum: [
            { required: true, message: '失败次数不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          pushAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          clickAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          failedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/bedside/bedsidemessage/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.openid = data.bedsidemessage.openid
                this.dataForm.formId = data.bedsidemessage.formId
                this.dataForm.templateId = data.bedsidemessage.templateId
                this.dataForm.page = data.bedsidemessage.page
                this.dataForm.formIdType = data.bedsidemessage.formIdType
                this.dataForm.status = data.bedsidemessage.status
                this.dataForm.isClick = data.bedsidemessage.isClick
                this.dataForm.keyword = data.bedsidemessage.keyword
                this.dataForm.failedInfo = data.bedsidemessage.failedInfo
                this.dataForm.failedNum = data.bedsidemessage.failedNum
                this.dataForm.createdAt = data.bedsidemessage.createdAt
                this.dataForm.updatedAt = data.bedsidemessage.updatedAt
                this.dataForm.pushAt = data.bedsidemessage.pushAt
                this.dataForm.clickAt = data.bedsidemessage.clickAt
                this.dataForm.failedAt = data.bedsidemessage.failedAt
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/bedside/bedsidemessage/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'openid': this.dataForm.openid,
                'formId': this.dataForm.formId,
                'templateId': this.dataForm.templateId,
                'page': this.dataForm.page,
                'formIdType': this.dataForm.formIdType,
                'status': this.dataForm.status,
                'isClick': this.dataForm.isClick,
                'keyword': this.dataForm.keyword,
                'failedInfo': this.dataForm.failedInfo,
                'failedNum': this.dataForm.failedNum,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'pushAt': this.dataForm.pushAt,
                'clickAt': this.dataForm.clickAt,
                'failedAt': this.dataForm.failedAt
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
