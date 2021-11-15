import service from '~/api/request'

export function queryProject (projectId) {
  return service.get('/api/excode/project/' + projectId)
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
