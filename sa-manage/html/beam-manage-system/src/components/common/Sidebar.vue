<template>
    <div class="sidebar">
        <!--<el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"-->
        <!--text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>-->
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse"
             unique-opened router>
            <template v-for="item in menuItems">
                <template v-if="item.list">
                    <el-submenu :index="item.url" :key="item.url">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.name }}</span>
                        </template>
                        <template v-for="subItem in item.list">
                            <el-submenu v-if="subItem.list" :index="subItem.url" :key="subItem.url">
                                <template slot="title"><i :class="subItem.icon"></i><span slot="title">{{ subItem.name }}</span>{{ subItem.name }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.list" :key="i" :index="threeItem.url">
                                    {{ threeItem.name }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.url" :key="subItem.url">
                                <i :class="subItem.icon"></i><span slot="title">{{ subItem.name }}</span>
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.url" :key="item.url">
                        <i :class="item.icon"></i><span slot="title">{{ item.name }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from '../../api/bus';
    import AccountApi from '../../api/account';
    export default {
        data() {
            return {
                collapse: false,
                menuItems:[],
                buttonItems:[]
            }
        },
        computed:{
            onRoutes(){
                // return this.$route.path.replace('/','');
            }
        },
        created(){
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
            this.getNavList();
            this.getButtonList();
        },
        methods:{
            getNavList(){
                AccountApi.getNavList().then((res)=>{
                        if(res.error==false){
                            this.menuItems = res.data;
                            localStorage.setItem("menuItems",res.data);
                        }
                    },
                    (err) => {
                    this.menuItems = []
                    this.$message.error(err.msg);
                })
            },
            getButtonList(){
                AccountApi.getButtonList().then((res)=>{
                        if(res.error==false){
                            this.buttonItems = res.data;
                            localStorage.setItem("buttonItems",res.data);
                        }

                    },
                    (err) => {
                        this.buttonItems = []
                        this.$message.error(err.msg);
                    })
            }
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom:0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 250px;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
