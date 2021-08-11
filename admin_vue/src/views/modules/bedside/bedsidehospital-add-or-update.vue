<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="医院名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="医院名称"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="dataForm.password" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item label="联系人" prop="contact">
      <el-input v-model="dataForm.contact" placeholder="联系人"></el-input>
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="dataForm.phone" placeholder="手机号"></el-input>
    </el-form-item>
    <el-form-item label="省" prop="province">
      <el-input v-model="dataForm.province" placeholder="省"></el-input>
    </el-form-item>
    <el-form-item label="市" prop="city">
      <el-input v-model="dataForm.city" placeholder="市"></el-input>
    </el-form-item>
    <el-form-item label="区县" prop="county">
      <el-input v-model="dataForm.county" placeholder="区县"></el-input>
    </el-form-item>
    <el-form-item label="详细地址" prop="address">
      <el-input v-model="dataForm.address" placeholder="详细地址"></el-input>
    </el-form-item>
    <el-form-item label="使用开始时间" prop="startTime">
      <el-input v-model="dataForm.startTime" placeholder="使用开始时间"></el-input>
    </el-form-item>
    <el-form-item label="使用结束时间" prop="endTime">
      <el-input v-model="dataForm.endTime" placeholder="使用结束时间"></el-input>
    </el-form-item>
    <el-form-item label="单价" prop="money">
      <el-input v-model="dataForm.money" placeholder="单价"></el-input>
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
    <el-form-item label="付款方式  0 直接付费  1押金" prop="payType">
      <el-input v-model="dataForm.payType" placeholder="付款方式  0 直接付费  1押金"></el-input>
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
          password: '',
          contact: '',
          phone: '',
          province: '',
          city: '',
          county: '',
          address: '',
          startTime: '',
          endTime: '',
          money: '',
          rememberToken: '',
          createdAt: '',
          updatedAt: '',
          deletedAt: '',
          payType: ''
        },
        dataRule: {
          name: [
            { required: true, message: '医院名称不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          contact: [
            { required: true, message: '联系人不能为空', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '手机号不能为空', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '省不能为空', trigger: 'blur' }
          ],
          city: [
            { required: true, message: '市不能为空', trigger: 'blur' }
          ],
          county: [
            { required: true, message: '区县不能为空', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '详细地址不能为空', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '使用开始时间不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '使用结束时间不能为空', trigger: 'blur' }
          ],
          money: [
            { required: true, message: '单价不能为空', trigger: 'blur' }
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
          ],
          payType: [
            { required: true, message: '付款方式  0 直接付费  1押金不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsidehospital/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.bedsidehospital.name
                this.dataForm.password = data.bedsidehospital.password
                this.dataForm.contact = data.bedsidehospital.contact
                this.dataForm.phone = data.bedsidehospital.phone
                this.dataForm.province = data.bedsidehospital.province
                this.dataForm.city = data.bedsidehospital.city
                this.dataForm.county = data.bedsidehospital.county
                this.dataForm.address = data.bedsidehospital.address
                this.dataForm.startTime = data.bedsidehospital.startTime
                this.dataForm.endTime = data.bedsidehospital.endTime
                this.dataForm.money = data.bedsidehospital.money
                this.dataForm.rememberToken = data.bedsidehospital.rememberToken
                this.dataForm.createdAt = data.bedsidehospital.createdAt
                this.dataForm.updatedAt = data.bedsidehospital.updatedAt
                this.dataForm.deletedAt = data.bedsidehospital.deletedAt
                this.dataForm.payType = data.bedsidehospital.payType
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
              url: this.$http.adornUrl(`/bedside/bedsidehospital/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'password': this.dataForm.password,
                'contact': this.dataForm.contact,
                'phone': this.dataForm.phone,
                'province': this.dataForm.province,
                'city': this.dataForm.city,
                'county': this.dataForm.county,
                'address': this.dataForm.address,
                'startTime': this.dataForm.startTime,
                'endTime': this.dataForm.endTime,
                'money': this.dataForm.money,
                'rememberToken': this.dataForm.rememberToken,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'deletedAt': this.dataForm.deletedAt,
                'payType': this.dataForm.payType
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
