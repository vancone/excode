import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  timeout: 2 * 60 * 1000,
  withCredentials: true
})

service.interceptors.request.use((config) => {
//   const axiosConfig = config

  //   removeResource(axiosConfig)
  //   axiosConfig.cancelToken = new CancelToken((cancel) => {
  //     const { url, method } = axiosConfig
  //     storeResource({ url, method, cancel })
  //   })

  return config
})

service.interceptors.response.use((response) => {
  const { headers, method } = response.config
  const { code, message } = response.data
  const { 'No-Success-Message': noSuccessMessage } = headers
  const notGet = method !== 'get'

  //   removeResource(response.config)

  if (code === 0) {
    if (notGet && !noSuccessMessage) {
      ElMessage({
        message,
        type: 'success'
      })
    }

    return Promise.resolve(response)
  }

  ElMessage({
    type: 'error',
    message
  })

  return Promise.reject(new Error(`${code}`))
}, (error) => {
  const { config, response } = error
  if (config) {
    // removeResource(config)
  }

  if (response) {
    const { status } = response
    if (status === 401) {
      const currentUrl = window.escape(window.location.href);
      window.location.href = `//${import.meta.env.VITE_PASSPORT_BASE_URL}/?from=${currentUrl}`
    }

    const { message, error: errorMessage } = response.data
    ElMessage({
      type: 'error',
      message: message || errorMessage
    })
  }

  return Promise.reject(new Error(error.message || 'Response Error'))
})

export default service
