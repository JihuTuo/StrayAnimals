<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 角色管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input style="width: 130px" v-model="req.roleName" placeholder="请输入角色名称"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button v-if="canDel" type="danger" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-button v-if="canAdd" type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增</el-button>
                <el-button v-if="canExport" type="warning"  class="handle-del mr10" @click="handleExport">导出</el-button>
            </div>
            <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column
                    label="角色名称"
                    align="center"
                    prop="roleName">
                </el-table-column>
                <el-table-column
                    label="备注"
                    align="center"
                    prop="remark">
                </el-table-column>

                <el-table-column
                    label="创建时间"
                    align="center"
                    prop="createTime">
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button v-if="canEdit" type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button v-if="canDel" type="text" icon="el-icon-delete" class="red"
                                   @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>
                        <el-button v-if="canConfigPerm" class="success" type="text" icon="el-icon-lx-lock" @click="handleConfigPerms(scope.$index, scope.row)">权限配置
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
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">

            <el-form ref="role" :model="role" label-width="100px">
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model.trim="role.roleName"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model.trim="role.remark"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 编辑弹出框 -->
        <el-dialog title="权限配置" :modal="false" :visible.sync="configMenuDialog" width="30%">
            <el-tree  v-loading="loading" show-checkbox node-key="id" :data="menuTreeData" :default-checked-keys="checkMenuData" :props="defaultProps"  ref="treeMenu" default-expand-all :expand-on-click-node="false" ></el-tree>
            <span slot="footer" class="dialog-footer">
                <el-button @click="configMenuDialog = false">取 消</el-button>
                <el-button type="primary" @click="saveMuenPerms">确 定</el-button>
            </span>
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

<script>
    import RoleApi from '../../api/sysrole';
    import http from '@/util/http'

    export default {
        name: 'basetable',
        data() {
            return {
                configMenuDialog:false,
                tableData: [],
                page: {pageNo: 1, pageSize: 20},
                multipleSelection: [],
                editVisible: false,
                delVisible: false,
                role: {},
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                menuTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkMenuData:[],
                roleId:null,
                canEdit:true,
                canAdd:true,
                canDel:true,
                canConfigPerm:true,
                canExport:true,

            }
        },
        created() {
            this.getData();
            this.canEdit = this.getPerms().indexOf("sys:role:edit")!=-1;
            this.canAdd = this.getPerms().indexOf("sys:role:add")!=-1;
            this.canDel = this.getPerms().indexOf("sys:role:del")!=-1;
            this.canConfigPerm = this.getPerms().indexOf("sys:role:configPerm")!=-1;
            this.canExport = this.getPerms().indexOf("sys:role:export")!=-1;
        },
        computed: {},
        methods: {
            handleExport(){
                this.roleIds = [];
                const length = this.multipleSelection.length;
                for (let i = 0; i < length; i++) {
                    this.menuIds.push(this.multipleSelection[i].id);
                }
                let url = http.getBaseUrl()+"/sys/role/export";
                let param = this.$utils.queryParams(this.req,"?");
                param = param + "&roleIds="+this.roleIds;
                window.location.href = url+param;
            },
            saveMuenPerms(){
                this.checkMenuData = [];
                this.checkMenuData = this.checkMenuData.concat(this.$refs.treeMenu.getCheckedKeys());
                this.checkMenuData = this.checkMenuData.concat(this.$refs.treeMenu.getHalfCheckedKeys());
                RoleApi.saveMuenPerms({id:this.roleId,menuIds:this.checkMenuData}).then((res) => {
                    this.configMenuDialog = false;
                    this.checkMenuData = [];
                    if (res.error === false) {
                        this.$message.success(res.msg);
                        this.reload();
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.checkMenuData = [];
                    this.loading = false;
                    this.$message.error(err.msg);
                });
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
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                RoleApi.getData(this.req).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.tableData = res.data.records ? res.data.records : []
                        this.page.pageNo = parseInt(res.data.current)
                        this.page.totalRows = parseInt(res.data.total)
                        this.tableData.forEach(item => {
                            item.status = Boolean(item.status)
                        })
                    } else {
                        console.log(res);
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            getMenuTreeData() {
                RoleApi.getMenuTreeData().then((res) => {
                    if (res.error === false) {
                        this.menuTreeData = res.data;
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            getCheckMenuData(roleId) {
                RoleApi.getCheckMenuData({roleId:roleId}).then((res) => {
                    if (res.error === false) {
                        this.checkMenuData = res.data;
                        this.loading = false;

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
                this.role = {};
                this.editVisible = true;
            },
            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                this.role = item;
                this.editVisible=true;

            },
            handleConfigPerms(index, row) {
                this.loading=true;
                this.roleId = row.id;
                this.checkMenuData=[];
                this.menuTreeData=[];
                this.getMenuTreeData();
                this.getCheckMenuData(this.roleId);
                this.configMenuDialog = true;

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
                // this.$set(this.tableData, this.idx, this.role);
                this.loading = true
                RoleApi.save(this.role).then((res) => {
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
                RoleApi.batchDelete(this.ids).then((res) => {
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
    .success {
        color: #67C23A;
    }
</style>
