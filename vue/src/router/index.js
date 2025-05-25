import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Test from '../views/Test.vue'
import Sorry from '../views/404.vue'
import Manager from '../views/Manager.vue'
import Data from '../views/Data.vue'
import Employee from '../views/Employee.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Admin from '../views/Admin.vue'
import Person from '../views/Person.vue'
import Password from '../views/Password.vue'
import Department from '../views/Department.vue'
import Product from '../views/Product.vue'
import Category from '../views/Category.vue'
import Log from '../views/Log.vue'
const routes = [
    { path: '/login', meta: {title: '登录系统'}, component: Login},
    { path: '/', redirect: '/login'},
    { path: '/manager', meta: {title: '管理页面'}, component: Manager, children:
            [
                { path: 'home', meta: { title: '主页面' }, component: Home },
                { path: 'Test', meta: { title: '测试页面' }, component: Test },
                { path: 'data', meta: {title: '数据展示页面'}, component: Data },
                { path: 'employee', meta: {title: '员工信息'}, component: Employee},
                { path: 'admin', meta: {title: '管理员信息'}, component: Admin},
                { path: 'person', meta: {title: '个人信息'}, component: Person},
                { path: 'password', meta: {title: '修改密码'}, component: Password},
                { path: 'department', meta: {title: '部门信息'}, component: Department},
                { path: 'product', meta: {title: '產品管理'}, component: Product},
                { path: 'category', meta: {title: '產品類別'}, component: Category},
                { path: 'log', meta: {title: '日誌管理'}, component: Log}
            ]
    },
    { path: '/register', meta: {title: '欢迎注册'}, component: Register },
    { path: '/404', name: 'NotFound', meta: {title: '404页面'}, component: Sorry },
    { path: '/:pathMatch(.*)', redirect: '/404' },
]
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
    // routes: [
    //   { path: '/', name: 'home', component: () => import('../views/Home.vue'),},
    // ],
})

router.beforeEach((to, from, next) => {
    document.title = to.meta.title || '默认标题';
    next()
})

export default router