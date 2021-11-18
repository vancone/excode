import service from '~/api/request'

export function queryDataStore (params) {
  return service.get('/api/excode/data-store/', { params })
}

export function queryProjects (params) {
  return service.get('/api/excode/project', { params })
}

export function createProject (project) {
  return service.post('/api/excode/project', project)
}

export function deleteProject (projectId) {
  return service.delete('/api/excode/project/' + projectId)
}
