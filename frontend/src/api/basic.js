import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/basic/list',
    method: 'get',
    params: query
  })
}

export function createBasic(data) {
  return request({
    url: '/basic/create',
    method: 'post',
    data
  })
}

export function updateBasic(data) {
  return request({
    url: '/basic/update',
    method: 'post',
    data
  })
}

export function deleteBasic(data) {
  return request({
    url: '/basic/delete',
    method: 'post',
    data
  })
}

export function findAllBasic() {
  return request({
    url: 'basic/findAll',
    method: 'get'
  })
}
