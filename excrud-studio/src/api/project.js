import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/exstudio/project/list',
    method: 'get',
    params: query
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
    url: '/exstudio/project/update',
    method: 'post',
    data
  })
}
