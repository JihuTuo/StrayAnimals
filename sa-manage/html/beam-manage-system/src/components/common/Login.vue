<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input autofocus="true" v-model="ruleForm.username" @keyup.enter.native="enterUsername"
                              placeholder="账户">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input ref="passwordRef" type="password" placeholder="密码" v-model="ruleForm.password"
                              @keyup.enter.native="enterPassword">
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item v-if="captchaOpen" prop="captcha">
                    <el-row style="height: 32px;">
                        <el-input style="width: 50%;" ref="captchaRef" placeholder="验证码" v-model="ruleForm.captcha"
                                  @keyup.enter.native="submitForm('ruleForm')">
                            <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                        </el-input>
                        <img class="captchaPic" alt="如果看不清楚，请单击图片刷新！" @click="refreshCode" :src="captchaSrc"/>
                    </el-row>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" v-loading="loading" @click="submitForm('ruleForm')">登录</el-button>
                </div>
                <p class="login-tips"></p>
            </el-form>
        </div>
    </div>
</template>

<script>
    import loginApi from '../../api/login';

    export default {
        data: function () {
            return {
                loading: false,
                captchaOpen: false,
                captchaSrc: "",
                ruleForm: {
                    username: 'test',
                    password: '123456',
                    captcha: '',
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                }
            }
        },
        created(){
            loginApi.getCaptchaOpen().then((res) => {
                this.loading = false
                if (res.error === false) {
                    this.captchaOpen = res.data;
                    if(this.captchaOpen){
                        this.captchaSrc = loginApi.getCaptchaSrc();
                    }
                } else {
                    this.$message.error(res.msg);
                }
            }, (err) => {
                this.loading = false
                this.$message.error(err.msg);
            })
        },
        methods: {
            enterUsername() {
                this.$refs.passwordRef.focus();
            },
            enterPassword() {
                if(this.captchaOpen){
                    this.$refs.captchaRef.focus();
                }
                else {
                    this.submitForm('ruleForm');
                }
            },
            refreshCode() {
                this.captchaSrc = loginApi.getCaptchaSrc();
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        loginApi.login(this.ruleForm).then((res) => {
                            this.loading = false
                            if (res.error === false) {
                                this.$message.success(res.msg);
                                localStorage.setItem('sysuser', JSON.stringify(res.data));
                                this.$router.push({path: '/dashboard'});
                            } else {
                                this.$message.error(res.msg);
                            }
                        }, (err) => {
                            this.loading = false
                            this.$message.error(err.msg);
                        })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .login-wrap {
        position: relative;
        width: 100%;
        height: 100%;
        /*background-image: url(../../assets/login-bg.jpg);*/
        background-size: 100%;
        background-color: #2d3a4b;
    }
    .ms-title {
        width: 100%;
        line-height: 50px;
        text-align: center;
        font-size: 20px;
        color: #fff;
        border-bottom: 1px solid #ddd;
    }
    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 350px;
        margin: -190px 0 0 -175px;
        border-radius: 5px;
        background: rgba(255, 255, 255, 0.3);
        overflow: hidden;
    }
    .ms-content {
        padding: 30px 30px;
    }
    .login-btn {
        text-align: center;
    }
    .login-btn button {
        width: 100%;
        height: 36px;
        margin-bottom: 10px;
    }
    .login-tips {
        font-size: 12px;
        line-height: 30px;
        color: #fff;
    }
    .captchaPic {
        width: 40%;
        height: 100%;
        float: right;
    }
</style>
