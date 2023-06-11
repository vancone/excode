import { AxiosResponse } from 'axios'
import service from '~/api/request'
import { IAnyObject, IApplication, IBusinessCatalog, IUser, Response, ResponseList } from './types'

export function queryBusinessCatalog(): Promise<AxiosResponse<Response<Array<IBusinessCatalog>>>> {
  return service.get('http://beta.vancone.com/api/portal/catalog/business');
}

export function queryUser(): Promise<AxiosResponse<Response<IUser>>> {
  return service.get('http://passport.beta.vancone.com/api/passport/service/v1/user');
}

export function logout() {
  return service.delete('http://passport.beta.vancone.com/api/passport/service/v1/login');
}

/*
 * Application
 */

export function queryApplication (applicationId: string
): Promise<AxiosResponse<Response<IApplication>>> {
  return service.get('/api/passport/account/' + applicationId);
}

export function queryApplications (params: IAnyObject
  ): Promise<AxiosResponse<ResponseList<IApplication>>> {
  return service.get('/api/passport/application', { params });
}

export function createApplication (application: IApplication) {
  return service.post('/api/passport/application', application);
}

export function updateApplication (application: IApplication) {
  return service.put('/api/passport/application', application);
}

export function deleteApplication (applicationId: string) {
  return service.delete('/api/passport/application/' + applicationId);
}
