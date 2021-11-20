import { IDataStore, IProject } from "./types"

const defaultProject: IProject = {
    id: '',
    name: '',
    version: '0.1.0-SNAPSHOT',
    description: '',
    author: '',
    organization: '',
    createdTime: '',
    updatedTime: ''
}


const defaultDataStore: IDataStore = {
    id: '',
    name: '',
    type: 'COLUMNAR',
    carrier: 'MYSQL',
    projectId: '',
    description: '',
    nodes: [],
    createdTime: '',
    updatedTime: ''
}

export {
    defaultDataStore,
    defaultProject
}