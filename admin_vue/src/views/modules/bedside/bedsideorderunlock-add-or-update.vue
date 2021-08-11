<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="订单号" prop="orderSn">
      <el-input v-model="dataForm.orderSn" placeholder="订单号"></el-input>
    </el-form-item>
    <el-form-item label="床头柜ID" prop="tableId">
      <el-input v-model="dataForm.tableId" placeholder="床头柜ID"></el-input>
    </el-form-item>
    <el-form-item label="二维码编号" prop="number">
      <el-input v-model="dataForm.number" placeholder="二维码编号"></el-input>
    </el-form-item>
    <el-form-item label="命令ID" prop="commandId">
      <el-input v-model="dataForm.commandId" placeholder="命令ID"></el-input>
    </el-form-item>
    <el-form-item label="用户唯一标识" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="用户唯一标识"></el-input>
    </el-form-item>
    <el-form-item label="开锁状态 1-未下发 2-已下发 3-已发送 4-已送达，已开锁 5-已关锁 6-下发失败" prop="status">
      <el-input v-model="dataForm.status" placeholder="开锁状态 1-未下发 2-已下发 3-已发送 4-已送达，已开锁 5-已关锁 6-下发失败"></el-input>
    </el-form-item>
    <el-form-item label="开锁方式 1-付费开锁 2-钥匙开锁" prop="type">
      <el-input v-model="dataForm.type" placeholder="开锁方式 1-付费开锁 2-钥匙开锁"></el-input>
    </el-form-item>
    <el-form-item label="使用时长（秒）" prop="time">
      <el-input v-model="dataForm.time" placeholder="使用时长（秒）"></el-input>
    </el-form-item>
    <el-form-item label="消费金额" prop="money">
      <el-input v-model="dataForm.money" placeholder="消费金额"></el-input>
    </el-form-item>
    <el-form-item label="" prop="content">
      <el-input v-model="dataForm.content" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="发起开锁时间" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder="发起开锁时间"></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="命令下发时间" prop="sendAt">
      <el-input v-model="dataForm.sendAt" placeholder="命令下发时间"></el-input>
    </el-form-item>
    <el-form-item label="命令发送时间" prop="sentAt">
      <el-input v-model="dataForm.sentAt" placeholder="命令发送时间"></el-input>
    </el-form-item>
    <el-form-item label="实际开锁时间" prop="startAt">
      <el-input v-model="dataForm.startAt" placeholder="实际开锁时间"></el-input>
    </el-form-item>
    <el-form-item label="关锁时间" prop="endAt">
      <el-input v-model="dataForm.endAt" placeholder="关锁时间"></el-input>
    </el-form-item>
    <el-form-item label="锁类型  1nb锁   2蓝牙锁" prop="tableType">
      <el-input v-model="dataForm.tableType" placeholder="锁类型  1nb锁   2蓝牙锁"></el-input>
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
          tableId: '',
          number: '',
          commandId: '',
          openid: '',
          status: '',
          type: '',
          time: '',
          money: '',
          content: '',
          createdAt: '',
          updatedAt: '',
          sendAt: '',
          sentAt: '',
          startAt: '',
          endAt: '',
          tableType: ''
        },
        dataRule: {
          orderSn: [
            { required: true, message: '订单号不能为空', trigger: 'blur' }
          ],
          tableId: [
            { required: true, message: '床头柜ID不能为空', trigger: 'blur' }
          ],
          number: [
            { required: true, message: '二维码编号不能为空', trigger: 'blur' }
          ],
          commandId: [
            { required: true, message: '命令ID不能为空', trigger: 'blur' }
          ],
          openid: [
            { required: true, message: '用户唯一标识不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '开锁状态 1-未下发 2-已下发 3-已发送 4-已送达，已开锁 5-已关锁 6-下发失败不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '开锁方式 1-付费开锁 2-钥匙开锁不能为空', trigger: 'blur' }
          ],
          time: [
            { required: true, message: '使用时长（秒）不能为空', trigger: 'blur' }
          ],
          money: [
            { required: true, message: '消费金额不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '发起开锁时间不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          sendAt: [
            { required: true, message: '命令下发时间不能为空', trigger: 'blur' }
          ],
          sentAt: [
            { required: true, message: '命令发送时间不能为空', trigger: 'blur' }
          ],
          startAt: [
            { required: true, message: '实际开锁时间不能为空', trigger: 'blur' }
          ],
          endAt: [
            { required: true, message: '关锁时间不能为空', trigger: 'blur' }
          ],
          tableType: [
            { required: true, message: '锁类型  1nb锁   2蓝牙锁不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsideorderunlock/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderSn = data.bedsideorderunlock.orderSn
                this.dataForm.tableId = data.bedsideorderunlock.tableId
                this.dataForm.number = data.bedsideorderunlock.number
                this.dataForm.commandId = data.bedsideorderunlock.commandId
                this.dataForm.openid = data.bedsideorderunlock.openid
                this.dataForm.status = data.bedsideorderunlock.status
                this.dataForm.type = data.bedsideorderunlock.type
                this.dataForm.time = data.bedsideorderunlock.time
                this.dataForm.money = data.bedsideorderunlock.money
                this.dataForm.content = data.bedsideorderunlock.content
                this.dataForm.createdAt = data.bedsideorderunlock.createdAt
                this.dataForm.updatedAt = data.bedsideorderunlock.updatedAt
                this.dataForm.sendAt = data.bedsideorderunlock.sendAt
                this.dataForm.sentAt = data.bedsideorderunlock.sentAt
                this.dataForm.startAt = data.bedsideorderunlock.startAt
                this.dataForm.endAt = data.bedsideorderunlock.endAt
                this.dataForm.tableType = data.bedsideorderunlock.tableType
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
              url: this.$http.adornUrl(`/bedside/bedsideorderunlock/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderSn': this.dataForm.orderSn,
                'tableId': this.dataForm.tableId,
                'number': this.dataForm.number,
                'commandId': this.dataForm.commandId,
                'openid': this.dataForm.openid,
                'status': this.dataForm.status,
                'type': this.dataForm.type,
                'time': this.dataForm.time,
                'money': this.dataForm.money,
                'content': this.dataForm.content,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'sendAt': this.dataForm.sendAt,
                'sentAt': this.dataForm.sentAt,
                'startAt': this.dataForm.startAt,
                'endAt': this.dataForm.endAt,
                'tableType': this.dataForm.tableType
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
