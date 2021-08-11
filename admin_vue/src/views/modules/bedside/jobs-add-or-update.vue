<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="queue">
      <el-input v-model="dataForm.queue" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="payload">
      <el-input v-model="dataForm.payload" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="attempts">
      <el-input v-model="dataForm.attempts" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="reservedAt">
      <el-input v-model="dataForm.reservedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="availableAt">
      <el-input v-model="dataForm.availableAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
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
          queue: '',
          payload: '',
          attempts: '',
          reservedAt: '',
          availableAt: '',
          createdAt: ''
        },
        dataRule: {
          queue: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          payload: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          attempts: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          reservedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          availableAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createdAt: [
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
              url: this.$http.adornUrl(`/bedside/jobs/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.queue = data.jobs.queue
                this.dataForm.payload = data.jobs.payload
                this.dataForm.attempts = data.jobs.attempts
                this.dataForm.reservedAt = data.jobs.reservedAt
                this.dataForm.availableAt = data.jobs.availableAt
                this.dataForm.createdAt = data.jobs.createdAt
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
              url: this.$http.adornUrl(`/bedside/jobs/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'queue': this.dataForm.queue,
                'payload': this.dataForm.payload,
                'attempts': this.dataForm.attempts,
                'reservedAt': this.dataForm.reservedAt,
                'availableAt': this.dataForm.availableAt,
                'createdAt': this.dataForm.createdAt
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
