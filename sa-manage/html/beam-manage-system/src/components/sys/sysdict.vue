<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">


                <el-input style="width: 120px" v-model="req.keyword" placeholder="请输入关键字"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <!--<el-button type="danger" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>-->
                <el-button v-if="canAdd" type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增</el-button>
            </div>
            <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>


                <el-table-column
                    label="名称"
                    align="center"
                    prop="name">
                </el-table-column>
                <el-table-column
                    label="描述"
                    align="center"
                    prop="des">
                </el-table-column>
                <el-table-column
                    label="编码"
                    align="center"
                    prop="code">
                </el-table-column>
                <el-table-column
                    label="排序"
                    align="center"
                    prop="sort">
                </el-table-column>
                <el-table-column
                    label="创建时间"
                    align="center"
                    prop="createTime">
                </el-table-column>
                <el-table-column
                    label="更新时间"
                    align="center"
                    prop="updateTime">
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" class="yellow" icon="el-icon-zoom-in"
                                   @click="handleView(scope.$index, scope.row)">查看字典值
                        </el-button>
                        <el-button v-if="canEdit" type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button v-if="canDel" type="text" icon="el-icon-delete" class="red"
                                   @click="handleDelete(scope.$index, scope.row)">删除
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

            <el-form ref="dict" :model="dict" :rules="rules" label-width="100px">
                <el-form-item label="父级字典" prop="pid">
                    <el-select filterable v-model="dict.pid" placeholder="请选择">
                        <el-option
                            v-for="item in dictList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名称" prop="name">
                    <el-input v-model.trim="dict.name"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="des">
                    <el-input v-model.trim="dict.des"></el-input>
                </el-form-item>
                <el-form-item label="编码" prop="code">
                    <el-input v-model.trim="dict.code"></el-input>
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                    <el-input v-model.trim="dict.sort"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveEdit">确 定</el-button>
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

        <el-dialog title="字典值" :visible.sync="subDictVisible">
            <el-table :data="subDictList">
                <el-table-column
                    label="名称"
                    align="center"
                    prop="name">
                </el-table-column>
                <el-table-column
                    label="描述"
                    align="center"
                    prop="des">
                </el-table-column>
                <el-table-column
                    label="编码"
                    align="center"
                    prop="code">
                </el-table-column>
                <el-table-column
                    label="排序"
                    align="center"
                    prop="sort">
                </el-table-column>
                <el-table-column
                    label="创建时间"
                    align="center"
                    prop="createTime">
                </el-table-column>
                <el-table-column
                    label="更新时间"
                    align="center"
                    prop="updateTime">
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button v-if="canEdit" type="text" icon="el-icon-edit" @click="subHandleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button v-if="canDel" type="text" icon="el-icon-delete" class="red"
                                   @click="subHandleDelete(scope.$index, scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </div>
</template>

<script>
    import DictApi from '../../api/sysdict';

    export default {
        name: 'basetable',
        data() {
            return {
                tableData: [],
                page: {pageNo: 1, pageSize: 20},
                multipleSelection: [],
                editVisible: false,
                delVisible: false,
                subDictVisible: false,
                dict: {},
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                rules: {
                    name: [
                        {required: true, message: '请填写字典名称', trigger: 'blur'},
                    ],
                    code: [
                        {required: true, message: '请填写字典编码', trigger: 'blur'},
                    ],
                },
                dictList: [],
                subDictList: [],
                mark: 0,
                pid: 0,
                canEdit:true,
                canAdd:true,
                canDel:true
            }
        },
        created() {
            this.getData();
            this.canEdit = this.getPerms().indexOf("sys:dict:edit")!=-1;
            this.canAdd = this.getPerms().indexOf("sys:dict:add")!=-1;
            this.canDel = this.getPerms().indexOf("sys:dict:del")!=-1;
        },
        computed: {},
        methods: {
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
            subReload() {
                this.getSubData();
            },
            getSubData() {
                DictApi.getData({pid: this.pid}).then((res) => {
                    if (res.error === false) {
                        this.subDictList = res.data.records ? res.data.records : []

                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                });
            },
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                DictApi.getData(this.req).then((res) => {
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
            // 获取 easy-mock 的模拟数据
            getDictList() {

                DictApi.getDictList().then((res) => {
                    if (res.error === false) {
                        this.dictList = res.data;
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                });
            },
            search() {
                this.page.pageNo = 1;
                this.getData();
            },

            handleAdd() {
                this.dict = {sort: 100};
                this.editVisible = true;
                this.getDictList();
            },
            handleEdit(index, row) {
                this.getDictList();
                this.editVisible = true;
                this.idx = index;
                const item = this.tableData[index];
                this.dict = item;
            },
            subHandleEdit(index, row) {
                this.getDictList();
                this.editVisible = true;
                this.idx = index;
                const item = this.subDictList[index];
                this.dict = item;
                this.mark = 1;
            },
            handleView(index, row) {
                this.subDictVisible = true;
                this.idx = index;
                this.dict = this.tableData[index];
                this.pid = this.dict.id;
                this.getSubData();

            },
            handleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
            },
            subHandleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
                this.mark = 1;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 保存编辑
            saveEdit() {
                // this.$set(this.tableData, this.idx, this.dict);
                this.loading = true
                DictApi.save(this.dict).then((res) => {
                    this.loading = false
                    if (res.error === false) {
                        this.editVisible = false
                        this.$message.success(res.msg);
                        if (this.mark == 1) {
                            this.subReload()
                            this.mark = 0;
                        } else {
                            this.reload()
                        }
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
                DictApi.batchDelete(this.ids).then((res) => {
                    if (res.error === false) {
                        this.$message.success(res.msg);
                        if(this.mark==1){
                            this.subReload();
                            this.mark=0;
                        }
                        else {
                            this.reload();
                        }
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

    .yellow {
        color: #e6a23c;
    }
</style>
