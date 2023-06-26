import request from '@/utils/request'

// 查询训练成绩列表
export function listTraGrades(query) {
  return request({
    url: '/system/traGrades/list',
    method: 'get',
    params: query
  })
}

// 查询训练成绩详细
export function getTraGrades(id) {
  return request({
    url: '/system/traGrades/' + id,
    method: 'get'
  })
}

// 新增训练成绩
export function addTraGrades(data) {
  return request({
    url: '/system/traGrades',
    method: 'post',
    data: data
  })
}

// 修改训练成绩
export function updateTraGrades(data) {
  return request({
    url: '/system/traGrades',
    method: 'put',
    data: data
  })
}

// 删除训练成绩
export function delTraGrades(id) {
  return request({
    url: '/system/traGrades/' + id,
    method: 'delete'
  })
}
