<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="医院ID" prop="hospitalId">
      <el-input v-model="dataForm.hospitalId" placeholder="医院ID"></el-input>
    </el-form-item>
    <el-form-item label="科室ID" prop="officeId">
      <el-input v-model="dataForm.officeId" placeholder="科室ID"></el-input>
    </el-form-item>
    <el-form-item label="房间号ID" prop="roomId">
      <el-input v-model="dataForm.roomId" placeholder="房间号ID"></el-input>
    </el-form-item>
    <el-form-item label="二维码编号" prop="number">
      <el-input v-model="dataForm.number" placeholder="二维码编号"></el-input>
    </el-form-item>
    <el-form-item label="设备编号" prop="deviceId">
      <el-input v-model="dataForm.deviceId" placeholder="设备编号"></el-input>
    </el-form-item>
    <el-form-item label="状态 1-待命中 2-使用中 3-维修中 4-禁用中 5开锁故障" prop="status">
      <el-input v-model="dataForm.status" placeholder="状态 1-待命中 2-使用中 3-维修中 4-禁用中 5开锁故障"></el-input>
    </el-form-item>
    <el-form-item label="信号强度" prop="signal">
      <el-input v-model="dataForm.signal" placeholder="信号强度"></el-input>
    </el-form-item>
    <el-form-item label="电量" prop="battery">
      <el-input v-model="dataForm.battery" placeholder="电量"></el-input>
    </el-form-item>
    <el-form-item label="累计使用次数" prop="num">
      <el-input v-model="dataForm.num" placeholder="累计使用次数"></el-input>
    </el-form-item>
    <el-form-item label="累计赚取金额" prop="money">
      <el-input v-model="dataForm.money" placeholder="累计赚取金额"></el-input>
    </el-form-item>
    <el-form-item label="是否为测试设备 1-是 0-否" prop="isTest">
      <el-input v-model="dataForm.isTest" placeholder="是否为测试设备 1-是 0-否"></el-input>
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
    <el-form-item label="是否删除  默认0" prop="isDelete">
      <el-input v-model="dataForm.isDelete" placeholder="是否删除  默认0"></el-input>
    </el-form-item>
    <el-form-item label="锁类型  1 感应锁   2蓝牙锁" prop="type">
      <el-input v-model="dataForm.type" placeholder="锁类型  1 感应锁   2蓝牙锁"></el-input>
    </el-form-item>
    <el-form-item label="蓝牙锁开锁密码" prop="macId">
      <el-input v-model="dataForm.macId" placeholder="蓝牙锁开锁密码"></el-input>
    </el-form-item>
    <el-form-item label="1大禹锁厂 2 洛阳 3行越锁厂" prop="source">
      <el-input v-model="dataForm.source" placeholder="1大禹锁厂 2 洛阳 3行越锁厂"></el-input>
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
          hospitalId: '',
          officeId: '',
          roomId: '',
          number: '',
          deviceId: '',
          status: '',
          signal: '',
          battery: '',
          num: '',
          money: '',
          isTest: '',
          createdAt: '',
          updatedAt: '',
          deletedAt: '',
          isDelete: '',
          type: '',
          macId: '',
          source: ''
        },
        dataRule: {
          hospitalId: [
            { required: true, message: '医院ID不能为空', trigger: 'blur' }
          ],
          officeId: [
            { required: true, message: '科室ID不能为空', trigger: 'blur' }
          ],
          roomId: [
            { required: true, message: '房间号ID不能为空', trigger: 'blur' }
          ],
          number: [
            { required: true, message: '二维码编号不能为空', trigger: 'blur' }
          ],
          deviceId: [
            { required: true, message: '设备编号不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '状态 1-待命中 2-使用中 3-维修中 4-禁用中 5开锁故障不能为空', trigger: 'blur' }
          ],
          signal: [
            { required: true, message: '信号强度不能为空', trigger: 'blur' }
          ],
          battery: [
            { required: true, message: '电量不能为空', trigger: 'blur' }
          ],
          num: [
            { required: true, message: '累计使用次数不能为空', trigger: 'blur' }
          ],
          money: [
            { required: true, message: '累计赚取金额不能为空', trigger: 'blur' }
          ],
          isTest: [
            { required: true, message: '是否为测试设备 1-是 0-否不能为空', trigger: 'blur' }
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
          isDelete: [
            { required: true, message: '是否删除  默认0不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '锁类型  1 感应锁   2蓝牙锁不能为空', trigger: 'blur' }
          ],
          macId: [
            { required: true, message: '蓝牙锁开锁密码不能为空', trigger: 'blur' }
          ],
          source: [
            { required: true, message: '1大禹锁厂 2 洛阳 3行越锁厂不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsidetable/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.hospitalId = data.bedsidetable.hospitalId
                this.dataForm.officeId = data.bedsidetable.officeId
                this.dataForm.roomId = data.bedsidetable.roomId
                this.dataForm.number = data.bedsidetable.number
                this.dataForm.deviceId = data.bedsidetable.deviceId
                this.dataForm.status = data.bedsidetable.status
                this.dataForm.signal = data.bedsidetable.signal
                this.dataForm.battery = data.bedsidetable.battery
                this.dataForm.num = data.bedsidetable.num
                this.dataForm.money = data.bedsidetable.money
                this.dataForm.isTest = data.bedsidetable.isTest
                this.dataForm.createdAt = data.bedsidetable.createdAt
                this.dataForm.updatedAt = data.bedsidetable.updatedAt
                this.dataForm.deletedAt = data.bedsidetable.deletedAt
                this.dataForm.isDelete = data.bedsidetable.isDelete
                this.dataForm.type = data.bedsidetable.type
                this.dataForm.macId = data.bedsidetable.macId
                this.dataForm.source = data.bedsidetable.source
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
              url: this.$http.adornUrl(`/bedside/bedsidetable/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'hospitalId': this.dataForm.hospitalId,
                'officeId': this.dataForm.officeId,
                'roomId': this.dataForm.roomId,
                'number': this.dataForm.number,
                'deviceId': this.dataForm.deviceId,
                'status': this.dataForm.status,
                'signal': this.dataForm.signal,
                'battery': this.dataForm.battery,
                'num': this.dataForm.num,
                'money': this.dataForm.money,
                'isTest': this.dataForm.isTest,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
                'deletedAt': this.dataForm.deletedAt,
                'isDelete': this.dataForm.isDelete,
                'type': this.dataForm.type,
                'macId': this.dataForm.macId,
                'source': this.dataForm.source
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
