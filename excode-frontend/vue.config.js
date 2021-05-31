module.exports = {
  devServer: {
    proxy: {
      '/api': {
        ws: true,
        changeOrigin: true,
        target: 'http://localhost:9901'
      }
    }
  }
}
