<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="connection">
      <el-input v-model="dataForm.connection" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="queue">
      <el-input v-model="dataForm.queue" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="payload">
      <el-input v-model="dataForm.payload" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="exception">
      <el-input v-model="dataForm.exception" placeholder=""></el-input>
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
          connection: '',
          queue: '',
          payload: '',
          exception: '',
          failedAt: ''
        },
        dataRule: {
          connection: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          queue: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          payload: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          exception: [
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
              url: this.$http.adornUrl(`/bedside/failedjobs/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.connection = data.failedjobs.connection
                this.dataForm.queue = data.failedjobs.queue
                this.dataForm.payload = data.failedjobs.payload
                this.dataForm.exception = data.failedjobs.exception
                this.dataForm.failedAt = data.failedjobs.failedAt
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
              url: this.$http.adornUrl(`/bedside/failedjobs/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'connection': this.dataForm.connection,
                'queue': this.dataForm.queue,
                'payload': this.dataForm.payload,
                'exception': this.dataForm.exception,
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
