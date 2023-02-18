/*<template>
    <div>
        <!-- 图片上传组件辅助-->
        <el-upload
                class="avatar-uploader"
                :action="serverUrl"
                name="pic"
                :headers="header"
                :show-file-list="false"
                :on-success="uploadSuccess"
                :on-error="uploadError"
                :before-upload="beforeUpload">
        </el-upload>

        <quill-editor
                class="editor"
                v-model="content"
                ref="myQuillEditor"
                :options="editorOption"
                @blur="onEditorBlur($event)" @focus="onEditorFocus($event)"
                @change="onEditorChange($event)">
        </quill-editor>
    </div>
</template>
<script>*/
    // 工具栏配置
    import shotcut from "../../js/pages/shotcut";

const edit = {
        template: " <div>\n" +
            "        <!-- 图片上传组件辅助-->\n" +
            "        <el-upload\n" +
            "                class=\"avatar-uploader\"\n" +
            "                :action=\"serverUrl\"\n" +
            "                name=\"pic\"\n" +
            "                :headers=\"header\"\n" +
            "                :show-file-list=\"false\"\n" +
            "                :on-success=\"uploadSuccess\"\n" +
            "                :on-error=\"uploadError\"\n" +
            "                :before-upload=\"beforeUpload\">\n" +
            "        </el-upload>\n" +
            "\n" +
            "        <quill-editor\n" +
            "                class=\"editor\"\n" +
            "                v-model=\"content\"\n" +
            "                ref=\"myQuillEditor\"\n" +
            "                :options=\"editorOption\"\n" +
            "                @blur=\"onEditorBlur($event)\" @focus=\"onEditorFocus($event)\"\n" +
            "                @change=\"onEditorChange($event)\">\n" +
            "        </quill-editor>\n" +
            "    </div>",
            name: edit,

    const toolbarOptions = [
        ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
        ["blockquote", "code-block"], // 引用  代码块
        [{ header: 1 }, { header: 2 }], // 1、2 级标题
        [{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表
        [{ script: "sub" }, { script: "super" }], // 上标/下标
        [{ indent: "-1" }, { indent: "+1" }], // 缩进
        // [{'direction': 'rtl'}],                         // 文本方向
        [{ size: ["small", false, "large", "huge"] }], // 字体大小
        [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
        [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
        [{ font: [] }], // 字体种类
        [{ align: [] }], // 对齐方式
        ["clean"], // 清除文本格式
        ["link", "image", "video"] // 链接、图片、视频
    ];

import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";

    props: {
        /*编辑器的内容*/
        value: {
            type: String
        },
        /*图片大小*/
        maxSize: {
            type: Number,
            default: 4000 //kb
        }
    },

    components: {
        quillEditor
    },

    data() {
        return {
            content: this.value,
            quillUpdateImg: false, // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
            editorOption: {
                placeholder: "",
                theme: "snow", // or 'bubble'
                placeholder: "您想说点什么？",
                modules: {
                    toolbar: {
                        container: toolbarOptions,
                        // container: "#toolbar",
                        handlers: {
                            image: function(value) {
                                if (value) {
                                    // 触发input框选择图片文件
                                    document.querySelector(".avatar-uploader input").click();
                                } else {
                                    this.quill.format("image", false);
                                }
                            },
                            // link: function(value) {
                            //   if (value) {
                            //     var href = prompt('请输入url');
                            //     this.quill.format("link", href);
                            //   } else {
                            //     this.quill.format("link", false);
                            //   }
                            // },
                        }
                    }
                }
            },
            serverUrl: process.env.BASE_API+'upload', // 这里写你要上传的图片服务器地址
            header: {
                // token: sessionStorage.token
            } // 有的图片服务器要求请求头需要有token
        };
    },

    methods: {
        onEditorBlur() {
            //失去焦点事件
        },
        onEditorFocus() {
            //获得焦点事件
        },
        onEditorChange() {
            //内容改变事件
            this.$emit("input", this.content);
        },

        // 富文本图片上传前
        beforeUpload() {
            // 显示loading动画
            this.quillUpdateImg = true;
        },

        uploadSuccess(res, file) {
            debugger
            // res为图片服务器返回的数据
            // 获取富文本组件实例
            let quill = this.$refs.myQuillEditor.quill;
            // 如果上传成功
            console.log("ueditor res: " + res)
            if (res.code === 200) {
                // 获取光标所在位置
                let length = quill.getSelection().index;
                // 插入图片  res.url为服务器返回的图片地址
                quill.insertEmbed(length, "image", res.data.url);
                // 调整光标到最后
                quill.setSelection(length + 1);
            } else {
                this.$message.error("图片插入失败");
            }
            // loading动画消失
            this.quillUpdateImg = false;
        },
        // 富文本图片上传失败
        uploadError() {
            // loading动画消失
            console.log("上传失败 ueditor res: " + res)
            this.quillUpdateImg = false;
            this.$message.error("图片插入失败");
        }
    }

export default edit;