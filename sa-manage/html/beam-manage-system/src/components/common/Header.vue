<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i class="el-icon-menu"></i>
        </div>
        <div class="logo">后台管理系统</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 全屏显示 -->
                <div class="btn-fullscreen" @click="handleFullScreen">
                    <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                        <i class="el-icon-rank"></i>
                    </el-tooltip>
                </div>

                <!-- 用户头像 -->
                <div class="user-avator">
                    <img v-if="sysuser.avatar" :src="sysuser.avatar">
                    <img v-else src="/static/img/user-head-img.png">
                </div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{sysuser.account}} <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <a href="https://www.jianshu.com/u/e2fcc1cdaca1" target="_blank">
                            <el-dropdown-item>作者简书</el-dropdown-item>
                        </a>
                        <a href="https://gitee.com/hsshy/beam-parent" target="_blank">
                            <el-dropdown-item>项目仓库</el-dropdown-item>
                        </a>
                        <el-dropdown-item divided v-if="canChangePassword" command="changePassword">修改密码
                        </el-dropdown-item>
                        <el-dropdown-item divided v-if="canClearCache" command="clearCache">清除缓存</el-dropdown-item>
                        <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
        <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
            <el-form :model="pwdForm" ref="AccountForm" :rules="rules" label-width="80px">
                <el-form-item label="旧密码" prop="oldPwd">
                    <el-input type="password" v-model.trim="pwdForm.oldPwd" auto-complete="off"
                              placeholder="旧密码"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPwd">
                    <el-input type="password" v-model.trim="pwdForm.newPwd" auto-complete="off"
                              placeholder="新密码"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="password_confirm">
                    <el-input type="password" v-model.trim="pwdForm.password_confirm" placeholder="确认密码"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false" size="mini">取 消</el-button>
                <el-button type="primary" @click="modifyPwd()" :loading="loading" size="mini">确 定</el-button>
            </div>
        </el-dialog>
    </div>

</template>
<script>
    import bus from '../../api/bus';
    import AccountApi from '../../api/account';

    export default {

        data() {
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.pwdForm.newPwd) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                collapse: false,
                fullscreen: false,
                name: '',
                message: 2,
                user: null,
                loading: false,
                dialogFormVisible: false,
                pwdForm: {
                    oldPwd: "",
                    newPwd: "",
                    password_confirm: ""
                },
                rules: {
                    oldPwd: [
                        {required: true, message: '请输入旧密码', trigger: 'blur'},
                    ],
                    newPwd: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                    ],
                    password_confirm: [
                        {validator: validatePass, trigger: 'blur'}
                    ]
                },
                canChangePassword: true,
                canClearCache: true
            }
        },
        computed: {
            username() {
                return this.name;
            },
            sysuser() {
                let sysuser = JSON.parse(localStorage.getItem('sysuser'));

                return sysuser ? sysuser : this.user;
            }

        },
        methods: {
            getNavList() {
                AccountApi.getNavList().then((res) => {
                        if (res.error == false) {
                            this.menuItems = res.data;
                            localStorage.setItem("menuItems", res.data);
                        }
                    },
                    (err) => {
                        this.menuItems = []
                        this.$message.error(err.msg);
                    })
            },
            getButtonList(isRefresh) {
                AccountApi.getButtonList().then((res) => {
                        if (res.error == false) {
                            this.buttonItems = res.data;
                            localStorage.setItem("buttonItems", res.data);
                            console.log(res.data);
                            if(isRefresh){
                                window.location.reload();
                            }
                        }

                    },
                    (err) => {
                        this.buttonItems = []
                        this.$message.error(err.msg);
                    })
            },
            // 修改密码
            modifyPwd() {
                this.$refs.AccountForm.validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        AccountApi.modifyPwd(this.pwdForm).then((res) => {
                            this.loading = false;
                            if (res.error === false) {
                                this.dialogFormVisible = false;
                                this.$message.success(res.msg);
                            } else {
                                this.$message.error(res.msg);
                            }
                        }, (err) => {
                            this.loading = false;
                            this.$message.error(err.msg);
                        })
                    }
                });
            },
            // 用户名下拉菜单选择事件
            handleCommand(command) {
                if (command == 'loginout') {
                    this.handleLogout();
                }
                if (command == 'clearCache') {
                    this.clearCache();
                }
                if (command == 'changePassword') {
                    this.dialogFormVisible = true;
                }
            },
            handleLogout() {
                AccountApi.handleLogout().then((res) => {
                    localStorage.removeItem('sysuser')
                    this.$router.push('/login');
                }, (err) => {
                    this.$message.error(err.msg);
                })
            },
            clearCache() {

                AccountApi.clearCache().then((res) => {
                    // bus.$emit('closeAll', "");
                    localStorage.removeItem('menuItems');
                    localStorage.removeItem('buttonItems');
                    let that = this;
                    this.$message({
                        showClose: true,
                        message: '清除成功，正在刷新页面...',
                        type: 'true',
                        duration: 1000,
                        onClose: function () {
                            that.getNavList();
                            that.getButtonList(false);
                        }
                    });
                }, (err) => {
                    this.$message.error(err.msg);
                })
            },
            created() {
                this.canClearCache = this.getPerms().indexOf("sys:user:clearCache") != -1;
                this.canChangePassword = this.getPerms().indexOf("sys:user:changePassword") != -1;
            },
            // 侧边栏折叠
            collapseChage() {
                this.collapse = !this.collapse;
                bus.$emit('collapse', this.collapse);
            },
            // 全屏事件
            handleFullScreen() {
                let element = document.documentElement;
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                this.fullscreen = !this.fullscreen;
            }
        },
        mounted() {
            if (document.body.clientWidth < 1500) {
                this.collapseChage();
            }
        }
    }
</script>
<style scoped>
    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 70px;
        font-size: 22px;
        color: #fff;
    }

    .collapse-btn {
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 70px;
    }

    .header .logo {
        float: left;
        width: 250px;
        line-height: 70px;
    }

    .header-right {
        float: right;
        padding-right: 50px;
    }

    .header-user-con {
        display: flex;
        height: 70px;
        align-items: center;
    }

    .btn-fullscreen {
        transform: rotate(45deg);
        margin-right: 5px;
        font-size: 24px;
    }

    .btn-bell, .btn-fullscreen {
        position: relative;
        width: 30px;
        height: 30px;
        text-align: center;
        border-radius: 15px;
        cursor: pointer;
    }

    .btn-bell-badge {
        position: absolute;
        right: 0;
        top: -2px;
        width: 8px;
        height: 8px;
        border-radius: 4px;
        background: #f56c6c;
        color: #fff;
    }

    .btn-bell .el-icon-bell {
        color: #fff;
    }

    .user-name {
        margin-left: 10px;
    }

    .user-avator {
        margin-left: 20px;
    }

    .user-avator img {
        display: block;
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .el-dropdown-link {
        color: #fff;
        cursor: pointer;
    }

    .el-dropdown-menu__item {
        text-align: center;
    }
</style>
