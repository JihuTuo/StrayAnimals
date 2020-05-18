<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 登陆日志</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input style="width: 120px" v-model="req.userName" placeholder="用户名称"></el-input>
                <el-input style="width: 120px" v-model="req.succeed" placeholder="是否成功"></el-input>
                <el-input style="width: 120px" v-model="req.ipAddress" placeholder="ip"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button v-if="canClear" type="danger" icon="delete" class="handle-del mr10" @click="delVisible=true">清空日志</el-button>
            </div>
            <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable">
                <el-table-column type="selection" width="55" align="center"></el-table-column>

                <el-table-column
                    label="日志名称"
                    align="center"
                    prop="logName">
                </el-table-column>
                <el-table-column
                    label="用户名称"
                    align="center"
                    prop="userName">
                </el-table-column>
                <el-table-column
                    label="是否成功"
                    align="center"
                    prop="succeed">
                </el-table-column>
                <el-table-column
                    label="消息"
                    align="center"
                    prop="message">
                </el-table-column>
                <el-table-column
                    label="ip"
                    align="center"
                    prop="ipAddress">
                </el-table-column>
                <el-table-column
                    label="创建时间"
                    align="center"
                    prop="createTime">
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


        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">清空后不可恢复，是否确定清空？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="clearAll">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import Http from '../../util/http';

    export default {
        name: 'basetable',
        data() {
            return {
                tableData: [],
                page: {pageNo: 1, pageSize: 20},
                delVisible: false,
                loginLog: {},
                req: {},
                loading: false,
                canClear:true
            }
        },
        created() {
            this.getData();
            this.canClear = this.getPerms().indexOf("sys:loginLog:clear")!=-1;
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
            // 获取 easy-mock 的模拟数据
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                Http.get("/sys/loginlog/page/list",this.req).then((res) => {
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
            search() {
                this.page.pageNo = 1;
                this.getData();
            },



            clearAll() {
                Http.post("/sys/loginlog/clear",this.reqs).then((res) => {
                    this.delVisible = false;
                    this.$message.success(res.msg);
                    this.reload();
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });

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
</style>
