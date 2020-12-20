import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/material/list',
    method: 'get',
    params: query
  })
}

export function createMaterial(data) {
  return request({
    url: '/material/create',
    method: 'post',
    data
  })
}

export function updateMaterial(data) {
  return request({
    url: '/material/update',
    method: 'post',
    data
  })
}

export function deleteMaterial(data) {
  return request({
    url: '/material/delete',
    method: 'post',
    data
  })
}

export function findAllMaterial() {
  return request({
    url: 'material/findAll',
    method: 'get'
  })
}
