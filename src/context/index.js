import MyRouter from "@/router"
import HomePage from "@/modules/Home/view/HomePage";

const contexts = require("./ModuleContexts.json");
const moduleNames = Object.keys(contexts);
const attrs = {
    'component': 'component',
    'name': 'name'
}
let module_name_zh = [];
function initModules() {
    for (let moduleName of moduleNames) {
        let context = loadContext(moduleName);
        if (context().context.default instanceof Function) {
            context().context.default();
        }
    }
}

function loadContext(moduleName) {
    return function () {
        return {
            context: require(`@/modules/${moduleName}/${moduleName}Context.js`)
        }
    }
}

function loadComponent(moduleName, vueName) {
    return require(`@/modules/${moduleName}/view/${vueName}.vue`)
}

function initRoutes() {
    let routes = {
        path: '/home',
        name: 'Home',
        component: HomePage,
        children: []
    };
    for (let moduleName of moduleNames) {
        let router = {
            path: moduleName,
            name: moduleName,
            components: loadComponent(moduleName, contexts[moduleName][attrs.component])
        }
        routes['children'].push(router);
        let item = {
            name: contexts[moduleName][attrs.name],
            link: router.path,
            children: [] // TODO 目前只想做成一级的，预留出children字段用于以后扩展二级三级菜单
        }
        module_name_zh.push(item);
    }
    MyRouter.addRoute(routes);
}

const Context = {
    module_name_zh: module_name_zh,
    initState: 0,
    init: function () {
        if (this.initState > 0) {
            console.warn("System Initialized!");
        }
        initModules();
        initRoutes();
        this.initState = 1;
    }
};

export default Context;