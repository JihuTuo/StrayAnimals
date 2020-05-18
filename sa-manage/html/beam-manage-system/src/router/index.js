import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/blog'
        },
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: {title: '系统管理'},
            children: [
                {
                    path: '/dashboard',
                    component: resolve => require(['../components/common/Dashboard.vue'], resolve),
                    meta: {title: '系统首页', permission: false, perms: "sys:dashboard:info"},
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: resolve => require(['../components/common/Permission.vue'], resolve),
                    meta: {title: '权限测试', permission: true}
                },

                {
                    path: '/403',
                    component: resolve => require(['../components/common/403.vue'], resolve),
                    meta: {title: '403'}
                }
                ,
                {
                    path: '/sysuser',
                    component: resolve => require(['../components/sys/sysuser.vue'], resolve),
                    meta: {title: '用户管理', permission: true, perms: "sys:user:list"}
                },
                {
                    path: '/sysrole',
                    component: resolve => require(['../components/sys/sysrole.vue'], resolve),
                    meta: {title: '角色管理', permission: true, perms: "sys:role:list"}
                },
                {
                    path: '/sysmenu',
                    component: resolve => require(['../components/sys/sysmenu.vue'], resolve),
                    meta: {title: '菜单管理', permission: true, perms: "sys:menu:list"}
                },
                {
                    path: '/sysdept',
                    component: resolve => require(['../components/sys/sysdept.vue'], resolve),
                    meta: {title: '部门管理', permission: true, perms: "sys:dept:list"}
                },
                {
                    path: '/schedulejob',
                    component: resolve => require(['../components/sys/schedulejob.vue'], resolve),
                    meta: {title: '定时任务管理', permission: true, perms: "sys:schedule:list"}
                },
                {
                    path: '/sysdict',
                    component: resolve => require(['../components/sys/sysdict.vue'], resolve),
                    meta: {title: '字典管理', permission: true, perms: "sys:dict:list"}
                },
                {
                    path: '/loginlog',
                    component: resolve => require(['../components/sys/loginlog.vue'], resolve),
                    meta: {title: '登陆日志', permission: true, perms: "sys:loginLog:list"}
                },
                {
                    path: '/operationlog',
                    component: resolve => require(['../components/sys/operationlog.vue'], resolve),
                    meta: {title: '业务日志', permission: true, perms: "sys:operationLog:list"}
                }

            ]
        },
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: {title: '内容管理'},
            children: [
                {
                    path: '/article',
                    component: resolve => require(['../components/blog/back/article.vue'], resolve),
                    meta: {title: '文章管理', permission: false},
                },
                {
                    path: '/article/add',
                    component: resolve => require(['../components/blog/back/addArticle.vue'], resolve),
                    meta: {title: '新增文章', permission: false},
                },
                {
                    path: '/article/edit/:id',
                    component: resolve => require(['../components/blog/back/addArticle.vue'], resolve),
                    meta: {title: '修改文章', permission: false},
                },
                {
                    path: '/article/category',
                    component: resolve => require(['../components/blog/back/category.vue'], resolve),
                    meta: {title: '文章分类', permission: false},
                },
            ]
        },

        {
            path: '/login',
            component: resolve => require(['../components/common/Login.vue'], resolve)
        },
        {
            path: '/blog',
            redirect: '/blog/index',
            component: resolve => require(['../components/blog/front/blog.vue'], resolve),
            meta: {title: '博客管理'},
            children: [
                {
                    path: '/blog/index',
                    component: resolve => require(['../components/blog/front/index.vue'], resolve),
                    meta: {title: 'hsshy的博客', permission: false},
                },
                {
                    path: '/blog/detail/:shortCode',
                    component: resolve => require(['../components/blog/front/detail.vue'], resolve),
                    meta: {title: '文章详情', permission: false},
                },
                {
                    path: '/blog/document',
                    component: resolve => require(['../components/blog/front/document.vue'], resolve),
                    meta: {title: '文档', permission: false},

                },
            ]
        },
        {
            path: '/404',
            component: resolve => require(['../components/common/404.vue'], resolve),
            meta: {title: '404'}
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
})
