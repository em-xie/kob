import request from '@/utils/request'

// 查询OSS对象存储列表
export function listOss(query) {
  return request({
    url: '/system/oss/list',
    method: 'get',
    params: query
  })
}

// 查询OSS对象基于id串
export function listByIds(ossId) {
  return request({
    url: '/system/oss/listByIds/' + ossId,
    method: 'get'
  })
}

// 删除OSS对象存储
export function delOss(ossId) {
  return request({
    url: '/system/oss/' + ossId,
    method: 'delete'
  })
}

// 删除OSS对象存储
export function upload(file) {
  return request({
    url: '/system/oss/upload',
    method: 'post',
    data: file,
    headers: {
      'Content-Type': "multipart/form-data"
     },
  })
}

