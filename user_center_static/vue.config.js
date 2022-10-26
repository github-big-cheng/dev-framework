const path = require("path");
const TerserPlugin = require("terser-webpack-plugin");
const BundleAnalyzerPlugin = require("webpack-bundle-analyzer")
  .BundleAnalyzerPlugin;
const CompressionPlugin = require("compression-webpack-plugin");

const isProduction = process.env.NODE_ENV === "production";
const requestUrl = "http://127.0.0.1";

module.exports = {
  // 基本路径
  // baseUrl: './',
  // 输出文件目录
  outputDir: "static",
  assetsDir: "static",
  indexPath: "index.html",
  publicPath: process.env.NODE_ENV === "production" ? '/' : process.env.BASE_URL,
  // eslint-loader 是否在保存的时候检查
  lintOnSave: true,
  // webpack配置
  chainWebpack: (config) => {
    config
      .entry("index")
      .add("babel-polyfill")
      .end();
    config.module
      .rule("thejs")
      .test(/\.js$/)
      .include.add(path.resolve("src"))
      .add(path.resolve("node_modules/element-ui/packages"))
      .end()
      .use("babel-loader")
      .loader("babel-loader")
      .end();
    config.module
      .rule("svg")
      .exclude.add(path.resolve("src/icons"))
      .end();
    config.module
      .rule("icons")
      .test(/\.svg$/)
      .include.add(path.resolve("src/icons"))
      .end()
      .use("svg-sprite-loader")
      .loader("svg-sprite-loader")
      .options({
        symbolId: "icon-[name]",
      })
      .end();
    config.module
      .rule("md")
      .test(/\.md/)
      .use("vue-loader")
      .loader("vue-loader")
      .end()
      .use("vue-markdown-loader")
      .loader("vue-markdown-loader/lib/markdown-compiler")
      .options({
        raw: true,
      });

    if (isProduction) {
      // 删除预加载
      config.plugins.delete("preload");
      config.plugins.delete("prefetch");
      // 压缩代码
      config.optimization.minimize(true);
      // 分割代码
      config.optimization.splitChunks({
        chunks: "all",
      });
      config
        .plugin("BundleAnalyzerPlugin")
        .use(BundleAnalyzerPlugin)
        .tap(() => [
          {
            rel: "BundleAnalyzerPlugin",
            analyzerMode: "server", // 'server': 启动端口服务；'static': 生成 report.html；'disabled': 配合 generateStatsFile 使用；
            generateStatsFile: false, // 是否生成stats.json文件
            analyzerHost: "127.0.0.1",
            analyzerPort: "8879",
            reportFilename: "report.html",
            defaultSizes: "parsed",
            openAnalyzer: false,
            statsFilename: "stats.json",
            statsOptions: null,
            excludeAssets: null,
          },
        ]);
      config.plugin("compressionPlugin").use(
        new CompressionPlugin({
          algorithm: "gzip",
          test: /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i,
          threshold: 10240,
          minRatio: 0.8,
          deleteOriginalAssets: false,
        })
      );
    }
    // 移除prefetch插件，避免加载多余的资源
    config.plugins.delete("prefetch");
  },
  configureWebpack: (config) => {
    config.optimization = {
      minimizer: [
        new TerserPlugin({
          terserOptions: {
            ecma: undefined,
            warnings: false,
            parse: {},
            compress: {
              drop_console: true,
              drop_debugger: false,
              pure_funcs: ["console.log"], // 移除console
            },
          },
        }),
      ],
    };

    // config.externals = {
    //   'vue':'Vue'
    // }
    // 公共代码抽离
    config.optimization = {
      splitChunks: {
        chunks: "all",
        cacheGroups: {
          vue: {
            name: "chunk-vuejs",
            test: /[\\/]node_modules[\\/]_?vue(.*)/,
            priority: 20,
          },
          elementUI: {
            name: "chunk-elementUI", // split elementUI into a single package
            priority: 30, // the weight needs to be larger than libs and app or it will be packaged into libs or app
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/, // in order to adapt to cnpm
          },
          tinymce: {
            // split async commons chunk
            name: "chunk-tinymce",
            priority: 20,
            test: /[\\/]node_modules[\\/]_?tinymce(.*)/,
            chunks: "async",
          },
          themeModules: {
            // split async commons chunk
            name: "chunk-theme-modules",
            priority: 10,
            test: /[\\/]node_modules(.*)/,
            chunks: "async",
          },
        },
      },
    };
  },
  // 生产环境是否生成 sourceMap 文件
  productionSourceMap: false,
  parallel: require("os").cpus().length > 1,
  devServer: {
    port: 8080,
    https: false,
    open: false,
    // after: require("./mock/mock-server.js"),
    proxy: {
      "/dev-api/sso": {
        target: requestUrl + "/sso",
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          "^/dev-api/sso": "",
        },
      },
      "/dev-api/sso/websocket": {
        target: requestUrl + "/sso/websocket",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/dev-api/sso/websocket": "",
        },
      },
      "/dev-api/ucenter": {
        target: requestUrl + "/ucenter",
        // target: "http://localhost:82/ucenter",
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          "^/dev-api/ucenter": "",
        },
      },
      "/dev-api/sys": {
        target: requestUrl + "/sys",
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          "^/dev-api/sys": "",
        },
      },
    },
  },
  css: {
    loaderOptions: {
      sass:{
        sassOptions: {
          outputStyle: 'expanded',
          // outputStyle: 'compressed',
        },
      },
      postcss: {
        plugins: [
          require('postcss-plugin-px2rem')({
            rootValue: 144,
            unitPrecision: 5,
            // selectorBlackList: ['el-checkbox', 'el-radio'],　//选择器黑名单
            // propBlackList: [], //属性黑名单
            minPixelValue: 3,
            // exclude: /node_modules/i
          }),
        ]
      }
    }
  }
}
