import request from '@/utils/request'

// 新增训练成绩
export function saveTraGrades(data) {
  return request({
    url: '/system/traGrades/save',
    method: 'post',
    data: data
  })
}

