<template>
    <el-main style="overflow-y: hidden;" class="main-box">
        <div class="article-list">
            <div class="article-item" @click="goToDetail(article.shortCode)" v-for="article in articleList">
                <el-card :body-style="{width:'100%',height:'50%', padding: '0px'}">
                    <div style="padding: 14px;">
                        <span>{{article.title}}</span>
                        <div class="article-bottom clearfix">
                            <time class="article-time">{{article.createTime}}</time>
                            <span class="article-tag-box">
                                            <el-tag v-if="index<=5&&article.tags.length>0" class="article-tag"
                                                    size="mini" :type="colorList[index]" :key="tag"
                                                    v-for="(tag,index) in article.tags">{{tag}}</el-tag>
                                            </span>
                            <span class="views-count">阅读 {{article.readNum}}</span>
                        </div>
                    </div>
                </el-card>
            </div>
        </div>
    </el-main>

</template>

<script>
    import BlogApi from '../../../api/business/blog';
    import 'element-ui/lib/theme-chalk/display.css';
    import bus from '../../../api/bus';

    export default {
        name: "index",
        data() {
            return {
                activeIndex: 'index',
                articleList: [],
                req: [],
                categoryList: [],
                colorList: ["danger", "warning", "success", "info", ""]
            };
        },
        created() {
            this.selectCategory(this.$route.query.cid);

            // this.getCategoryList();
        },
        watch: {
            $route() {
                this.selectCategory(this.$route.query.cid);

            },
        },
        methods: {
            getArticleList() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                BlogApi.getArticleList(this.req).then((res) => {
                    if (res.error === false) {
                        loading.close();
                        this.articleList = res.data ? res.data : []
                        this.articleList.forEach(article => {
                            if (article.tag) {
                                article.tags = article.tag.split(',');
                            }
                        })
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            getCategoryList() {
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
            handleSelect(key, keyPath) {
                if (key == 'index') {
                    this.$router.push({path: `/blog/index`})
                } else if (key == 'about') {
                    this.$message.error("暂未开放");
                } else if (key == 'friendLink') {
                    this.$message.error("暂未开放");
                } else if (key == 'tool') {
                    this.$message.error("暂未开放");
                }
            },
            goToDetail(shortCode) {
                this.$router.push({path: `/blog/detail/${shortCode}`})
            },
            selectCategory(cid) {
                this.req.cid = cid;
                this.getArticleList();
            }
        }
    }
</script>

<style>
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

    .views-count {
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

    .category-item a {
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
