<template>
    <div style="width: 100%;">
        <div class="main-box">
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
            <el-backtop target=".blog" :bottom="5">
            </el-backtop>
        </div>
    </div>

</template>
<script>
    import 'element-ui/lib/theme-chalk/display.css';
    import {mavonEditor} from 'mavon-editor'
    import 'mavon-editor/dist/css/index.css'
    import BlogApi from '../../../api/business/blog';

    export default {
        components: {
            mavonEditor
        },
        data() {
            return {//value的值是经过markdown解析后的文本，可使用`@change="changeData"`在控制台打印显示
                defaultData: "preview",
                article: {},
                codeStyle: "agate",
            };
        },
        created() {
            this.getArticleInfo('000eZ9dw');
            this.addReadNum('000eZ9dw');
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
