export interface IAnyObject {
    [key: string]: any;
}

export interface Pagination {
    pageNo: number,
    pageSize: number,
    totalCount: number,
    totalPage?: number
}

export interface DataList<T> extends Pagination {
    list: Array<T>
}

export interface Response<T = unknown> {
    code: number,
    message: string,
    data: T
}

export interface ResponseList<T> extends Response {
    data: DataList<T>
}

export interface IProject {
    id: string,
    name: string,
    version: string,
    description: string,
    author?: string,
    organization?: string,
    createdTime: string,
    updatedTime: string
}

export interface IDataStoreNode {
    name: string,
    type: string,
    length?: number,
    comment?: string,
    children?: Array<IDataStoreNode>
}

export interface IDataStore {
    id: string,
    name: string,
    type: string,
    carrier: string,
    projectId: string,
    description: string,
    nodes: Array<IDataStoreNode>,
    createdTime: string,
    updatedTime: string
}

