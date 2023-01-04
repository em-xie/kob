import request from '@/utils/request'


  // 获取用户详细信息
  export function getlist(page) {
    return request({
      url: 'record/getlist/',
      method: 'get',
      params: {
        page: page,
       
      }
    })
  }
  

  