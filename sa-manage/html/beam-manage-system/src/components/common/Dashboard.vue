<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img :src="sysuser.avatar" class="user-avator" alt="">
                        <div class="user-info-cont">
                            <div class="user-info-name">{{sysuser.account}}</div>
                            <div>{{sysuser.roleNames[0]}}</div>
                        </div>
                    </div>
                    <div class="user-info-list">真实姓名：<span>{{sysuser.name}}</span></div>
                    <div class="user-info-list">所在部门：<span>{{sysuser.deptName}}</span></div>
                </el-card>

                <el-card shadow="hover" class="mgb20">
                    <div class="user-info">
                        <el-image style="width: 120px;height: 120px;"
                                  src="http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png"></el-image>
                        <div class="user-info-cont">
                            <div class="user-info-name">微信公众号</div>
                            <div>还有问题请移步公众号，回复加群或者加微信</div>
                        </div>
                    </div>
                </el-card>
                <el-card shadow="hover" class="mgb20">
                    <div class="user-info">
                        <img src="http://img.hsshy.cn/5f3cf4da-b38f-4b0c-be54-93e35a637056.png" class="user-avator"
                             alt="">
                        <div class="user-info-cont">
                            <div class="user-info-name">光有工具</div>
                            <div>微信小程序：图片文字识别、动植物识别、车型识别、二维码生成解析、手写板等工具</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="16">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-1">
                                <i class="el-icon-lx-people grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">999999999</div>
                                    <div>用户访问量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-lx-notice grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">321</div>
                                    <div>系统消息</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-lx-goods grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">5000</div>
                                    <div>数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card>
                    <el-amap-search-box class="search-box" :search-option="amap.searchOption" :on-search-result="onSearchResult"></el-amap-search-box>
                    <el-amap ref="map" vid="amapDemo" :amap-manager="amap.amapManager" :center="amap.center" :zoom="amap.zoom" :plugin="amap.plugin"   :events="amap.events" class="amap-wrapper">
                        <el-amap-marker vid="component-marker" :position="amap.markers.position" visible="true" draggable="true"></el-amap-marker>
                        <el-amap-info-window
                            :position="amap.markers.position"
                            :visible="amap.currentWindow.visible"
                            :content="amap.currentWindow.content">
                        </el-amap-info-window>
                    </el-amap>
                    <div class="toolbar">
                        <span v-if="amap.loaded">
                          location: lng = {{amap.center.lng }} lat = {{ amap.center.lat }}
                        </span>
                        <span v-else>正在定位</span>
                    </div>
                </el-card>
            </el-col>

        </el-row>


    </div>
</template>

<script>
    import Schart from 'vue-schart';
    import bus from '../../api/bus';
    import DashboardApi from '../../api/dashboard';
    import { AMapManager } from 'vue-amap';
    let amapManager = new AMapManager();
    export default {
        name: 'dashboard',
        data() {
            return {
                user: null,
                amap:{
                    loaded:false,
                    center:{},
                    amapManager,
                    address:{
                        province:null,
                        district:null,
                        city:null
                    },
                    searchOption: {
                        citylimit: true
                    },
                    currentWindow:{
                        visible:true,
                        content:null
                    },
                    plugin: [
                       {
                            pName: 'ToolBar',
                            defaultType: 0,
                            events: {
                                init(o) {
                                    console.log(o);
                                }
                            }
                        },
                        {
                            pName: 'Geolocation',
                            events: {
                                init(o) {
                                    // o 是高德地图定位插件实例
                                    console.log(o);
                                    let that = this;
                                    o.getCurrentPosition((status, result) => {
                                        console.log(result);
                                        if (result && result.position) {
                                            that.amap.center = [result.position.lng,result.position.lat];
                                            that.amap.loaded = true;
                                            that.$nextTick();
                                        }
                                    });
                                }
                            }
                        }
                    ],
                    zoom:20,
                    markers:{
                        position:[],
                    },
                    events: {
                        init: (o) => {
                            console.log(o);
                            o.getCity(result => {
                                this.amap.address.city=result.city;
                                this.amap.address.province=result.province
                                this.amap.address.district=result.district
                            })
                        },
                        'moveend': () => {
                        },
                        'zoomchange': () => {
                        },
                        'click': (e) => {
                            console.log(e)
                            this.amap.markers.position=[e.lnglat.lng,e.lnglat.lat]
                            var geocoder = new AMap.Geocoder({
                                radius: 1000,
                                extensions: "all"
                            });
                            let _this =this
                            geocoder.getAddress([e.lnglat.lng,e.lnglat.lat], function(status, result) {
                                console.log(result);
                                let temp=result.regeocode.formattedAddress;
                                _this.amap.currentWindow.content="地址:"+temp
                            });
                        },
                    },
                },
            }
        },
        components: {
            Schart
        },
        computed: {
            sysuser() {
                let sysuser = JSON.parse(localStorage.getItem('sysuser'));
                return sysuser ? sysuser : this.user;
            }
        },
        created() {
            this.getDashboardContent();
            this.handleListener();
            this.initMap();
        },
        activated() {
            this.handleListener();
        },
        deactivated() {
            window.removeEventListener('resize', this.renderChart);
            bus.$off('collapse', this.handleBus);
        },
        methods: {
            initMap() {
                let longitude =  119.237974 ;
                let latitude = 26.053042;
                this.amap.center=[longitude, latitude]
                this.amap.markers.position=this.amap.center;
            },
            onSearchResult(pois) {
                if (pois.length > 0) {
                    //第一个为精确搜索点
                    this.amap.markers.position=[pois[0].lng, pois[0].lat];
                    this.amap.center = [pois[0].lng, pois[0].lat];
                }
            },
            getDashboardContent() {
                DashboardApi.getDashboardContent().then((res) => {
                }, (err) => {
                    this.$message.error(err.msg);
                })
            },
            handleListener() {
                bus.$on('collapse', this.handleBus);
                // 调用renderChart方法对图表进行重新渲染
                window.addEventListener('resize', this.renderChart)
            },
            handleBus(msg) {
                setTimeout(() => {
                    this.renderChart()
                }, 300);
            }
        }
    }

</script>


<style scoped>
    .search-box{
        margin-bottom: 10px;
    }

    .amap-wrapper {
        width: 100%;
        height: 300px;
    }

    .el-row {
        margin-bottom: 20px;
    }

    .grid-content {
        display: flex;
        align-items: center;
        height: 100px;
    }

    .grid-cont-right {
        flex: 1;
        text-align: center;
        font-size: 14px;
        color: #999;
    }

    .grid-num {
        font-size: 30px;
        font-weight: bold;
    }

    .grid-con-icon {
        font-size: 50px;
        width: 100px;
        height: 100px;
        text-align: center;
        line-height: 100px;
        color: #fff;
    }

    .grid-con-1 .grid-con-icon {
        background: rgb(45, 140, 240);
    }

    .grid-con-1 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-2 .grid-con-icon {
        background: rgb(100, 213, 114);
    }

    .grid-con-2 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-3 .grid-con-icon {
        background: rgb(242, 94, 67);
    }

    .grid-con-3 .grid-num {
        color: rgb(242, 94, 67);
    }

    .user-info {
        display: flex;
        align-items: center;
        padding-bottom: 20px;
        border-bottom: 2px solid #ccc;
        margin-bottom: 20px;
    }

    .user-avator {
        width: 120px;
        height: 120px;
        border-radius: 50%;
    }

    .user-info-cont {
        padding-left: 50px;
        flex: 1;
        font-size: 14px;
        color: #999;
    }

    .user-info-cont div:first-child {
        font-size: 30px;
        color: #222;
    }

    .user-info-list {
        font-size: 14px;
        color: #999;
        line-height: 25px;
    }

    .user-info-list span {
        margin-left: 70px;
    }

    .mgb20 {
        margin-bottom: 20px;
    }

    .todo-item {
        font-size: 14px;
    }

    .todo-item-del {
        text-decoration: line-through;
        color: #999;
    }

    .schart {
        width: 100%;
        height: 300px;
    }

</style>
