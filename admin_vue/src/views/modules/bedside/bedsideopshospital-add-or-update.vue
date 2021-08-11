<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="运维人员ID" prop="opsId">
      <el-input v-model="dataForm.opsId" placeholder="运维人员ID"></el-input>
    </el-form-item>
    <el-form-item label="医院ID" prop="hospitalId">
      <el-input v-model="dataForm.hospitalId" placeholder="医院ID"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
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
          opsId: '',
          hospitalId: '',
          createdAt: '',
          updatedAt: ''
        },
        dataRule: {
          opsId: [
            { required: true, message: '运维人员ID不能为空', trigger: 'blur' }
          ],
          hospitalId: [
            { required: true, message: '医院ID不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updatedAt: [
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
              url: this.$http.adornUrl(`/bedside/bedsideopshospital/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.opsId = data.bedsideopshospital.opsId
                this.dataForm.hospitalId = data.bedsideopshospital.hospitalId
                this.dataForm.createdAt = data.bedsideopshospital.createdAt
                this.dataForm.updatedAt = data.bedsideopshospital.updatedAt
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
              url: this.$http.adornUrl(`/bedside/bedsideopshospital/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'opsId': this.dataForm.opsId,
                'hospitalId': this.dataForm.hospitalId,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt
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
