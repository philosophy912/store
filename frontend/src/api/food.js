import request from '@/utils/request'

export function fetchList(query) {
    return request({
        url: '/food/list',
        method: 'get',
        params: query
    })
}