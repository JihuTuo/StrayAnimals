<template>
    <div id="blog" class="blog">
        <el-header style="padding: 0px;">
            <div class="header-box">
                <el-menu :default-active="menuActiveIndex"  class="menu-box" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="index">首页</el-menu-item>
                    <el-submenu  index="category">
                        <template slot="title">分类</template>
                        <el-menu-item v-for="category in categoryList" :key="category.id"  :index="category.id">
                            {{category.name}}（{{category.articleCount}}）
                        </el-menu-item>
                    </el-submenu>
                    <el-menu-item index="document">文档</el-menu-item>
                    <!--<el-menu-item index="tool">工具</el-menu-item>-->
                </el-menu>
            </div>
        </el-header>
        <el-container>
            <el-aside style="width: 20%;" class="aside-left-box hidden-xs-only">
                <el-card class="left-box-1">
                    <el-image src="http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png"></el-image>
                    <div style="font-size: 18px;font-weight: bold;text-align: center;">关注公众号</div>
                </el-card>
            </el-aside>
            <el-container >
                <router-view></router-view>
            </el-container>
            <el-aside style="width: 25%;" class="aside-right-box hidden-xs-only">
                <el-card class="right-box-1" :body-style="{width:'500px', padding: '14px'}" >
                    <div  class="category-item">
                        <a href="https://portal.qiniu.com/signup?code=1h8cpibemhb9u">七牛云每月10G免费空间与流量</a>
                    </div>
                    <div  class="category-item">
                        <a href="https://chuangke.aliyun.com/invite?userCode=647hkjjy" target="_blank"><img src="/static/img/hot-1.gif">阿里云8月爆款限时抢，力省11000+
                        </a>
                    </div>
                </el-card>
                <el-card  class="right-box-2" >
                    <div class="user-info">
                        <img style="width: 100px;height: 100px;" src="http://img.hsshy.cn/5f3cf4da-b38f-4b0c-be54-93e35a637056.png" class="user-avator" alt="">
                        <div class="user-info-cont">
                            <div >光有工具</div>
                            <div>图片文字识别、动植物识别、车型识别、二维码生成解析、手写板等工具</div>
                        </div>
                    </div>
                </el-card>

            </el-aside>


        </el-container>

    </div>
</template>

<script>
    import bus from '../../../api/bus';
    import BlogApi from '../../../api/business/blog';
    export default {
        data() {
            return {
                // menuActiveIndex:'index'
                categoryList:[],

            };
        },
        computed:{
            menuActiveIndex: function () {
                return this.$store.state.menuActiveIndex;
            },
            // menuActiveIndex: function () {
            //     通过中央事件总线插件vue-bus
            //     return this.indexValue;
            // }
        },
        created() {
            // bus.$on('changeMenuActiveIndex', key => {
            //     this.indexValue = key;
            // })
            this.getCategoryList();

        },
        methods: {
            handleSelect(key, keyPath) {
                // console.log(key)
                // console.log(keyPath)
                if (key == 'index') {
                    // this.menuActiveIndex = "index";
                    this.$store.commit('changeMenuActiveIndex','index');
                    this.$router.push({path: `/blog/index`})
                    bus.$emit('selectCategory','');
                }
                else if (key == 'document') {
                    this.$router.push({path: `/blog/document`})
                }
                // else if (key == 'tool') {
                //     this.$message.error("暂未开放");
                // }
                else if(keyPath[0]=='category'){
                    this.$router.push({path: `/blog/index`,query:{cid:keyPath[keyPath.length-1]}})
                }
            },
            getCategoryList(){
                BlogApi.getCategoryList().then((res) => {
                    if (res.error === false) {
                        this.categoryList = res.data;
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                })
            },

        }
    }
</script>

<style>
    #blog {
        height: 100%;
        overflow-y: auto;
        background: #f5f8f9;
    }

    .header-box {
        width: 100%;
        height: 60px;
        position: absolute;
        top: 0px;
        z-index: 1501;
        margin: 0px;
    }

    .menu-box {
        width: 100%;
    }

    .main-box {
        width: 100%;
        height: 100%;
    }
    .article-list {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        margin-bottom: 300px;
    }
    .article-item {
        width: 100%;
        margin-bottom: 10px;
        cursor: pointer;
    }
    .article-time {
        font-size: 13px;
        color: #999;
    }
    .article-tag-box {
        font-size: 13px;
        margin-left: 5%;
    }
    .article-tag {
        margin-left: 2px;
    }
    .views-count{
        font-size: 12px;
        color: #999;
        float: right;
    }
    .article-bottom {
        margin-top: 13px;
        line-height: 12px;
    }
    .aside-left-box {
        display: flex;
        justify-content: center;
        height: 100%;
    }
    .aside-right-box {
        display: flex;
        justify-content: center;
        flex-direction: column;
        height: 100%;
    }
    .right-box-1 {
        height: 80%;
        width: 80%;
        margin-top: 5%;
    }
    .right-box-2 {
        height: 80%;
        width: 80%;
        margin-top: 5%;
    }
    .right-box-3 {
        height: 80%;
        width: 80%;
        margin-top: 5%;
        display: flex;
        justify-content: center;
    }
    .left-box-1 {
        height: 80%;
        width: 100%;
        margin-top: 5%;
        display: flex;
        justify-content: center;
    }

    .category-item {
        font-size: 15px;
        padding: 10px 0;
        cursor: pointer;
    }
    .category-active {
        color: #38b7ea;
    }
    .category-item a{
        color: #38b7ea;
    }
    .user-info {
        display: flex;
        align-items: center;
        padding-bottom: 20px;
        border-bottom: 2px solid #ccc;
        margin-bottom: 20px;
    }
    .user-info-cont {
        padding-left: 50px;
        flex: 1;
        font-size: 14px;
        color: #999;
    }
</style>

