
// const { defineConfig } = require('@vue/cli-service')

 const port = process.env.port || process.env.npm_config_port || 8080 // 端口
// // gzip压缩
// const CompressionWebpackPlugin = require('compression-webpack-plugin')


// // 代码压缩
// const UglifyJsPlugin = require('uglifyjs-webpack-plugin')


module.exports = {
  // publicPath: './',
  transpileDependencies: true,
  lintOnSave: false,
  // 在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
  outputDir: 'dist',
  // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  assetsDir: 'static',
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
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
  },
//   css: {
//     loaderOptions: {
//       sass: {
//         sassOptions: { outputStyle: "expanded" }
//       }
//     }
//   },


// configureWebpack: (config) => {
  
//   if (process.env.NODE_ENV === 'production') {
//       /* gzip压缩 */
//       const productionGzipExtensions = ['html', 'js', 'css']
//       config.plugins.push(
//         new CompressionWebpackPlugin({
//           filename: '[path][base].gz',
//           algorithm: 'gzip',
//           test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
//           threshold: 10240, // 只有大小大于该值的资源会被处理 10240
//           minRatio: 0.8, // 只有压缩率小于这个值的资源才会被处理
//           deleteOriginalAssets: false, // 删除原文件
//         })
//       )
//     }


// /* 代码压缩 */
// config.plugins.push(
//   new UglifyJsPlugin({
//     uglifyOptions: {
//       //自动删除console
//       compress: {
//         // warnings: false, // 若打包错误，则注释这行
//         drop_debugger: true,
//         drop_console: true,
//         pure_funcs: ['console.log'],
//       },
//     },
//     sourceMap: false,
//     parallel: true,
//   })
// )
// },


//    chainWebpack: (config) => {
//     /* 压缩图片 */
//     config.module
//       .rule('images')
//       .use('image-webpack-loader')
//       .loader('image-webpack-loader')
//       .options({ bypassOnDebug: true })
//       .end()
//   },


}

