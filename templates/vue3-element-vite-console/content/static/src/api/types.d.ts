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

export interface IBusinessCatalog {
    name: string,
    menus: Array<IBusinessCatalogMenu>,
}

export interface IBusinessCatalogMenu {
    name: string,
    link?: string,
}

export interface IApplication {
    id: string,
    name: string,
    owner: string,
    description: string,
    organization: string,
    secretKey: string,
}

export interface IUser {
    id: number,
    displayId: string,
    name: string,
    password: string,
    email?: string,
    phone?: string,
}
