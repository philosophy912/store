import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/category/list',
    method: 'get',
    params: query
  })
}

export function createCategory(data) {
  return request({
    url: '/category/create',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/category/update',
    method: 'post',
    data
  })
}

export function deleteCategory(data) {
  return request({
    url: '/category/delete',
    method: 'post',
    data
  })
}

export function findAllCategory() {
  return request({
    url: 'category/findAll',
    method: 'get'
  })
}
