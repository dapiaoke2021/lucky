<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="配置名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="配置名称"></el-input>
    </el-form-item>
    <el-form-item label="配置值" prop="value">
      <el-input v-model="dataForm.value" placeholder="配置值"></el-input>
    </el-form-item>
    <el-form-item label="说明（描述）" prop="desc">
      <el-input v-model="dataForm.desc" placeholder="说明（描述）"></el-input>
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
          value: '',
          desc: '',
          createdAt: '',
          updatedAt: '',
          deletedAt: ''
        },
        dataRule: {
          name: [
            { required: true, message: '配置名称不能为空', trigger: 'blur' }
          ],
          value: [
            { required: true, message: '配置值不能为空', trigger: 'blur' }
          ],
          desc: [
            { required: true, message: '说明（描述）不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsideconfig/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.bedsideconfig.name
                this.dataForm.value = data.bedsideconfig.value
                this.dataForm.desc = data.bedsideconfig.desc
                this.dataForm.createdAt = data.bedsideconfig.createdAt
                this.dataForm.updatedAt = data.bedsideconfig.updatedAt
                this.dataForm.deletedAt = data.bedsideconfig.deletedAt
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
              url: this.$http.adornUrl(`/bedside/bedsideconfig/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'value': this.dataForm.value,
                'desc': this.dataForm.desc,
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
