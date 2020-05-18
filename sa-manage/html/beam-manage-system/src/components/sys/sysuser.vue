<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 用户管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">

            <el-container>
                <el-aside width="150px">
                    <el-tree :data="deptTreeData" :props="defaultProps" default-expand-all :expand-on-click-node="false" @node-click="leftDeptClick"></el-tree>
                </el-aside>
                <el-container>
                    <el-header>
                            <el-input style="width: 120px" v-model="req.account" placeholder="请输入账号"></el-input>
                            <el-input style="width: 120px" v-model="req.name" placeholder="请输入姓名"></el-input>
                            <el-button type="primary" icon="search" @click="search">搜索</el-button>
                            <el-button v-if="canDel" type="danger" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                            <el-button v-if="canAdd" type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增用户</el-button>
                    </el-header>
                    <el-main>
                        <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                                  @selection-change="handleSelectionChange">
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column
                                label="用户头像"
                                min-width="120"
                                align="center">
                                <template v-if="scope.row.avatar" slot-scope="scope">
                                    <img :src="scope.row.avatar" style="width: 100px;height: 100px"/>
                                </template>
                            </el-table-column>
                            <el-table-column prop="account" align="center" label="账号">
                            </el-table-column>
                            <el-table-column prop="name" align="center" label="姓名">
                            </el-table-column>
                            <el-table-column prop="sexName" align="center" label="性别">
                            </el-table-column>
                            <el-table-column prop="deptName" align="center" label="部门名称">
                            </el-table-column>
                            <el-table-column prop="email" align="center" label="邮箱">
                            </el-table-column>
                            <el-table-column prop="phone" align="center" label="手机号">
                            </el-table-column>
                            <el-table-column prop="birthday" align="center" label="出生日期" sortable>
                            </el-table-column>
                            <el-table-column
                                width="160"
                                label="是否可用"
                                align="center">
                                <template slot-scope="scope">
                                    <el-switch
                                        v-model="scope.row.status"
                                        :active-text="scope.row.status ? '可用' : '不可用'"
                                        @change="changeStatus(scope.row.id, scope.row.status)">
                                    </el-switch>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" width="180" align="center">
                                <template slot-scope="scope">
                                    <el-button v-if="canEdit" type="text" icon="el-icon-edit"
                                               @click="handleEdit(scope.$index, scope.row)">编辑
                                    </el-button>
                                    <el-button v-if="canDel" type="text" icon="el-icon-delete" class="red"
                                               @click="handleDelete(scope.$index, scope.row)">删除
                                    </el-button>
                                    <el-button
                                        v-if="canResetPassword"
                                        type="text"
                                        icon="el-icon-refresh"
                                        class="warning"
                                        @click="handleResetPassword(scope.row.id)">重置密码
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pagination">
                            <el-pagination
                                background
                                :page-sizes="[10, 20, 30, 40, 50]"
                                :page-size="page.pageSize"
                                :current-page="page.pageNo"
                                @current-change="handleCurrentChange"
                                @size-change="changePageSize"
                                layout="prev, pager, next"
                                :total="page.totalRows">
                            </el-pagination>
                        </div>
                    </el-main>
                </el-container>
            </el-container>


        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="50%">

            <el-form ref="form" :model="form" label-width="100px">
                <el-upload
                    label=" 头像"
                    class="avatar-uploader el-dialog--center"
                    action="/beam_ht/file/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload">
                    <img v-if="form.avatar" :src="form.avatar" class="avatar" key="1"/>
                    <i v-else class="el-icon-plus avatar-uploader-icon" key="2"></i>
                </el-upload>
                <el-form-item label="账号">
                    <el-input :disabled="accountInput" v-model="form.account"></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>

                <el-form-item  label="选择角色">
                    <el-select v-model="form.roleIds" multiple placeholder="选择角色">
                        <el-option
                            v-for="item in roleList"
                            :key="item.id"
                            :label="item.roleName"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  label="部门名称">
                    <el-input @click.native="goToSelectDept" readonly="readonly" v-model="form.deptName"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="form.email"></el-input>
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input v-model="form.phone"></el-input>
                </el-form-item>
                <el-form-item label="出生日期">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.birthday" value-format="yyyy-MM-dd"
                                    style="width: 100%;"></el-date-picker>
                </el-form-item>
                <el-form-item label="是否可用" prop="status">
                    <el-switch v-model="form.status" :active-text="form.status ? '可用' : '不可用'"></el-switch>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-tag type="danger">新增的用户密码默认:123456</el-tag>
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 编辑弹出框 -->
        <el-dialog title="选择部门" :modal="false" :visible.sync="selectDeptDialog" width="30%">
            <el-tree :data="deptTreeData" :props="defaultProps" default-expand-all :expand-on-click-node="false" @node-click="selectDeptClick"></el-tree>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 100px;
        height: 100px;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 100px;
        height: 100px;
        line-height: 100px;
        text-align: center;
    }

    .avatar {
        width: 100px;
        height: 100px;
        display: block;
    }

    .warning {
        color: #E6A23C;
    }

    .red {
        color: #ff0000;
    }
</style>
<script>
    import SysUserApi from '../../api/sysuser';
    import DeptApi from '../../api/sysdept';
    export default {
        name: 'basetable',
        data() {
            return {
                selectDeptDialog:false,
                tableData: [],
                page: {pageNo: 1, pageSize: 20},
                multipleSelection: [],
                editVisible: false,
                delVisible: false,
                form: {
                    name: '',
                    account: '',
                    birthday: '',
                    email: '',
                    phone: '',
                    deptId:'',
                    roleIds:[]
                },
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                deptTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                roleList:[],
                canEdit:true,
                canAdd:true,
                canDel:true,
                canResetPassword:true,
                canChangePassword:true
            }
        },
        created() {
            this.getData();
            this.getDeptTreeData();
            this.canEdit = this.getPerms().indexOf("sys:user:edit")!=-1;
            this.canAdd = this.getPerms().indexOf("sys:user:add")!=-1;
            this.canDel = this.getPerms().indexOf("sys:user:del")!=-1;
            this.canResetPassword = this.getPerms().indexOf("sys:user:resetPassword")!=-1;
        },
        computed: {},
        methods: {
            getRoleList() {
                SysUserApi.getRoleList().then((res) => {
                    if (res.error === false) {
                        this.roleList = res.data;

                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            goToSelectDept(){
                this.selectDeptDialog = true;
            },
            selectDeptClick(data){
                this.selectDeptDialog = false;
                this.form.deptId=data.id;
                console.log(data);
                this.form.deptName=data.name;
            },
            leftDeptClick(data){
                let deptId = [];
                deptId.push(data.id);
                if(data.children!=''){
                    deptId = this.getChildrenDept(data.children,deptId);
                }
                this.req.deptIds = deptId.join(",");
                this.reload();
            },
            getChildrenDept(data,deptId){
                data.forEach(item=>{
                    deptId.push(item.id)
                    if(item.children!=''){
                        this.getChildrenDept(item.children,deptId);
                    }
                })

                return deptId;
            },
            handleAvatarSuccess(res, file) {

                console.log(res);
                console.log(file);
                if (res.error === false) {
                    // this.form.avatar = res.data;
                    this.$set(this.form, "avatar", res.data);
                    console.log(this.form);

                } else {
                    this.$message.error(res.msg);
                }
            },
            beforeAvatarUpload(file) {
                console.log(file.type)
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isJPG&&!isPNG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                    return false;
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                    return isLt2M;
                }
            },
            handleCurrentChange(val) {
                this.page.pageNo = val;
                this.getData();
            },
            changePageSize(value) { // 修改每页条数size
                this.page.pageNo = 1
                this.page.pageSize = value
                this.tableData = null
                this.getData()
            },
            reload() {
                this.page.pageNo = 1
                this.getData()
            },
            // 获取 easy-mock 的模拟数据
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                SysUserApi.getData(this.req).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.tableData = res.data.records ? res.data.records : []
                        this.page.pageNo = parseInt(res.data.current)
                        this.page.totalRows = parseInt(res.data.total)
                        this.tableData.forEach(item => {
                            item.status = Boolean(item.status)
                        })
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            getDeptTreeData() {
                DeptApi.getTreeData().then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.deptTreeData = res.data;

                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            search() {
                this.page.pageNo = 1;
                this.getData();
            },

            handleAdd() {
                this.form = {};
                this.editVisible = true;
                this.accountInput = false;
                this.form.status = true;
                this.getRoleList();
            },
            handleEdit(index, row) {
                this.idx = index;
                // const item = this.tableData[index];
                SysUserApi.info({userId:row.id}).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.form = res.data;
                        this.form.status = Boolean(this.form.status)
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
                this.getRoleList();
                this.editVisible = true;
                this.accountInput = true;
            },
            handleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
            },
            delAll() {
                this.ids = [];
                const length = this.multipleSelection.length;
                for (let i = 0; i < length; i++) {
                    this.ids.push(this.multipleSelection[i].id);
                }

                if(this.ids.length<=0){
                    this.$message.error("请选择要操作的记录");
                    return false;
                }

                this.delVisible = true;


            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 保存编辑
            saveEdit() {
                // this.$set(this.tableData, this.idx, this.form);
                this.loading = true
                SysUserApi.save(this.form).then((res) => {
                    this.loading = false
                    if (res.error === false) {
                        this.editVisible = false
                        this.$message.success(res.msg);
                        this.reload()
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false
                    this.$message.error(err.msg);
                })

            },
            // 确定删除
            deleteRow() {
                SysUserApi.batchDelete(this.ids).then((res) => {
                    if (res.error === false) {
                        this.$message.success(res.msg);
                        this.reload()
                    } else {
                        this.$message.error(res.msg);
                    }

                }, (err) => {
                    this.$message.error(err.msg);
                })
                this.delVisible = false;
            },
            handleResetPassword(id) {
                let ids = [id]
                this.$confirm('是否确认将密码重置为：123456？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    SysUserApi.resetPassword(ids).then((res) => {
                        if (res.error === false) {
                            this.$message.success(res.msg);
                            this.reload()
                        } else {
                            this.$message.error(res.msg);
                        }

                    }, (err) => {
                        this.$message.error(err.msg);
                    })
                })
            },
            // 上下架
            changeStatus(id, flag) {
                SysUserApi.changeStatus(id, !flag ? 0 : 1).then((res) => {
                    if (res.error === false) {
                        this.$message.success('操作成功');
                        this.reload()
                    } else {
                        this.$message.error(err.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                })
            }
        }
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .red {
        color: #ff0000;
    }
</style>
