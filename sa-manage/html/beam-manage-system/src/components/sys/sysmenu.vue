<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 菜单管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">


                <el-input style="width: 150px" v-model="req.name" placeholder="请输入菜单名称"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <!--<el-button type="danger" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>-->
                <el-button v-if="canAdd" type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增</el-button>
            </div>
            <el-table row-key="id" :data="treeData" v-loading="loading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column
                    label="菜单名称"
                    align="center"
                    prop="name">
                </el-table-column>
                <el-table-column
                    label="菜单URL"
                    align="center"
                    prop="url">
                </el-table-column>
                <el-table-column
                    label="授权标识"
                    align="center"
                    prop="perms">
                </el-table-column>
                <el-table-column
                    label="类型 "
                    align="center"
                    prop="typeName">
                </el-table-column>
                <el-table-column
                    label="菜单图标"
                    align="center"
                    prop="icon">
                </el-table-column>
                <el-table-column
                    label="排序"
                    align="center"
                    prop="orderNum">
                </el-table-column>

                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button v-if="canEdit" type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button v-if="canDel" type="text" icon="el-icon-delete" class="red"
                                   @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>

                    </template>
                </el-table-column>
            </el-table>

        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="50%">

            <el-form ref="menu" :model="menu" label-width="100px">
                <el-form-item label="父菜单" prop="parentId">
                    <el-input @click.native="goToSelectMenu" readonly="readonly" v-model.trim="menu.pname"></el-input>
                </el-form-item>
                <el-form-item label="菜单名称" prop="name">
                    <el-input v-model.trim="menu.name"></el-input>
                </el-form-item>
                <el-form-item label="菜单URL" prop="url">
                    <el-input v-model.trim="menu.url"></el-input>
                </el-form-item>
                <el-form-item label="授权标识" prop="perms">
                    <el-input v-model.trim="menu.perms"></el-input>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-select v-model="menu.type" placeholder="请选择">
                        <el-option
                            v-for="item in menuType"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜单图标" prop="icon">
                    <el-input @click.native="goToSelectIcon" readonly="readonly" v-model.trim="menu.icon"></el-input>
                </el-form-item>
                <el-form-item label="排序" prop="orderNum">
                    <el-input v-model.trim="menu.orderNum"></el-input>
                </el-form-item>


            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 编辑弹出框 -->
        <el-dialog title="选择父级菜单" :modal="false" :visible.sync="selectMenuDialog" width="30%">
            <el-tree :data="treeData" :props="defaultProps" default-expand-all :expand-on-click-node="false" @node-click="selectMenuClick"></el-tree>
        </el-dialog>
        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 删除提示框 -->
        <el-dialog title="选择图标" :visible.sync="iconVisible" width="61%" center>
            <div>
                <div class="container">
                    <div class="search-box">
                        <el-input class="search" size="large" v-model="keyword" clearable placeholder="请输入图标名称"></el-input>
                    </div>
                    <ul>
                        <li @click="selectIcon(item)" class="icon-li" v-for="(item,index) in list" :key="index">
                            <div class="icon-li-content">
                                <i :class="`${item}`"></i>
                                <span>{{item}}</span>
                            </div>
                        </li>
                    </ul>
                </div>

            </div>
        </el-dialog>
    </div>
</template>

<script>
    import MenuApi from '../../api/sysmenu';
    export default {
        name: 'basetable',
        data() {
            return {
                selectMenuDialog:false,
                tableData: [],
                treeData: [],
                page: {pageNo: 1, pageSize: 20},
                multipleSelection: [],
                editVisible: false,
                delVisible: false,
                iconVisible: false,
                menu: {},
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                menuType:[
                    {value:0,name:"目录"},
                    {value:1,name:"菜单"},
                    {value:2,name:"按钮"}
                ],
                columns: [
                    {
                        text: '菜单名称',
                        value: 'name',
                        width: 200
                    },
                    {
                        text: 'url',
                        value: 'url'
                    },
                    {
                        text: '权限标识',
                        value: 'perms'
                    },
                    {
                        text: '菜单类型',
                        value: 'typeName'
                    },
                    {
                        text: '图标',
                        value: 'icon'
                    },
                    {
                        text: '排序',
                        value: 'orderNum'
                    }
                ],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                canEdit:true,
                canAdd:true,
                canDel:true,
                keyword: '',
                iconList: [
                    'el-icon-platform-eleme', 'el-icon-eleme', 'el-icon-delete-solid', 'el-icon-delete', 'el-icon-s-tools', 'el-icon-setting', 'el-icon-user-solid', 'el-icon-user', 'el-icon-phone', 'el-icon-phone-outline', 'el-icon-more', 'el-icon-more-outline', 'el-icon-star-on', 'el-icon-star-off', 'el-icon-s-goods', 'el-icon-goods', 'el-icon-warning', 'el-icon-warning-outline', 'el-icon-question', 'el-icon-info', 'el-icon-remove', 'el-icon-circle-plus', 'el-icon-success', 'el-icon-error', 'el-icon-zoom-in', 'el-icon-zoom-out', 'el-icon-remove-outline', 'el-icon-circle-plus-outline', 'el-icon-circle-check', 'el-icon-circle-close', 'el-icon-s-help', 'el-icon-help', 'el-icon-minus', 'el-icon-plus', 'el-icon-check', 'el-icon-close', 'el-icon-picture', 'el-icon-picture-outline', 'el-icon-picture-outline-round', 'el-icon-upload', 'el-icon-upload2', 'el-icon-download', 'el-icon-camera-solid', 'el-icon-camera', 'el-icon-video-camera-solid', 'el-icon-video-camera', 'el-icon-message-solid', 'el-icon-bell', 'el-icon-s-cooperation', 'el-icon-s-order', 'el-icon-s-platform', 'el-icon-s-fold', 'el-icon-s-unfold', 'el-icon-s-operation', 'el-icon-s-promotion', 'el-icon-s-home', 'el-icon-s-release', 'el-icon-s-ticket', 'el-icon-s-management', 'el-icon-s-open', 'el-icon-s-shop', 'el-icon-s-marketing', 'el-icon-s-flag', 'el-icon-s-comment', 'el-icon-s-finance', 'el-icon-s-claim', 'el-icon-s-custom', 'el-icon-s-opportunity', 'el-icon-s-data', 'el-icon-s-check', 'el-icon-s-grid', 'el-icon-menu', 'el-icon-share', 'el-icon-d-caret', 'el-icon-caret-left', 'el-icon-caret-right', 'el-icon-caret-bottom', 'el-icon-caret-top', 'el-icon-bottom-left', 'el-icon-bottom-right', 'el-icon-back', 'el-icon-right', 'el-icon-bottom', 'el-icon-top', 'el-icon-top-left', 'el-icon-top-right', 'el-icon-arrow-left', 'el-icon-arrow-right', 'el-icon-arrow-down', 'el-icon-arrow-up', 'el-icon-d-arrow-left', 'el-icon-d-arrow-right', 'el-icon-video-pause', 'el-icon-video-play', 'el-icon-refresh', 'el-icon-refresh-right', 'el-icon-refresh-left', 'el-icon-finished', 'el-icon-sort', 'el-icon-sort-up', 'el-icon-sort-down', 'el-icon-rank', 'el-icon-loading', 'el-icon-view', 'el-icon-c-scale-to-original', 'el-icon-date', 'el-icon-edit', 'el-icon-edit-outline', 'el-icon-folder', 'el-icon-folder-opened', 'el-icon-folder-add', 'el-icon-folder-remove', 'el-icon-folder-delete', 'el-icon-folder-checked', 'el-icon-tickets', 'el-icon-document-remove', 'el-icon-document-delete', 'el-icon-document-copy', 'el-icon-document-checked', 'el-icon-document', 'el-icon-document-add', 'el-icon-printer', 'el-icon-paperclip', 'el-icon-takeaway-box', 'el-icon-search', 'el-icon-monitor', 'el-icon-attract', 'el-icon-mobile', 'el-icon-scissors', 'el-icon-umbrella', 'el-icon-headset', 'el-icon-brush', 'el-icon-mouse', 'el-icon-coordinate', 'el-icon-magic-stick', 'el-icon-reading', 'el-icon-data-line', 'el-icon-data-board', 'el-icon-pie-chart', 'el-icon-data-analysis', 'el-icon-collection-tag', 'el-icon-film', 'el-icon-suitcase', 'el-icon-suitcase-1', 'el-icon-receiving', 'el-icon-collection', 'el-icon-files', 'el-icon-notebook-1', 'el-icon-notebook-2', 'el-icon-toilet-paper', 'el-icon-office-building', 'el-icon-school', 'el-icon-table-lamp', 'el-icon-house', 'el-icon-no-smoking', 'el-icon-smoking', 'el-icon-shopping-cart-full', 'el-icon-shopping-cart-1', 'el-icon-shopping-cart-2', 'el-icon-shopping-bag-1', 'el-icon-shopping-bag-2', 'el-icon-sold-out', 'el-icon-sell', 'el-icon-present', 'el-icon-box', 'el-icon-bank-card', 'el-icon-money', 'el-icon-coin', 'el-icon-wallet', 'el-icon-discount', 'el-icon-price-tag', 'el-icon-news', 'el-icon-guide', 'el-icon-male', 'el-icon-female', 'el-icon-thumb', 'el-icon-cpu', 'el-icon-link', 'el-icon-connection', 'el-icon-open', 'el-icon-turn-off', 'el-icon-set-up', 'el-icon-chat-round', 'el-icon-chat-line-round', 'el-icon-chat-square', 'el-icon-chat-dot-round', 'el-icon-chat-dot-square', 'el-icon-chat-line-square', 'el-icon-message', 'el-icon-postcard', 'el-icon-position', 'el-icon-turn-off-microphone', 'el-icon-microphone', 'el-icon-close-notification', 'el-icon-bangzhu', 'el-icon-time', 'el-icon-odometer', 'el-icon-crop', 'el-icon-aim', 'el-icon-switch-button', 'el-icon-full-screen', 'el-icon-copy-document', 'el-icon-mic', 'el-icon-stopwatch', 'el-icon-medal-1', 'el-icon-medal', 'el-icon-trophy', 'el-icon-trophy-1', 'el-icon-first-aid-kit', 'el-icon-discover', 'el-icon-place', 'el-icon-location', 'el-icon-location-outline', 'el-icon-location-information', 'el-icon-add-location', 'el-icon-delete-location', 'el-icon-map-location', 'el-icon-alarm-clock', 'el-icon-timer', 'el-icon-watch-1', 'el-icon-watch', 'el-icon-lock', 'el-icon-unlock', 'el-icon-key', 'el-icon-service', 'el-icon-mobile-phone', 'el-icon-bicycle', 'el-icon-truck', 'el-icon-ship', 'el-icon-basketball', 'el-icon-football', 'el-icon-soccer', 'el-icon-baseball', 'el-icon-wind-power', 'el-icon-light-rain', 'el-icon-lightning', 'el-icon-heavy-rain', 'el-icon-sunrise', 'el-icon-sunrise-1', 'el-icon-sunset', 'el-icon-sunny', 'el-icon-cloudy', 'el-icon-partly-cloudy', 'el-icon-cloudy-and-sunny', 'el-icon-moon', 'el-icon-moon-night', 'el-icon-dish', 'el-icon-dish-1', 'el-icon-food', 'el-icon-chicken', 'el-icon-fork-spoon', 'el-icon-knife-fork', 'el-icon-burger', 'el-icon-tableware', 'el-icon-sugar', 'el-icon-dessert', 'el-icon-ice-cream', 'el-icon-hot-water', 'el-icon-water-cup', 'el-icon-coffee-cup', 'el-icon-cold-drink', 'el-icon-goblet', 'el-icon-goblet-full', 'el-icon-goblet-square', 'el-icon-goblet-square-full', 'el-icon-refrigerator', 'el-icon-grape', 'el-icon-watermelon', 'el-icon-cherry', 'el-icon-apple', 'el-icon-pear', 'el-icon-orange', 'el-icon-coffee', 'el-icon-ice-tea', 'el-icon-ice-drink', 'el-icon-milk-tea', 'el-icon-potato-strips', 'el-icon-lollipop', 'el-icon-ice-cream-square', 'el-icon-ice-cream-round',
                ]

            }
        },
        created() {
            this.getTreeData();
            this.canEdit = this.getPerms().indexOf("sys:menu:edit")!=-1;
            this.canAdd = this.getPerms().indexOf("sys:menu:add")!=-1;
            this.canDel = this.getPerms().indexOf("sys:menu:del")!=-1;
        },
        computed: {
            list() {
                return this.iconList.filter((item) => {
                    return item.indexOf(this.keyword) !== -1;
                })
            }
        },
        methods: {
            selectIcon(item){
                this.iconVisible = false;
                this.menu.icon = item;
            },
            goToSelectMenu(){
                this.selectMenuDialog = true;
            },
            goToSelectIcon(){
                this.iconVisible = true;
            },
            selectMenuClick(data){
                this.selectMenuDialog = false;
                this.menu.parentId=data.id;
                this.menu.pname=data.name;
            },



            reload() {
                this.page.pageNo = 1
                this.getTreeData()
            },
            getTreeData() {
                this.loading = true;
                MenuApi.getTreeData(this.req).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.treeData = res.data;
                        this.wrapMenuType(this.treeData);

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
                this.getTreeData();
            },

            handleAdd() {
                this.menu = {};
                this.menu.delFlag = 0;
                this.editVisible = true;
            },

            handleEdit(index, row) {
                this.menu.delFlag = 0;

                MenuApi.info({menuId:row.id}).then((res) => {

                    if (res.error === false) {
                        this.menu = res.data;

                        console.log(this.menu);
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
                this.editVisible = true;


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
            // 保存编辑
            saveEdit() {
                // this.$set(this.tableData, this.idx, this.menu);
                this.loading = true
                MenuApi.save(this.menu).then((res) => {
                    this.loading = false
                    if (res.error === false) {
                        localStorage.removeItem('menuItems');
                        localStorage.removeItem('buttonItems');
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
                MenuApi.batchDelete(this.ids).then((res) => {
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
            wrapMenuType(treeData){
                treeData.forEach(item=>{
                    item.typeName = this.menuType[item.type].name;
                    if(item.children){
                        this.wrapMenuType(item.children);
                    }
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
    .example-p {
        height: 45px;
        display: flex;
        align-items: center;
    }

    .search-box {
        text-align: center;
        margin-top: 10px;
    }

    .search {
        width: 300px;
    }

    ul, li {
        list-style: none;
    }

    .icon-li {
        display: inline-block;
        padding: 10px;
        width: 120px;
        height: 120px;
    }

    .icon-li-content {
        display: flex;
        height: 100%;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }

    .icon-li-content i {
        font-size: 36px;
        color: #606266;
    }

    .icon-li-content span {
        margin-top: 10px;
        color: #787878;
    }
</style>
