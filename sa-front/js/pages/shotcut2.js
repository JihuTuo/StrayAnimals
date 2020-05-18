const shotcut ={
    template: "<div>\n" +
        "    <div class=\"header\">   <!--line-height: 30px 将文字宽度与div一致 让文字居中 -->\n" +
        "        <div style=\"margin:0 auto;width:900px;height: 30px;line-height: 30px;font-size:15px\">\n" +
        "            <span class=\"glyphicon glyphicon-home\">&nbsp;</span>请选择您所在的地区：<span style=\"color: red\"><span id=\"city\">{{place}}</span></span>\n" +
        "            <!-- 按钮触发模态框 -->\n" +
        "            <span data-toggle=\"modal2\" data-target=\"#area-modal\">" +
        "            <span  :style=\"loginTo\">\n" +
        "                <span style=\"float:right\">\n" +
        "                    <a href=\"regist.html\">注册</a>\n" +
        "                </span>\n" +
        "                <span style=\"float:right\">&nbsp; | &nbsp;</span>\n" +
        "                <span style=\"float:right\">\n" +
        "                     <a href=\"login.html\">登录</a>\n" +
        "                </span>\n" +
        "            </span>\n" +
        "            <span :style=\"loginShow\">\n" +
        "                <span  style=\"float:right;\">\n" +
        "                欢迎登录：<a href=\"javascript\">{{user.username}}</a>\n" +
        "                 </span>\n" +
        "            </span>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <!-- 地区选择 模态框（Modal） -->\n" +
        "    <div class=\"modal fade\" id=\"area-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n" +
        "        <div class=\"modal-dialog\">\n" +
        "            <div class=\"modal-content\">\n" +
        "                <div class=\"modal-header\">\n" +
        "                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>\n" +
        "                    <h4 class=\"modal-title\" id=\"myModalLabel\"><span class=\"glyphicon glyphicon-home\">&nbsp;</span>请选择您所在的城市</h4>\n" +
        "                </div>\n" +
        "                <!--在这里添加一些文本-->\n" +
        "                <div class=\"modal-body\" style=\"padding-left: 100px\">\n" +
        "\n" +
        "                    <div style=\"width: 100%\">\n" +
        "                       &emsp;\n" +
        "                        <!--省份-->\n" +
        "                        <select style=\"width: 160px\" class=\"input-message\" v-model=\"provinceId\" @change=\"getCities\">\n" +
        "                            <option value=\"-1\">请选择</option>\n" +
        "                            <option v-for=\"p in provinces\" :value=\"p.id\">{{p.name}}</option>\n" +
        "                        </select>\n" +
        "                        &emsp;&emsp;\n" +
        "                        <!--城市-->\n" +
        "                        <select style=\"width: 160px\" class=\"input-message\" v-model=\"cityId\" @change=\"showPlace\">\n" +
        "                            <option v-for=\"c in cities\" :value=\"c.id\">{{c.name}}</option>\n" +
        "                        </select>\n" +
        "                    </div>\n" +
        "\n" +
        "                </div>\n" +
        "\n" +
        "                <div class=\"modal-footer\">\n" +
        "                    <button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>\n" +
        "                </div>\n" +
        "            </div><\n" +
        "        </div>\n" +
        "    </div>\n" +
        "\n" +
        "</div>\n",
    name: "shotcut",
    data() {
        return {
            user: "",
            loginTo: "",
            loginShow: "display: none",
            provinceId: "",
            provinces: [],
            cityId: "",
            cities: [],
            countyId: "",
            counties : [],
            // 默认的地区
            place: "",
        }
    },
    // created() {
    //     ly.http.get("/auth/verify")
    //         .then(resp => {
    //             this.user = resp.data;
    //             ly.store.setJson("user", this.user);
    //             this.changeShow();
    //         });
    //     /*城市*/
    //     this.getProvinces();
    //     /*加载城市*/
    //     this.isChangePlace();
    //     /*向页面传递区县*/
    //     this.toFather ();
    // },
    methods: {
        changeShow() {
            if (this.user) {
                this.loginTo = "display: none";
                this.loginShow = "";
            }
        },

        /*获取省份列表*/
        getProvinces () {
            this.cities = "";
            var _this = this;
            ly.http.get(
                "provinceAndCity/getAllProvince",
            ).then( (resp) => {
                    if (resp.status === 200) {
                        _this.provinces = resp.data;
                    }
                }
            ).catch(
            );
        },
        /*获取城市列表*/
        getCities () {
            // 变化后首先清楚数据
            this.cities = "";
            var _this = this;
            ly.http.get(
                "provinceAndCity/getCityByProvinceId",{
                    params: {
                        provinceId : _this.provinceId,
                    }
                }
            ).then( (resp) => {
                    if (resp.status === 200) {
                        _this.cities = resp.data;
                    }
                }
            );
        },
        /*获取区县列表*/
        getCounties () {
            // 变化后首先清楚数据
            this.counties = "";
            var _this = this;
            ly.http.get(
                "provinceAndCity/getCountyByCityId",{
                    params: {
                        cityId : _this.cityId,
                    }
                }
            ).then( (resp) => {
                    if (resp.status === 200) {
                        _this.counties = resp.data;
                        console.log("开始查询具体县区");
                        console.log(_this.counties);
                        if (_this.counties) {
                            // 将数据保存到locaStorage
                            ly.store.setJson("counties", _this.counties);
                            console.log("通信");
                            _this.toFather ();  // 查询完县区后将数据发送给父组件
                        }
                    }
                }
            );
        },
        showPlace() {
            var province = this.provinces.find( (province) => {
                    return province.id === this.provinceId;
                }
            );

            var city = this.cities.find( (city) => {
                    return city.id === this.cityId;
                }
            );
            console.log("要选择的的city" + province.name + city.name);
           /*将信息存到localSession中*/
            ly.store.setJson("province", province);
            ly.store.setJson("city", city);
            /*动态改变地区*/
            $("#city").text( province.name + "-" + city.name);

            /*最后将县区数据进行加载*/
            this.getCounties();
            // 刷新数据
            this.toFather();
        },

        isChangePlace() {
            const province = ly.store.getJson("province");
            const city = ly.store.getJson("city");
            const counties = ly.store.getJson("counties");
            /*动态改变地区*/
            if(province && city && counties) {
                this.place = province.name + "-" + city.name;
                this.counties = counties;
               // $("#city").text( province + "-" + city);
            }
        },

        /*子组件向父组件传递数据*/
        toFather () {
            // this.$emit(event,child-data)  第一个参数是虚拟事件，第二个参数是子组件中要传递的参数
            let userId = ly.store.getJson("user").id;
            //console.log("触发了子组件" + this.cityId);
            this.$emit("deliver", userId, this.counties, this.cityId);
        },
    }

}
export default shotcut;