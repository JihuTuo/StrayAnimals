<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 文章管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="common-add-content goods-add-content">

                <div class="content__main">
                    <div class="section">
                        <div class="section-content">
                            <el-form ref="articleForm"  :rules="rules" :model="article" label-width="100px">
                                <el-form-item label="文章标题" prop="title">
                                    <el-input style="width: 500px;" v-model.trim="article.title"></el-input>
                                </el-form-item>
                                <el-form-item label="作者" prop="author">
                                    <el-input style="width: 150px;" v-model.trim="article.author"></el-input>
                                </el-form-item>
                                <el-form-item label="文章分类" prop="cids">
                                    <el-select v-model="article.cids" multiple placeholder="请选择">
                                        <el-option
                                            v-for="item in categoryList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="文章标签" prop="tag">
                                    <el-tag
                                        :key="tag"
                                        v-for="tag in tags"
                                        closable
                                        :disable-transitions="false"
                                        @close="handleClose(tag)">
                                        {{tag}}
                                    </el-tag>
                                    <el-input
                                        class="input-new-tag"
                                        v-model="tagValue"
                                        ref="saveTagInput"
                                        size="small"
                                        @keyup.enter.native="handleInputConfirm"
                                        @blur="handleInputConfirm"
                                        v-if="tagVisible"
                                    >
                                    </el-input>
                                    <el-button v-else class="button-new-tag" size="small" @click="showTagInput">添加标签</el-button>
                                </el-form-item>
                                <el-form-item label="排序" prop="sort">
                                    <el-input-number  v-model.trim="article.sort"></el-input-number>
                                </el-form-item>
                                <el-form-item label="是否可用" prop="status">
                                    <el-switch v-model="article.frozen" :active-text="article.frozen ? '可用' : '不可用'"></el-switch>
                                </el-form-item>

                                <el-form-item label="文本类型" prop="textType">
                                    <el-radio-group v-model="article.textType">
                                        <el-radio :label="0">Markdown</el-radio>
                                        <el-radio :label="1">富文本</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item v-if="article.textType==0" label="内容" prop="content">
                                    <mavon-editor ref="myMavonEditor" :subfield="false" @save="$saveContent" @imgAdd="$imgAdd"  defaultOpen="edit" v-model="article.content"/>
                                </el-form-item>
                                <el-form-item v-if="article.textType==1" label="内容" prop="content">
                                    <quill-editor ref="myTextEditor" v-model="article.content" :options="editorOption"></quill-editor>
                                </el-form-item>

                            </el-form>
                        </div>
                    </div>
                    <div class="next-footer">
                        <el-button size="small" type="primary" @click="saveArticle">保存</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import ArticleApi from '../../../api/business/article';
    import {mavonEditor} from 'mavon-editor'
    import 'mavon-editor/dist/css/index.css'
    import {quillRedefine} from 'vue-quill-editor-upload'//引入图片上传
    import 'quill/dist/quill.core.css';
    import 'quill/dist/quill.snow.css';
    import 'quill/dist/quill.bubble.css';
    import {quillEditor} from 'vue-quill-editor';
    import bus from '../../../api/bus';

    export default {
        data(){
            return{
                article: {},
                authorName:"yellow_shy",
                authorHeadImg:"http://img.hsshy.cn/head_img.jpg",
                editorOption: {},
                categoryList: [],
                tagValue:"",
                tagVisible:false,
                tags: [],
                rules: {
                    title: [
                        {required: true, message: '请填写文章标题', trigger: 'blur'},
                    ],
                    content: [
                        {required: true, message: '请填写文章内容', trigger: 'blur'}
                    ],
                }

            }
        },
        components:{
            mavonEditor,
            quillEditor,
            quillRedefine
        },
        created() {
            this.getCategoryList();
            if (this.$route.params.id) {
                this.getArticleInfo(this.$route.params.id);
            } else {
                this.article = {
                    frozen: true,
                    textType: 0,
                    author:this.authorName,
                    headImg:this.authorHeadImg,
                    sort:100
                }
            }
            this.editorOption = quillRedefine(//修改富文本编辑器图片上传路径
                {
                    // 图片上传的设置
                    uploadConfig: {
                        action: "/beam_ht/file/upload",  // 必填参数 图片上传地址
                        methods: 'POST',
                        res: (res) => {
                            return res.data;//return图片url
                        },
                        name: 'file'  // 图片上传参数名
                    }
                })
        },
        watch: {
            $route() {
                if (this.$route.params.id) {
                    this.getArticleInfo(this.$route.params.id);

                } else {
                    this.article = {
                        frozen: true,
                        textType: 0,
                        author:this.authorName,
                        headImg:this.authorHeadImg,
                        sort:100
                    }

                }
            },
        },
        methods: {
            getArticleInfo(articleId){
                const loading = this.$loading();
                ArticleApi.getArticleInfo(articleId).then((res) => {
                    loading.close();
                    if (res.error === false) {
                        this.article = res.data;
                        this.article.frozen = Boolean(this.article.frozen)
                        if(this.article.tag){
                            this.tags = this.article.tag.split(",");
                        }
                        this.$message.success(res.msg);
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    loading.close();
                    this.$message.error(err.msg);
                })
            },
            getCategoryList(){
                ArticleApi.getCategoryList().then((res) => {
                    if (res.error === false) {
                        this.categoryList = res.data;
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                })
            },
            saveArticle() {
                // this.$set(this.tableData, this.idx, this.article);
                this.$refs.articleForm.validate((valid) => {
                    if (valid) {
                        const loading = this.$loading({
                            lock: true,
                            text: 'Loading',
                            spinner: 'el-icon-loading',
                            background: 'rgba(0, 0, 0, 0.7)'
                        });
                        this.article.tag = this.tags.join(",");
                        ArticleApi.save(this.article).then((res) => {
                            loading.close();
                            if (res.error === false) {
                                this.editVisible = false
                                bus.$emit('reloadArticleList', "刷新文章列表");
                                this.$message.success(res.msg);
                                this.$router.push({path: `/article`})
                            } else {
                                this.$message.error(res.msg);
                            }
                        }, (err) => {
                            this.loading = false
                            this.$message.error(err.msg);
                        })
                    }
                })


            },
            handleClose(tag) {
                this.tags.splice(this.tags.indexOf(tag), 1);
            },
            showTagInput() {
                this.tagVisible = true;
                this.$nextTick(_ => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },
            handleInputConfirm() {
                let tagValue = this.tagValue;
                if (tagValue) {
                    this.tags.push(tagValue);
                }
                this.tagVisible = false;
                this.tagValue = '';
            },
            $imgAdd(pos, $file){
                // 第一步.将图片上传到服务器.
                var formdata = new FormData();
                formdata.append('file', $file);
                ArticleApi.uploadImg(formdata).then((res) => {
                    // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
                    // $vm.$img2Url 详情见本页末尾
                    this.$refs.myMavonEditor.$img2Url(pos, res.data);
                })
            },
            $saveContent(value,render){
                // console.log(render); 渲染后的值：html
                // console.log(value);
                if(this.article.id){
                    ArticleApi.saveContent(this.article).then((res) => {
                        if (res.error === false) {
                            this.$message.success(res.msg);
                        } else {
                            this.$message.error(res.msg);
                        }
                    }, (err) => {
                        this.$message.error(err.msg);
                    })
                }
                else {
                    this.saveArticle();
                }

            }

        },
    }
</script>

<style scoped>
    .el-tag + .el-tag {
        margin-left: 10px;
    }
    .button-new-tag {
        margin-left: 10px;
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }
    .input-new-tag {
        width: 90px;
        margin-left: 10px;
        vertical-align: bottom;
    }
</style>
