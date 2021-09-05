import { ISummaryMethodParams, Nullable, IResultMap } from '@/api/types'
import { RouteRecordRaw } from 'vue-router'
import { clearAllCookies } from '@/utils/cookie'
import { ISummaryMethodParams } from '../api/types'

export const getErrorMessageForJson = (msg: string) => {
    const result = {
        code: '',
        message: ''
    }
    let message = {}
    try {
        message = JSON.parse(msg)
    } catch (error) {
        if (msg.indexOf('content:') > -1) {
            const content = msg.split('content:')[1].trim()
            const contentJson = JSON.parse(content)
            message = JSON.parse(contentJson.message)
        }
    }
    Object.assign(result, message)
    return result
}

export const visibleChildrenFilter = (children: Array<RouteRecordRaw>) => children.filter(
    ({ meta }) => !meta || !(meta as any).hidden
)

export function getPropByPath(obj: any, path: string, strict: boolean): {
    o: unknown
    k: string
    v: Nullable<unknown>
} {
    let tempObj = obj
    let tempPath = path
    tempPath = tempPath.replace(/\[(\w+)]/g, '.$1')
    tempPath = tempPath.replace(/^\./, '')

    const keyArr = tempPath.split('.')
    let i = 0
    for (i; i < keyArr.length - 1; i += 1) {
        if (!tempObj && !strict) break
        const key = keyArr[i]

        if (key in tempObj) {
            tempObj = tempObj[key]
        } else {
            if (strict) {
                throw new Error('please transfer a valid prop path to form item!')
            }
            break
        }
    }
    return {
        o: tempObj,
        k: keyArr[i],
        v: tempObj?.[keyArr[i]]
    }
}

export function getSummaries(param: ISummaryMethodParams) {
    const { columns, data } = param
    const sums: Array<any> = []
    columns.forEach((column, index) => {
        if (index === 0) {
            sums[index] = '合计'
            return
        }
        const { property } = column
        if (!property) {
            sums[index] = ''
            return
        }

        const values = data.map((item) => Number(getPropByPath(item, property, false).v))
        if (!values.every((value) => Number.isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr)
                if (Number.isNaN(value)) {
                    return prev
                }
                return Math.round((prev + curr) * 100) / 100
            }, 0)
        } else {
            sums[index] = ''
        }
    })
    return sums
}

export function generateServiceName(nameCN: string = '', nameEN: string = ''): string {
    return `${nameEN} ${nameCN}`
}

export function redirectToLogin() {
    // Todo
}

export function logout() {
    
}
