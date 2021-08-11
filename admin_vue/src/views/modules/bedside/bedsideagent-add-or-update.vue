<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="代理商名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="代理商名称"></el-input>
    </el-form-item>
    <el-form-item label="类型 1-个人 2-企业" prop="type">
      <el-input v-model="dataForm.type" placeholder="类型 1-个人 2-企业"></el-input>
    </el-form-item>
    <el-form-item label="联系人" prop="contact">
      <el-input v-model="dataForm.contact" placeholder="联系人"></el-input>
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
    <el-form-item label="保证金" prop="money">
      <el-input v-model="dataForm.money" placeholder="保证金"></el-input>
    </el-form-item>
    <el-form-item label="" prop="rememberToken">
      <el-input v-model="dataForm.rememberToken" placeholder=""></el-input>
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
          type: '',
          contact: '',
          phone: '',
          password: '',
          address: '',
          money: '',
          rememberToken: '',
          createdAt: '',
          updatedAt: '',
          deletedAt: ''
        },
        dataRule: {
          name: [
            { required: true, message: '代理商名称不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '类型 1-个人 2-企业不能为空', trigger: 'blur' }
          ],
          contact: [
            { required: true, message: '联系人不能为空', trigger: 'blur' }
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
          money: [
            { required: true, message: '保证金不能为空', trigger: 'blur' }
          ],
          rememberToken: [
            { required: true, message: '不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsideagent/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.bedsideagent.name
                this.dataForm.type = data.bedsideagent.type
                this.dataForm.contact = data.bedsideagent.contact
                this.dataForm.phone = data.bedsideagent.phone
                this.dataForm.password = data.bedsideagent.password
                this.dataForm.address = data.bedsideagent.address
                this.dataForm.money = data.bedsideagent.money
                this.dataForm.rememberToken = data.bedsideagent.rememberToken
                this.dataForm.createdAt = data.bedsideagent.createdAt
                this.dataForm.updatedAt = data.bedsideagent.updatedAt
                this.dataForm.deletedAt = data.bedsideagent.deletedAt
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
              url: this.$http.adornUrl(`/bedside/bedsideagent/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'type': this.dataForm.type,
                'contact': this.dataForm.contact,
                'phone': this.dataForm.phone,
                'password': this.dataForm.password,
                'address': this.dataForm.address,
                'money': this.dataForm.money,
                'rememberToken': this.dataForm.rememberToken,
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
