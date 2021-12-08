module.exports = {
    devServer: {
        port: 8080,
        headers: {},
        inline: true,
        overlay: true,
        stats: 'errors-only',
        proxy: {
            '/api': {
                target: 'http://127.0.0.1:8081',
                changeOrigin: true // 是否跨域
            }
        }
    },
}