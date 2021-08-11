<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="运维人员名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="运维人员名称"></el-input>
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="dataForm.phone" placeholder="手机号"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="dataForm.password" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item label="联系地址" prop="address">
      <el-input v-model="dataForm.address" placeholder="联系地址"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="deletedAt">
      <el-input v-model="dataForm.deletedAt" placeholder=""></el-input>
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
          name: '',
          phone: '',
          password: '',
          address: '',
          createdAt: '',
          updatedAt: '',
          deletedAt: ''
        },
        dataRule: {
          name: [
            { required: true, message: '运维人员名称不能为空', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '手机号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '联系地址不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deletedAt: [
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
              url: this.$http.adornUrl(`/bedside/bedsideops/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.bedsideops.name
                this.dataForm.phone = data.bedsideops.phone
                this.dataForm.password = data.bedsideops.password
                this.dataForm.address = data.bedsideops.address
                this.dataForm.createdAt = data.bedsideops.createdAt
                this.dataForm.updatedAt = data.bedsideops.updatedAt
                this.dataForm.deletedAt = data.bedsideops.deletedAt
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
              url: this.$http.adornUrl(`/bedside/bedsideops/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'phone': this.dataForm.phone,
                'password': this.dataForm.password,
                'address': this.dataForm.address,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'deletedAt': this.dataForm.deletedAt
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
