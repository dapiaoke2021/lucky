<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="支付宝唯一标识" prop="aliUserId">
      <el-input v-model="dataForm.aliUserId" placeholder="支付宝唯一标识"></el-input>
    </el-form-item>
    <el-form-item label="用户唯一标识" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="用户唯一标识"></el-input>
    </el-form-item>
    <el-form-item label="用户在微信开放平台的唯一标识符" prop="unionid">
      <el-input v-model="dataForm.unionid" placeholder="用户在微信开放平台的唯一标识符"></el-input>
    </el-form-item>
    <el-form-item label="用户昵称" prop="nickname">
      <el-input v-model="dataForm.nickname" placeholder="用户昵称"></el-input>
    </el-form-item>
    <el-form-item label="用户头像" prop="avatar">
      <el-input v-model="dataForm.avatar" placeholder="用户头像"></el-input>
    </el-form-item>
    <el-form-item label="性别 0：未知、1：男、2：女" prop="gender">
      <el-input v-model="dataForm.gender" placeholder="性别 0：未知、1：男、2：女"></el-input>
    </el-form-item>
    <el-form-item label="类型 1-客户端用户 2-运维端用户" prop="type">
      <el-input v-model="dataForm.type" placeholder="类型 1-客户端用户 2-运维端用户"></el-input>
    </el-form-item>
    <el-form-item label="累计消费金额" prop="money">
      <el-input v-model="dataForm.money" placeholder="累计消费金额"></el-input>
    </el-form-item>
    <el-form-item label="累计使用次数" prop="number">
      <el-input v-model="dataForm.number" placeholder="累计使用次数"></el-input>
    </el-form-item>
    <el-form-item label="用户绑定的手机号（国外手机号会有区号）" prop="phoneNumber">
      <el-input v-model="dataForm.phoneNumber" placeholder="用户绑定的手机号（国外手机号会有区号）"></el-input>
    </el-form-item>
    <el-form-item label="没有区号的手机号" prop="mobile">
      <el-input v-model="dataForm.mobile" placeholder="没有区号的手机号"></el-input>
    </el-form-item>
    <el-form-item label="区号" prop="countryCode">
      <el-input v-model="dataForm.countryCode" placeholder="区号"></el-input>
    </el-form-item>
    <el-form-item label="用户所在国家" prop="country">
      <el-input v-model="dataForm.country" placeholder="用户所在国家"></el-input>
    </el-form-item>
    <el-form-item label="用户所在省份" prop="province">
      <el-input v-model="dataForm.province" placeholder="用户所在省份"></el-input>
    </el-form-item>
    <el-form-item label="用户所在城市" prop="city">
      <el-input v-model="dataForm.city" placeholder="用户所在城市"></el-input>
    </el-form-item>
    <el-form-item label="手机品牌" prop="brand">
      <el-input v-model="dataForm.brand" placeholder="手机品牌"></el-input>
    </el-form-item>
    <el-form-item label="手机型号" prop="model">
      <el-input v-model="dataForm.model" placeholder="手机型号"></el-input>
    </el-form-item>
    <el-form-item label="微信版本号" prop="version">
      <el-input v-model="dataForm.version" placeholder="微信版本号"></el-input>
    </el-form-item>
    <el-form-item label="操作系统版本" prop="system">
      <el-input v-model="dataForm.system" placeholder="操作系统版本"></el-input>
    </el-form-item>
    <el-form-item label="客户端平台" prop="platform">
      <el-input v-model="dataForm.platform" placeholder="客户端平台"></el-input>
    </el-form-item>
    <el-form-item label="客户端基础库版本" prop="sdkVersion">
      <el-input v-model="dataForm.sdkVersion" placeholder="客户端基础库版本"></el-input>
    </el-form-item>
    <el-form-item label="ip地址" prop="ip">
      <el-input v-model="dataForm.ip" placeholder="ip地址"></el-input>
    </el-form-item>
    <el-form-item label="纬度" prop="latitude">
      <el-input v-model="dataForm.latitude" placeholder="纬度"></el-input>
    </el-form-item>
    <el-form-item label="经度" prop="longitude">
      <el-input v-model="dataForm.longitude" placeholder="经度"></el-input>
    </el-form-item>
    <el-form-item label="是否获取手机信息 1-未获取 2-已获取" prop="isAuth">
      <el-input v-model="dataForm.isAuth" placeholder="是否获取手机信息 1-未获取 2-已获取"></el-input>
    </el-form-item>
    <el-form-item label="是否获取过用户信息 1-未获取 2-已获取" prop="isMobile">
      <el-input v-model="dataForm.isMobile" placeholder="是否获取过用户信息 1-未获取 2-已获取"></el-input>
    </el-form-item>
    <el-form-item label="是否授权过地理定位 1-否 2-是" prop="isLocation">
      <el-input v-model="dataForm.isLocation" placeholder="是否授权过地理定位 1-否 2-是"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updatedAt">
      <el-input v-model="dataForm.updatedAt" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="来源 1 微信  2支付宝" prop="source">
      <el-input v-model="dataForm.source" placeholder="来源 1 微信  2支付宝"></el-input>
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
          aliUserId: '',
          openid: '',
          unionid: '',
          nickname: '',
          avatar: '',
          gender: '',
          type: '',
          money: '',
          number: '',
          phoneNumber: '',
          mobile: '',
          countryCode: '',
          country: '',
          province: '',
          city: '',
          brand: '',
          model: '',
          version: '',
          system: '',
          platform: '',
          sdkVersion: '',
          ip: '',
          latitude: '',
          longitude: '',
          isAuth: '',
          isMobile: '',
          isLocation: '',
          createdAt: '',
          updatedAt: '',
          source: ''
        },
        dataRule: {
          aliUserId: [
            { required: true, message: '支付宝唯一标识不能为空', trigger: 'blur' }
          ],
          openid: [
            { required: true, message: '用户唯一标识不能为空', trigger: 'blur' }
          ],
          unionid: [
            { required: true, message: '用户在微信开放平台的唯一标识符不能为空', trigger: 'blur' }
          ],
          nickname: [
            { required: true, message: '用户昵称不能为空', trigger: 'blur' }
          ],
          avatar: [
            { required: true, message: '用户头像不能为空', trigger: 'blur' }
          ],
          gender: [
            { required: true, message: '性别 0：未知、1：男、2：女不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '类型 1-客户端用户 2-运维端用户不能为空', trigger: 'blur' }
          ],
          money: [
            { required: true, message: '累计消费金额不能为空', trigger: 'blur' }
          ],
          number: [
            { required: true, message: '累计使用次数不能为空', trigger: 'blur' }
          ],
          phoneNumber: [
            { required: true, message: '用户绑定的手机号（国外手机号会有区号）不能为空', trigger: 'blur' }
          ],
          mobile: [
            { required: true, message: '没有区号的手机号不能为空', trigger: 'blur' }
          ],
          countryCode: [
            { required: true, message: '区号不能为空', trigger: 'blur' }
          ],
          country: [
            { required: true, message: '用户所在国家不能为空', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '用户所在省份不能为空', trigger: 'blur' }
          ],
          city: [
            { required: true, message: '用户所在城市不能为空', trigger: 'blur' }
          ],
          brand: [
            { required: true, message: '手机品牌不能为空', trigger: 'blur' }
          ],
          model: [
            { required: true, message: '手机型号不能为空', trigger: 'blur' }
          ],
          version: [
            { required: true, message: '微信版本号不能为空', trigger: 'blur' }
          ],
          system: [
            { required: true, message: '操作系统版本不能为空', trigger: 'blur' }
          ],
          platform: [
            { required: true, message: '客户端平台不能为空', trigger: 'blur' }
          ],
          sdkVersion: [
            { required: true, message: '客户端基础库版本不能为空', trigger: 'blur' }
          ],
          ip: [
            { required: true, message: 'ip地址不能为空', trigger: 'blur' }
          ],
          latitude: [
            { required: true, message: '纬度不能为空', trigger: 'blur' }
          ],
          longitude: [
            { required: true, message: '经度不能为空', trigger: 'blur' }
          ],
          isAuth: [
            { required: true, message: '是否获取手机信息 1-未获取 2-已获取不能为空', trigger: 'blur' }
          ],
          isMobile: [
            { required: true, message: '是否获取过用户信息 1-未获取 2-已获取不能为空', trigger: 'blur' }
          ],
          isLocation: [
            { required: true, message: '是否授权过地理定位 1-否 2-是不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updatedAt: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          source: [
            { required: true, message: '来源 1 微信  2支付宝不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/bedside/bedsideuser/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.aliUserId = data.bedsideuser.aliUserId
                this.dataForm.openid = data.bedsideuser.openid
                this.dataForm.unionid = data.bedsideuser.unionid
                this.dataForm.nickname = data.bedsideuser.nickname
                this.dataForm.avatar = data.bedsideuser.avatar
                this.dataForm.gender = data.bedsideuser.gender
                this.dataForm.type = data.bedsideuser.type
                this.dataForm.money = data.bedsideuser.money
                this.dataForm.number = data.bedsideuser.number
                this.dataForm.phoneNumber = data.bedsideuser.phoneNumber
                this.dataForm.mobile = data.bedsideuser.mobile
                this.dataForm.countryCode = data.bedsideuser.countryCode
                this.dataForm.country = data.bedsideuser.country
                this.dataForm.province = data.bedsideuser.province
                this.dataForm.city = data.bedsideuser.city
                this.dataForm.brand = data.bedsideuser.brand
                this.dataForm.model = data.bedsideuser.model
                this.dataForm.version = data.bedsideuser.version
                this.dataForm.system = data.bedsideuser.system
                this.dataForm.platform = data.bedsideuser.platform
                this.dataForm.sdkVersion = data.bedsideuser.sdkVersion
                this.dataForm.ip = data.bedsideuser.ip
                this.dataForm.latitude = data.bedsideuser.latitude
                this.dataForm.longitude = data.bedsideuser.longitude
                this.dataForm.isAuth = data.bedsideuser.isAuth
                this.dataForm.isMobile = data.bedsideuser.isMobile
                this.dataForm.isLocation = data.bedsideuser.isLocation
                this.dataForm.createdAt = data.bedsideuser.createdAt
                this.dataForm.updatedAt = data.bedsideuser.updatedAt
                this.dataForm.source = data.bedsideuser.source
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
              url: this.$http.adornUrl(`/bedside/bedsideuser/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'aliUserId': this.dataForm.aliUserId,
                'openid': this.dataForm.openid,
                'unionid': this.dataForm.unionid,
                'nickname': this.dataForm.nickname,
                'avatar': this.dataForm.avatar,
                'gender': this.dataForm.gender,
                'type': this.dataForm.type,
                'money': this.dataForm.money,
                'number': this.dataForm.number,
                'phoneNumber': this.dataForm.phoneNumber,
                'mobile': this.dataForm.mobile,
                'countryCode': this.dataForm.countryCode,
                'country': this.dataForm.country,
                'province': this.dataForm.province,
                'city': this.dataForm.city,
                'brand': this.dataForm.brand,
                'model': this.dataForm.model,
                'version': this.dataForm.version,
                'system': this.dataForm.system,
                'platform': this.dataForm.platform,
                'sdkVersion': this.dataForm.sdkVersion,
                'ip': this.dataForm.ip,
                'latitude': this.dataForm.latitude,
                'longitude': this.dataForm.longitude,
                'isAuth': this.dataForm.isAuth,
                'isMobile': this.dataForm.isMobile,
                'isLocation': this.dataForm.isLocation,
                'createdAt': this.dataForm.createdAt,
                'updatedAt': this.dataForm.updatedAt,
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
