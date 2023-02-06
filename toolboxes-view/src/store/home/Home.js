//state:仓库存储数据的地方
const state = {}
//mutations：存放了可以修改state中数据的方法，且仅支持同步操作，修改state的唯一手段
const mutations = {}
//actions:同mutations类似，可以书写自己的业务逻辑，也可以处理异步，不直接更变数据状态，而是提交到mutations，由mutations进行变更
const actions = {}
//getters:理解为计算属性，用于简化仓库数据，让组件获取仓库的数据更加方便
const getters = {}

export default {
    state,
    mutations,
    actions,
    getters
}