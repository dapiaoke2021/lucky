<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="订单号" prop="orderId">
      <el-input v-model="dataForm.orderId" placeholder="订单号"></el-input>
    </el-form-item>
    <el-form-item label="退款金额" prop="refundMoney">
      <el-input v-model="dataForm.refundMoney" placeholder="退款金额"></el-input>
    </el-form-item>
    <el-form-item label="退款单号" prop="orderSn">
      <el-input v-model="dataForm.orderSn" placeholder="退款单号"></el-input>
    </el-form-item>
    <el-form-item label="退款状态  默认0 申请中   1审核通过  2审核失败" prop="status">
      <el-input v-model="dataForm.status" placeholder="退款状态  默认0 申请中   1审核通过  2审核失败"></el-input>
    </el-form-item>
    <el-form-item label="退款编码" prop="transactionId">
      <el-input v-model="dataForm.transactionId" placeholder="退款编码"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="操作人" prop="adminId">
      <el-input v-model="dataForm.adminId" placeholder="操作人"></el-input>
    </el-form-item>
    <el-form-item label="" prop="disposeTime">
      <el-input v-model="dataForm.disposeTime" placeholder=""></el-input>
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
          orderId: '',
          refundMoney: '',
          orderSn: '',
          status: '',
          transactionId: '',
          createdAt: '',
          adminId: '',
          disposeTime: ''
        },
        dataRule: {
          orderId: [
            { required: true, message: '订单号不能为空', trigger: 'blur' }
          ],
          refundMoney: [
            { required: true, message: '退款金额不能为空', trigger: 'blur' }
          ],
          orderSn: [
            { required: true, message: '退款单号不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '退款状态  默认0 申请中   1审核通过  2审核失败不能为空', trigger: 'blur' }
          ],
          transactionId: [
            { required: true, message: '退款编码不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          adminId: [
            { required: true, message: '操作人不能为空', trigger: 'blur' }
          ],
          disposeTime: [
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
              url: this.$http.adornUrl(`/bedside/bedsiderefundorder/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderId = data.bedsiderefundorder.orderId
                this.dataForm.refundMoney = data.bedsiderefundorder.refundMoney
                this.dataForm.orderSn = data.bedsiderefundorder.orderSn
                this.dataForm.status = data.bedsiderefundorder.status
                this.dataForm.transactionId = data.bedsiderefundorder.transactionId
                this.dataForm.createdAt = data.bedsiderefundorder.createdAt
                this.dataForm.adminId = data.bedsiderefundorder.adminId
                this.dataForm.disposeTime = data.bedsiderefundorder.disposeTime
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
              url: this.$http.adornUrl(`/bedside/bedsiderefundorder/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderId': this.dataForm.orderId,
                'refundMoney': this.dataForm.refundMoney,
                'orderSn': this.dataForm.orderSn,
                'status': this.dataForm.status,
                'transactionId': this.dataForm.transactionId,
                'createdAt': this.dataForm.createdAt,
                'adminId': this.dataForm.adminId,
                'disposeTime': this.dataForm.disposeTime
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
