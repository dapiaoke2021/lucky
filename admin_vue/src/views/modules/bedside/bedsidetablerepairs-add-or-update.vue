<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="床头柜ID" prop="tableId">
      <el-input v-model="dataForm.tableId" placeholder="床头柜ID"></el-input>
    </el-form-item>
    <el-form-item label="用户唯一标识" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="用户唯一标识"></el-input>
    </el-form-item>
    <el-form-item label="运维人员ID" prop="opsId">
      <el-input v-model="dataForm.opsId" placeholder="运维人员ID"></el-input>
    </el-form-item>
    <el-form-item label="来源 1-客户端 2-运维端" prop="type">
      <el-input v-model="dataForm.type" placeholder="来源 1-客户端 2-运维端"></el-input>
    </el-form-item>
    <el-form-item label="状态 1-未指派 2-已指派，未处理 3-已处理" prop="status">
      <el-input v-model="dataForm.status" placeholder="状态 1-未指派 2-已指派，未处理 3-已处理"></el-input>
    </el-form-item>
    <el-form-item label="报修内容" prop="content">
      <el-input v-model="dataForm.content" placeholder="报修内容"></el-input>
    </el-form-item>
    <el-form-item label="图片" prop="img">
      <el-input v-model="dataForm.img" placeholder="图片"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="处理完成时间" prop="finishAt">
      <el-input v-model="dataForm.finishAt" placeholder="处理完成时间"></el-input>
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
          tableId: '',
          openid: '',
          opsId: '',
          type: '',
          status: '',
          content: '',
          img: '',
          createdAt: '',
          updatedAt: '',
          finishAt: ''
        },
        dataRule: {
          tableId: [
            { required: true, message: '床头柜ID不能为空', trigger: 'blur' }
          ],
          openid: [
            { required: true, message: '用户唯一标识不能为空', trigger: 'blur' }
          ],
          opsId: [
            { required: true, message: '运维人员ID不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '来源 1-客户端 2-运维端不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '状态 1-未指派 2-已指派，未处理 3-已处理不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '报修内容不能为空', trigger: 'blur' }
          ],
          img: [
            { required: true, message: '图片不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          finishAt: [
            { required: true, message: '处理完成时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsidetablerepairs/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.tableId = data.bedsidetablerepairs.tableId
                this.dataForm.openid = data.bedsidetablerepairs.openid
                this.dataForm.opsId = data.bedsidetablerepairs.opsId
                this.dataForm.type = data.bedsidetablerepairs.type
                this.dataForm.status = data.bedsidetablerepairs.status
                this.dataForm.content = data.bedsidetablerepairs.content
                this.dataForm.img = data.bedsidetablerepairs.img
                this.dataForm.createdAt = data.bedsidetablerepairs.createdAt
                this.dataForm.updatedAt = data.bedsidetablerepairs.updatedAt
                this.dataForm.finishAt = data.bedsidetablerepairs.finishAt
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
              url: this.$http.adornUrl(`/bedside/bedsidetablerepairs/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'tableId': this.dataForm.tableId,
                'openid': this.dataForm.openid,
                'opsId': this.dataForm.opsId,
                'type': this.dataForm.type,
                'status': this.dataForm.status,
                'content': this.dataForm.content,
                'img': this.dataForm.img,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'finishAt': this.dataForm.finishAt
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
