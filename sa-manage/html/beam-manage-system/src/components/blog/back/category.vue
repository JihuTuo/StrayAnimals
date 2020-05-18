<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input style="width: 120px" v-model="req.name" placeholder="分类名称"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增</el-button>
            </div>
            <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>


                <el-table-column
                    label="分类名称"
                    align="center"
                    prop="name">
                </el-table-column>
                <el-table-column
                    label="排序"
                    align="center"
                    prop="sort">
                </el-table-column>
                <el-table-column
                    width="160"
                    label="是否可用"
                    align="center">
                    <template slot-scope="scope">
                        <el-switch
                            v-model="scope.row.frozen"
                            :active-text="scope.row.frozen ? '可用' : '不可用'"
                            @change="changeStatus(scope.row.id, scope.row.frozen)">
                        </el-switch>
                    </template>
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
                        <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"
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
            <el-form ref="articleCategory" :model="articleCategory" label-width="100px">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model.trim="articleCategory.name"></el-input>
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                    <el-input-number  v-model.trim="articleCategory.sort"></el-input-number>
                </el-form-item>
                <el-form-item label="是否可用" prop="status">
                    <el-switch v-model="articleCategory.frozen" :active-text="articleCategory.frozen ? '可用' : '不可用'"></el-switch>
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
    </div>
</template>

<script>
    import ArticleCategoryApi from '../../../api/business/articlecategory';

    export default {
        name: 'basetable',
        data() {
            return {
                tableData: [],
                page: {pageNo: 1, pageSize: 20},
                multipleSelection: [],
                editVisible: false,
                delVisible: false,
                articleCategory: {},
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false
            }
        },
        created() {
            this.getData();
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
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                ArticleCategoryApi.getData(this.req).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.tableData = res.data.records ? res.data.records : []
                        this.page.pageNo = parseInt(res.data.current)
                        this.page.totalRows = parseInt(res.data.total)
                        this.tableData.forEach(item => {
                            item.frozen = Boolean(item.frozen)
                        })
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
                this.articleCategory = {
                    frozen:true
                };
                this.editVisible = true;
            },
            handleEdit(index, row) {
                this.editVisible = true;
                this.idx = index;
                const item = this.tableData[index];
                this.articleCategory = item;
            },
            handleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
            },
            delAll() {
                this.delVisible = true;
                this.ids = [];
                const length = this.multipleSelection.length;
                for (let i = 0; i < length; i++) {
                    this.ids.push(this.multipleSelection[i].id);
                }

            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            saveEdit() {
                this.loading = true
                ArticleCategoryApi.save(this.articleCategory).then((res) => {
                    this.loading = false
                    if (res.error === false) {
                        this.editVisible = false
                        this.$message.success(res.msg);
                        this.reload()
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                })

            },
            deleteRow() {
                ArticleCategoryApi.batchDelete(this.ids).then((res) => {
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
            // 上下架
            changeStatus(id, flag) {
                ArticleCategoryApi.changeStatus(id, !flag ? 0 : 1).then((res) => {
                    if (res.error === false) {
                        this.$message.success(res.msg);
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
