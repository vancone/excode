import { AxiosResponse } from 'axios'
import service from '~/api/request'
import { IAnyObject, IProject, IDataStore, Response, ResponseList } from './types'

/*
 * Project
 */

export function queryProject (projectId: string
): Promise<AxiosResponse<Response<IProject>>> {
  return service.get('/api/excode/project/' + projectId);
}

export function queryProjects (params: IAnyObject
): Promise<AxiosResponse<ResponseList<IProject>>> {
  return service.get('/api/excode/project', { params });
}

export function createProject (project: IProject) {
  return service.post('/api/excode/project', project);
}

export function updateProject (project: IProject) {
  return service.put('/api/excode/project', project);
}

export function deleteProject (projectId: string) {
  return service.delete('/api/excode/project/' + projectId);
}

/*
 * DataStore
 */

export function queryDataStores (params: IAnyObject) {
  return service.get('/api/excode/data-store', { params });
}

export function queryDataStore (dataStoreId: string) {
  return service.get('/api/excode/data-store/' + dataStoreId);
}

export function createDataStore (dataStore: IDataStore) {
  return service.post('/api/excode/data-store', dataStore);
}

export function updateDataStore (dataStore: IDataStore) {
  return service.put('/api/excode/data-store', dataStore);
}

export function deleteDataStore (dataStoreId: string) {
  return service.delete('/api/excode/data-store/' + dataStoreId);
}

export function generateDataStoreSql (dataStoreId: string) {
  return service.get('/api/excode/data-store/generate-sql/' + dataStoreId);
}


