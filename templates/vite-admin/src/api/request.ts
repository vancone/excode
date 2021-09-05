import axios, { AxiosRequestConfig, AxiosResponse, AxiosError} from "axios"
import { ElMessage } from "element-plus"
import { Response, ResponseError } from '@/api/types'
import { removeResource, storeResource } from '@/api/request-resources'
import { redirectToLogin } from '@/utils/index'

const { CancelToken } = axios

export const enum ResponseCode {
    Success
}

const enum ResponseStatus {
    Unauthorized = 401,
    ServerError = 500
}

export const enum RequestMethods {
    GET = 'GET',
    POST = 'POST'
}

const service = axios.create({
    timeout: 2 * 60 * 1000,
    withCredentials: true
})

service.interceptors.request.use((config: AxiosRequestConfig) => {
    const axiosConfig = config

    removeResource(axiosConfig)
    axiosConfig.cancelToken = new CancelToken((cancel) => {
        const { url, method } = axiosConfig
        storeResource({ url, method, cancel })
    })

    return axiosConfig
})

service.interceptors.response.use((response: AxiosResponse<Response>) => {
    const { headers, method } = response.config
    const { code, message } = response.data
    const { 'No-Success-Message': noSuccessMessage } = headers
    const notGet = method !== 'get'

    removeResource(response.config)

    if (code === ResponseCode.Success) {
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
}, (error: AxiosError<ResponseError>) => {
    const { config, response } = error
    if (config) {
        removeResource(config)
    }

    if (response) {
        const { status } = response
        if (status === ResponseStatus.Unauthorized) {
            redirectToLogin()
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