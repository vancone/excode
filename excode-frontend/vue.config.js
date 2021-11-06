module.exports = {
  devServer: {
    hot: true,
    proxy: {
      '/api': {
        ws: true,
        changeOrigin: true,
        target: 'http://localhost:9901'
      }
    }
  }
}
