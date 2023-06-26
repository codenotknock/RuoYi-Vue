import request from '@/utils/request'

// 查询训练成员信息列表
export function listTraInfo(query) {
  return request({
    url: '/system/traInfo/list',
    method: 'get',
    params: query
  })
}

// 查询训练成员信息详细
export function getTraInfo(id) {
  return request({
    url: '/system/traInfo/' + id,
    method: 'get'
  })
}

// 新增训练成员信息
export function addTraInfo(data) {
  return request({
    url: '/system/traInfo',
    method: 'post',
    data: data
  })
}

// 修改训练成员信息
export function updateTraInfo(data) {
  return request({
    url: '/system/traInfo',
    method: 'put',
    data: data
  })
}

// 删除训练成员信息
export function delTraInfo(id) {
  return request({
    url: '/system/traInfo/' + id,
    method: 'delete'
  })
}
