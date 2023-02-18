/*https://blog.csdn.net/cscscssjsp/article/details/81290256*/
const ue2 = {
    template: " <div>\n" +
        "    <script :id=id type=\"text/plain\"></script>\n" +
        "  </div>",
    name: "ue2",

    data () {
        return {
            editor: ""
        }
    },
    props: {
        defaultmsg2: {
            type: String
        },
        config2: {
            type: Object
        },
        id2: {
            type: String
        },
    },
    mounted() {
        const _this = this;
        this.editor = UE.getEditor(this.id, this.config); // 初始化UE
        this.editor.addListener("ready", function () {
            _this.editor.setContent(_this.defaultmsg); // 确保UE加载完成后，放入内容。
        });
        UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
        UE.Editor.prototype.getActionUrl = function(action) {
            if (action === 'uploadimage'){
                return 'http://127.0.0.1/stary-animals/ueditor/upload'; //!* 你自己的图片上传action *!/
            }else{
                return this._bkGetActionUrl.call(this, action);
            }
        };
    },
    methods: {
        getUEContent() { // 获取内容方法
            return this.editor.getContent()
        },
        getUEContentTxt() { // 获取纯文本内容方法
            return this.editor.getContentTxt()
        },
        show () {
          console.log("====-99999999999999");
        },
    },
    destroyed() {
        this.editor.destroy();
    }
}
export default ue2;