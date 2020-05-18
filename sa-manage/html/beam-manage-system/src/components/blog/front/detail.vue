<template>
    <div style="width: 100%;">
        <div class="main-box">
            <div class="article-title">
                <h2>{{article.title}}</h2>
            </div>
            <div class="article-author">
                <a class="avatar">
                    <img v-if="article.headImg!=undefined" :src="article.headImg" alt="96">
                </a>
                <div class="info">
                    <span class="name">{{article.author}}</span>
                    <div class="meta">
                        <span class="publish-time">{{article.createTime}}</span>
                        <span v-if="article.readNum!=undefined" class="views-count">阅读 {{article.readNum}}</span>
                    </div>

                </div>
            </div>
            <div class="article-content">
                <mavon-editor v-if="article.textType==0" v-model="article.content" :subfield="false"
                              :defaultOpen="defaultData"
                              :toolbarsFlag="false"
                              :codeStyle="codeStyle"
                              :ishljs="true"
                              :boxShadow="true"/>
                <div class="edit-container" v-if="article.textType==1">
                    <div class="edit-content" v-html="article.content">
                    </div>
                </div>
                <div style="width:100%;height: 30px;">
                </div>
            </div>
        </div>
        <el-backtop target=".blog" :bottom="5">
        </el-backtop>
    </div>

</template>
<script>
    import 'element-ui/lib/theme-chalk/display.css';
    import {mavonEditor} from 'mavon-editor'
    import 'mavon-editor/dist/css/index.css'
    import BlogApi from '../../../api/business/blog';
    // import bus from '../../common/bus';
    export default {
        components: {
            mavonEditor
        },
        data() {
            return {//value的值是经过markdown解析后的文本，可使用`@change="changeData"`在控制台打印显示
                defaultData: "preview",
                article: {},
                codeStyle:"agate",
            };
        },
        created() {
            // bus.$emit('changeMenuActiveIndex', "");

            this.$store.commit('changeMenuActiveIndex','');
            if (this.$route.params.shortCode) {
                this.getArticleInfo(this.$route.params.shortCode);
                this.addReadNum(this.$route.params.shortCode);
            }
        },
        watch: {
            $route() {
                // bus.$emit('changeMenuActiveIndex', "");
                this.$store.commit('changeMenuActiveIndex','');
                if (this.$route.params.shortCode) {
                    this.getArticleInfo(this.$route.params.shortCode);
                    this.addReadNum(this.$route.params.shortCode);
                }
            },
        },

        methods: {
            getArticleInfo(shortCode) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                BlogApi.getArticleInfo(shortCode).then((res) => {
                    if (res.error === false) {
                        loading.close();
                        this.article = res.data;
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false
                    this.$message.error(err.msg);
                })
            },
            handleSelect(key, keyPath) {
                if (key == 'index') {
                    this.$router.push({path: `/blog/index`})
                }
            },
            addReadNum(shortCode) {
                BlogApi.addReadNum(shortCode).then((res) => {
                }, (err) => {
                    this.$message.error(err.msg);
                });
            },
        }
    };
</script>

<style scoped>

    .main-box {
        width: 100%;
        height: 100%;
        margin-top: 20px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
    }

    .article-title {
        width: 60%;
        display: flex;
        justify-content: center;
    }

    .article-author {
        margin: 30px 0 40px;
    }

    .article-author .avatar {
        width: 48px;
        height: 48px;
        vertical-align: middle;
        display: inline-block;
    }

    .article-author .avatar img {
        width: 100%;
        height: 100%;
        border: 1px solid #ddd;
        border-radius: 50%;
    }

    .article-author .name {
        margin-right: 3px;
        font-size: 16px;
        vertical-align: middle;
    }

    .article-author .meta {
        margin-top: 5px;
        font-size: 12px;
        color: #969696;
        vertical-align: top;
    }

    .article-author .meta .publish-time {
        vertical-align: middle;
    }

    .article-author .meta .views-count {
        margin-left: 5px;
        font-size: 12px;
        vertical-align: middle;
    }

    .article-author .info {
        vertical-align: middle;
        display: inline-block;
        margin-left: 8px;
    }

    .article-content {
        width: 90%;
        height: 100%;
    }

    .edit-container {
        width: 100%;
        height: 100%;
        border: 1px solid #e0e0e0;
        box-sizing: border-box;
        background: #fbfbfb;
    }

    .edit-content {
        padding: 8px 25px 15px 25px;
        box-sizing: border-box;
    }
</style>
