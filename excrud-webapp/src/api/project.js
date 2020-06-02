import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/project',
    method: 'get',
    params: query
  })
}

export function deleteProject(id) {
  return request({
    url: '/project/' + id,
    method: 'delete'
  })
}

export function fetchArticle(id) {
  return request({
    url: '/exstudio/project/detail',
    method: 'get',
    params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: '/exstudio/project/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: '/exstudio/project/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/project',
    method: 'put',
    data
  })
}
