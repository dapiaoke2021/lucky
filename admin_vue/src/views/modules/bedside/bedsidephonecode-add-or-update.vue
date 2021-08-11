<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="dataForm.phone" placeholder="手机号"></el-input>
    </el-form-item>
    <el-form-item label="验证码" prop="code">
      <el-input v-model="dataForm.code" placeholder="验证码"></el-input>
    </el-form-item>
    <el-form-item label="类型 1-重置密码" prop="type">
      <el-input v-model="dataForm.type" placeholder="类型 1-重置密码"></el-input>
    </el-form-item>
    <el-form-item label="状态 1-未使用 2-已使用 3-已过期" prop="status">
      <el-input v-model="dataForm.status" placeholder="状态 1-未使用 2-已使用 3-已过期"></el-input>
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
          phone: '',
          code: '',
          type: '',
          status: '',
          createdAt: '',
          updatedAt: ''
        },
        dataRule: {
          phone: [
            { required: true, message: '手机号不能为空', trigger: 'blur' }
          ],
          code: [
            { required: true, message: '验证码不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '类型 1-重置密码不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '状态 1-未使用 2-已使用 3-已过期不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsidephonecode/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.phone = data.bedsidephonecode.phone
                this.dataForm.code = data.bedsidephonecode.code
                this.dataForm.type = data.bedsidephonecode.type
                this.dataForm.status = data.bedsidephonecode.status
                this.dataForm.createdAt = data.bedsidephonecode.createdAt
                this.dataForm.updatedAt = data.bedsidephonecode.updatedAt
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
              url: this.$http.adornUrl(`/bedside/bedsidephonecode/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'phone': this.dataForm.phone,
                'code': this.dataForm.code,
                'type': this.dataForm.type,
                'status': this.dataForm.status,
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
