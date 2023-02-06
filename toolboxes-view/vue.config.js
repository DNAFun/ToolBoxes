

const { defineConfig } = require('@vue/cli-service')
const Api_Url_Dev = "http://127.0.0.1:8999/xmingl";
const Api_Url_Prod = "http://127.0.0.1:8999/xmingl";
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    proxy:{
      "/api":{
        changeOrigin: true,
        target: Api_Url_Dev,
        pathRewrite: {
          '^/api': ''// 重定向删除/api
        }
      },
      "/api1":{
        changeOrigin: true,
        target: Api_Url_Prod
      }
    }
  }
})
