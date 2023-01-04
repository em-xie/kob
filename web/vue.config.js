const { defineConfig } = require('@vue/cli-service')
const port = process.env.port || process.env.npm_config_port || 8080 // 端口
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    host: 'localhost',
    port: port,
    open: true,
    proxy: {
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:3000`,
        changeOrigin: true,
      }
    }
  }
})

