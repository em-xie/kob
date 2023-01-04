import request from '@/utils/request'


  // 获取列表
  export function getlist(page) {
    //console.log(page)

    return request({
      url: 'ranklist/getlist/',
      method: 'get',
      params: {
        page: page,
       
      }
    })
  }
  

  