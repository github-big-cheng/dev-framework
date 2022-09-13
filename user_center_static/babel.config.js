module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  plugins: [
    [
      'import',
      {
        libraryName: 'vxe-table',
        style: true // 是否按需加载样式
      }
    ]
  ]
}
