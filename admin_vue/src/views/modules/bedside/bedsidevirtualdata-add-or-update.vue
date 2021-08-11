<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="增加数量" prop="user">
      <el-input v-model="dataForm.user" placeholder="增加数量"></el-input>
    </el-form-item>
    <el-form-item label="订单" prop="order">
      <el-input v-model="dataForm.order" placeholder="订单"></el-input>
    </el-form-item>
    <el-form-item label="订单金额" prop="orderMoney">
      <el-input v-model="dataForm.orderMoney" placeholder="订单金额"></el-input>
    </el-form-item>
    <el-form-item label="添加时间 年月日" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder="添加时间 年月日"></el-input>
    </el-form-item>
    <el-form-item label="更新时间" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder="更新时间"></el-input>
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
          user: '',
          order: '',
          orderMoney: '',
          createdAt: '',
          updatedAt: ''
        },
        dataRule: {
          user: [
            { required: true, message: '增加数量不能为空', trigger: 'blur' }
          ],
          order: [
            { required: true, message: '订单不能为空', trigger: 'blur' }
          ],
          orderMoney: [
            { required: true, message: '订单金额不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '添加时间 年月日不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '更新时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsidevirtualdata/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.user = data.bedsidevirtualdata.user
                this.dataForm.order = data.bedsidevirtualdata.order
                this.dataForm.orderMoney = data.bedsidevirtualdata.orderMoney
                this.dataForm.createdAt = data.bedsidevirtualdata.createdAt
                this.dataForm.updatedAt = data.bedsidevirtualdata.updatedAt
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
              url: this.$http.adornUrl(`/bedside/bedsidevirtualdata/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'user': this.dataForm.user,
                'order': this.dataForm.order,
                'orderMoney': this.dataForm.orderMoney,
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
