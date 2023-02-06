import { createApp } from 'vue'
import App from './App.vue'
import store from './store/index'
import router from './router/index'
import context from "@/context/index";
import ElementPlus  from "element-plus";
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import '../public/css/components.css';
import '../public/css/modules.css';

context.init();

const app = createApp(App);
app.use(store);
app.use(router);
app.use(ElementPlus, {
    locale: zhCn,
})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount("#app");
app.mixin({
    beforeCreate: function (){
        this.$context = context;
    }
})