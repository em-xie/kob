import request from '@/utils/request'


export function sendSimpleMessage(to,subject,text) {
    
  return request({
    url: '/mail/sendSimpleMessage',
    method: 'get',
    params: {
        to: to,
        subject: subject,
        text: text
    }
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

