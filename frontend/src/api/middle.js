import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/middle/list',
    method: 'get',
    params: query
  })
}

export function createMiddle(data) {
  return request({
    url: '/middle/create',
    method: 'post',
    data
  })
}

export function updateMiddle(data) {
  return request({
    url: '/middle/update',
    method: 'post',
    data
  })
}

export function deleteMiddle(data) {
  return request({
    url: '/middle/delete',
    method: 'post',
    data
  })
}

export function findAllMiddle() {
  return request({
    url: 'middle/findAll',
    method: 'get'
  })
}
