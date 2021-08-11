<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="订单号" prop="orderSn">
      <el-input v-model="dataForm.orderSn" placeholder="订单号"></el-input>
    </el-form-item>
    <el-form-item label="二维码编号" prop="number">
      <el-input v-model="dataForm.number" placeholder="二维码编号"></el-input>
    </el-form-item>
    <el-form-item label="用户唯一标识" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="用户唯一标识"></el-input>
    </el-form-item>
    <el-form-item label="床头柜ID" prop="tableId">
      <el-input v-model="dataForm.tableId" placeholder="床头柜ID"></el-input>
    </el-form-item>
    <el-form-item label="状态 1-待付款 2-已付款" prop="status">
      <el-input v-model="dataForm.status" placeholder="状态 1-待付款 2-已付款"></el-input>
    </el-form-item>
    <el-form-item label="使用时长（秒）" prop="time">
      <el-input v-model="dataForm.time" placeholder="使用时长（秒）"></el-input>
    </el-form-item>
    <el-form-item label="消费金额" prop="money">
      <el-input v-model="dataForm.money" placeholder="消费金额"></el-input>
    </el-form-item>
    <el-form-item label="开关锁次数" prop="num">
      <el-input v-model="dataForm.num" placeholder="开关锁次数"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="付款时间" prop="payAt">
      <el-input v-model="dataForm.payAt" placeholder="付款时间"></el-input>
    </el-form-item>
    <el-form-item label="关锁时间" prop="endAt">
      <el-input v-model="dataForm.endAt" placeholder="关锁时间"></el-input>
    </el-form-item>
    <el-form-item label="退款状态" prop="refundStatus">
      <el-input v-model="dataForm.refundStatus" placeholder="退款状态"></el-input>
    </el-form-item>
    <el-form-item label="锁类型  默认1 感应锁  2蓝牙锁 " prop="type">
      <el-input v-model="dataForm.type" placeholder="锁类型  默认1 感应锁  2蓝牙锁 "></el-input>
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
          orderSn: '',
          number: '',
          openid: '',
          tableId: '',
          status: '',
          time: '',
          money: '',
          num: '',
          createdAt: '',
          updatedAt: '',
          payAt: '',
          endAt: '',
          refundStatus: '',
          type: ''
        },
        dataRule: {
          orderSn: [
            { required: true, message: '订单号不能为空', trigger: 'blur' }
          ],
          number: [
            { required: true, message: '二维码编号不能为空', trigger: 'blur' }
          ],
          openid: [
            { required: true, message: '用户唯一标识不能为空', trigger: 'blur' }
          ],
          tableId: [
            { required: true, message: '床头柜ID不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '状态 1-待付款 2-已付款不能为空', trigger: 'blur' }
          ],
          time: [
            { required: true, message: '使用时长（秒）不能为空', trigger: 'blur' }
          ],
          money: [
            { required: true, message: '消费金额不能为空', trigger: 'blur' }
          ],
          num: [
            { required: true, message: '开关锁次数不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          payAt: [
            { required: true, message: '付款时间不能为空', trigger: 'blur' }
          ],
          endAt: [
            { required: true, message: '关锁时间不能为空', trigger: 'blur' }
          ],
          refundStatus: [
            { required: true, message: '退款状态不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '锁类型  默认1 感应锁  2蓝牙锁 不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsideorder/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderSn = data.bedsideorder.orderSn
                this.dataForm.number = data.bedsideorder.number
                this.dataForm.openid = data.bedsideorder.openid
                this.dataForm.tableId = data.bedsideorder.tableId
                this.dataForm.status = data.bedsideorder.status
                this.dataForm.time = data.bedsideorder.time
                this.dataForm.money = data.bedsideorder.money
                this.dataForm.num = data.bedsideorder.num
                this.dataForm.createdAt = data.bedsideorder.createdAt
                this.dataForm.updatedAt = data.bedsideorder.updatedAt
                this.dataForm.payAt = data.bedsideorder.payAt
                this.dataForm.endAt = data.bedsideorder.endAt
                this.dataForm.refundStatus = data.bedsideorder.refundStatus
                this.dataForm.type = data.bedsideorder.type
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
              url: this.$http.adornUrl(`/bedside/bedsideorder/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderSn': this.dataForm.orderSn,
                'number': this.dataForm.number,
                'openid': this.dataForm.openid,
                'tableId': this.dataForm.tableId,
                'status': this.dataForm.status,
                'time': this.dataForm.time,
                'money': this.dataForm.money,
                'num': this.dataForm.num,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'payAt': this.dataForm.payAt,
                'endAt': this.dataForm.endAt,
                'refundStatus': this.dataForm.refundStatus,
                'type': this.dataForm.type
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
