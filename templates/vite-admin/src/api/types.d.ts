import { Method } from 'axios'
import { TableColumnCtx } from 'element-plus/es/el-table/src/table-column/defaults'

export interface RequestSource {
    method?: Method
    url?: string
    cancel: Function
}

export interface ResponseError {
    error: string
    message: string
    path: string
    status: number
    timestamp: string
    exception: string
    errors: Array<any>
}

export interface Pagination {
    pageNo: number
    pageSize: number
    totalCount: number
    totalPage?: number
}

export interface DataList<T> extends Pagination {
    list: Array<T>
}

export interface Response<T = unknown> {
    code: number
    message: string
    data: T
}

// ...

export interface IAnyObject {
    [key: string]: any
}

export interface ISummaryMethodParams {
    columns: Array<TableColumnCtx<any>>
    data: Array<IAnyObject>
}

export type Nullable<T> = T | null

// ...

export interface ICumulus {
    top: IAnyObject,
    bottom: IAnyObject
}

export interface IResultMap {
    success: string
    fail: string
    pending: string
}